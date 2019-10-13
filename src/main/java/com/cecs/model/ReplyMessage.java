package com.cecs.model;

public class ReplyMessage {
    private int requestId;
    private String message;

    public ReplyMessage(int requestId, String message) {
        this.requestId = requestId;
        this.message = message;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
