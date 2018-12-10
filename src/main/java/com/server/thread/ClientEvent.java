package com.server.thread;

import com.shared.interfaces.RequestType;

public class ClientEvent {
    String message;
    String sourceName;
    RequestType requestType;
    ClientHandler clientHandler;

    public ClientEvent(String message, RequestType requestType, String sourceName, ClientHandler clientHandler) {
        this.message = message;
        this.requestType = requestType;
        this.sourceName = sourceName;
        this.clientHandler = clientHandler;
    }

    public ClientHandler getClientHandler() {
        return clientHandler;
    }

    public String getMessage() {
        return message;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public String getSourceName() {
        return sourceName;
    }
}
