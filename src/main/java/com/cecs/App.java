package com.cecs;

import java.io.IOException;

import com.cecs.DFS.DFS;
import com.cecs.DFS.DFSCommand;

public class App {
    public static void main(String[] args) {

        new Thread() {
            public void run() {
                String[] args = new String[1];
                args[0] = "2001";
                try {
                    DFSCommand.main(args);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread(){
            public void run() {
                String[] args = new String[1];
                args[0] = "2002";
                try {
                    DFSCommand.main(args);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread(){
            public void run() {
                String[] args = new String[1];
                args[0] = "2003";
                try {
                    DFSCommand.main(args);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();

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
