package com.server.thread;

import com.shared.interfaces.RequestType;

public class ClientEvent {
    String msg;
    String sourceName;
    RequestType requestType;
    ClientHandler clientHandler;

    public ClientEvent(String msg, RequestType requestType, String sourceName, ClientHandler clientHandler) {
        this.msg = msg;
        this.requestType = requestType;
        this.sourceName = sourceName;
        this.clientHandler = clientHandler;
    }

    public ClientHandler getClientHandler() {
        return clientHandler;
    }

    public String getMsg() {
        return msg;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public String getSourceName() {
        return sourceName;
    }
}
