package com.cecs;

import java.io.File;
import java.io.FileReader;
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
    UserServices() {
    }

    /**
     * Loads the user playlist from server, will return null if the authentication fails
     * @param username Username of user
     * @param password Password of user
     * @return ArrayList of user from the server if the user is found, or null if the user is not found
     * @throws IOException If file could not be found or modified
     */
    public static User login(String username, String password) throws IOException {
        var loginUser = new User(username, password);
        var gson = new GsonBuilder().setPrettyPrinting().create();
        var users = loadUsers(gson);

        return Arrays.stream(users).filter(
                it -> it.username.equalsIgnoreCase(loginUser.username) && it.password.equals(loginUser.password))
                .findFirst().orElse(null);
    }

    /**
     * Loads the users.json file into the program.
     *
     * @throws IOException If file could not be modified or created
     */
    private static User[] loadUsers(Gson gson) throws IOException {
        var file = new File("users.json");
        file.createNewFile();

        // Load current Users from user file
        var reader = new FileReader(file, StandardCharsets.UTF_8);
        return gson.fromJson(reader, User[].class);
    }
}