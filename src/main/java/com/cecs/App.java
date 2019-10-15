package com.cecs;

import java.io.IOException;

public class App {
    public static void main(String[] args) {
        var comm = new Communication(5500, 32768);
        try {
            comm.openConnection();
        } catch (IOException e) {
            System.err.println("The server has encountered an error.");
            e.printStackTrace();
        }
    }
}
