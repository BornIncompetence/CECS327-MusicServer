package com.cecs;

import java.io.IOException;

import com.cecs.DFS.DFS;

public class App {
    public static void main(String[] args) {

        DFS dfs = null;
        try{
            dfs = new DFS(2000);
            dfs.join("127.0.0.1", 2001);
        } catch (Exception e){
            e.printStackTrace();
        }

        var comm = new Communication(5500, 32768, dfs);
        try {
            comm.openConnection();
        } catch (IOException e) {
            System.err.println("The server has encountered an error.");
            e.printStackTrace();
        }
    }
}
