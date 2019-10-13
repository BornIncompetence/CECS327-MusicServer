package com.cecs;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

import com.cecs.model.Playlist;
import com.cecs.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class UserServices {
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    UserServices() {
    }

    /**
     * Loads the user playlist from server, will return null if the authentication
     * fails
     * 
     * @param newUser The user whose playlist is being updated
     * @return <code>true</code> if update is successful and <code>false</code>
     *         otherwise
     * @throws IOException If file could not be found or modified
     */
    public static boolean updateUser(User newUser) throws IOException {
        var users = loadUsers();

        if (users == null) {
            return false;
        } else {
            for (var user : users) {
                if (newUser.username.equalsIgnoreCase(user.username)) {
                    user.setUserPlaylists(newUser.userPlaylists);
                    break;
                }
            }
        }

        var jsonUsers = gson.toJson(users);
        var writer = new FileWriter("users.json");
        writer.write(jsonUsers);
        writer.close();
        return true;
    }

    /**
     * Function to create a new User and add the User to a JSON file
     *
     * @param name Name of user
     * @param pass Password of user
     *
     * @return <code>true</code> If new user is added to file, <code>false</code> if
     *         the username already exists
     *
     * @throws IOException If file could not be modified or created
     */
    public static boolean createAccount(String name, String pass) throws IOException {
        var newUser = new User(name, pass);
        var users = loadUsers();

        User[] newUsers;
        if (users == null) {
            newUsers = new User[] { newUser };
        } else {
            var len = users.length;
            newUsers = new User[len + 1];

            // Check is username is already taken
            for (var user : users) {
                if (newUser.username.equalsIgnoreCase(user.username)) {
                    return false;
                }
            }

            // Append new User to old User list
            System.arraycopy(users, 0, newUsers, 0, len);
            newUsers[len] = newUser;
            Arrays.sort(newUsers);
        }

        // Create string from array of Users and write to file
        var jsonUsers = gson.toJson(newUsers);
        var writer = new FileWriter("users.json");
        writer.write(jsonUsers);
        writer.close();

        return true;
    }

    /**
     * Loads the user playlist from server, will return null if the authentication
     * fails
     * 
     * @param username Username of user
     * @param password Password of user
     * @return <code>ArrayList</code> of user from the server if the user is found,
     *         or <code>null</code> if the user is not found
     * @throws IOException If file could not be found or modified
     */
    public static User login(String username, String password) throws IOException {
        var users = loadUsers();

        return Arrays.stream(users).filter(it -> it.username.equalsIgnoreCase(username) && it.password.equals(password))
                .findFirst().orElse(null);
    }

    /**
     * Loads the users.json file into the program.
     *
     * @throws IOException If file could not be modified or created
     */
    private static User[] loadUsers() throws IOException {
        var file = new File("users.json");
        file.createNewFile();

        // Load current Users from user file
        var reader = new FileReader(file, StandardCharsets.UTF_8);
        return gson.fromJson(reader, User[].class);
    }
}