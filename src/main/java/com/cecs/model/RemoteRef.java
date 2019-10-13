package com.cecs.model;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class RemoteRef {
    private int port ;
    private InetAddress address;

    public RemoteRef(int port, InetAddress address) {
        this.port = port;
        this.address = address;
    }

    public RemoteRef() throws UnknownHostException {
        this.port = 5500;
        this.address = InetAddress.getByName("localhost");
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public InetAddress getAddress() {
        return address;
    }

    public void setAddress(InetAddress address) {
        this.address = address;
    }
}
