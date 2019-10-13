package com.cecs;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.cecs.model.RemoteRef;
import com.cecs.model.ReplyMessage;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class App {
    private static  RemoteRef remoteRef;
    private static final byte[] buffer = new byte[16384];
    private static HashMap<String, Object> listOfObjects = new HashMap<>();
    private static Map<InetAddress, ReplyMessage> history; // store last message from each client to avoid duplicate
    public static void main(String[] args) {
        registerObject(new SongServices(), "SongServices");
        registerObject(new UserServices(), "UserServices");
        history =  new HashMap<>();
        try {
            remoteRef = new RemoteRef();
            openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openConnection() throws IOException {
        var socket = new DatagramSocket(remoteRef.getPort());
        while (true) {
            // Receive
            final var inbound = new DatagramPacket(buffer, buffer.length);
            System.out.println("Listening...");
            socket.receive(inbound);
            // Get client info
            remoteRef.setAddress(inbound.getAddress());
            remoteRef.setPort(inbound.getPort());

            // Break out of loop if server receives end, else do an action
            final var message = new String(inbound.getData(), 0, inbound.getLength());
            if (message.equals("end")) {
                System.out.println("Server is closing...");
                break;
            }
            final var out = dispatch(message).getBytes();

            // Send back to client
            final var outboundPacket = new DatagramPacket(out, out.length, remoteRef.getAddress(), remoteRef.getPort());
            System.out.format("Sending message of size %s to %s\n", out.length, remoteRef.getAddress());
            socket.send(outboundPacket);
        }
        socket.close();
    }

    /**
     * Parses and invokes the method as described in the passed in JSON string
     * Returns the result of invoking the method if the request string is valid.
     *
     * @param request The JSON object represented as a string
     *
     * @return String representing either the return value of the function, or an
     *         error if one occurred
     */
    private static String dispatch(String request) {
        JsonObject jsonReturn = new JsonObject();
        JsonParser parser = new JsonParser();
        JsonObject jsonRequest = parser.parse(request).getAsJsonObject();
        var gson = new GsonBuilder().setPrettyPrinting().create();
        int requestId = 0;
        try {
            // Obtains the object pointing to SongServices
            Object object = listOfObjects.get(jsonRequest.get("objectName").getAsString());
            requestId = jsonRequest.get("requestId").getAsInt();
            String semantic = jsonRequest.get("semantic").getAsString();

            if(semantic == "AT_MOST_ONCE"){  // retransmit reply
                ReplyMessage message = history.getOrDefault(remoteRef.getAddress(), null);
                if(message != null && message.getRequestId() == requestId){
                    return message.getMessage();
                }
            }
            //else if(semantic == "AT_LEAST_ONCE" or message is not found in history) {

            // Obtains the method from the list of methods that exist for the class
            var optionalMethod = Arrays.stream(object.getClass().getMethods()).filter(it -> it.getName().equals(jsonRequest.get("remoteMethod").getAsString())).findFirst();
            if (optionalMethod.isEmpty()) {
                jsonReturn.addProperty("error", "Method does not exist");
                return jsonReturn.toString();
            }
            var method = optionalMethod.get();

            // Prepare the parameters
            var types = method.getParameterTypes();
            var parameters = new Object[types.length];
            String parameterStrs;
            {
                var it = jsonRequest.get("param").getAsJsonObject().entrySet().iterator();
                for (int i = 0; i < types.length; ++i) {
                    parameterStrs = it.next().getValue().getAsString();
                    switch (types[i].getCanonicalName()) {
                    case "java.lang.Long":
                        parameters[i] = Long.parseLong(parameterStrs);
                        break;
                    case "java.lang.Integer":
                        parameters[i] = Integer.parseInt(parameterStrs);
                        break;
                    case "java.lang.String":
                        parameters[i] = parameterStrs;
                        break;
                    }
                }
            }

            var ret = method.invoke(object, parameters);
            jsonReturn.addProperty("ret", gson.toJson(ret));

        } catch (InvocationTargetException | IllegalAccessException e) {
            var errorField = String.format("Error on %s.%s()", jsonRequest.get("objectName").getAsString(),
                    jsonRequest.get("remoteMethod").getAsString());
            jsonReturn.addProperty("error", errorField);
        }
        
        // only store the last reply message for each client
        history.put(remoteRef.getAddress(), new ReplyMessage(requestId, jsonReturn.toString()));

        return jsonReturn.toString();
    }

    /*
     * registerObject: It register the objects that handle the request
     * 
     * @param remoteMethod: It is the name of the method that objectName implements.
     * 
     * @objectName: It is the main class that contains the remote methods each
     * object can contain several remote methods
     */
    public static void registerObject(Object remoteMethod, String objectName) {
        listOfObjects.put(objectName, remoteMethod);
    }
}
