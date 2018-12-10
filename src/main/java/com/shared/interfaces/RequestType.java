package com.shared.interfaces;

public enum RequestType {
    LOGIN_SUCCESS("login"),
    EXIT ("exit_"),
    SEND_MSG ("sendMsg"),
    LOGIN_FAILED("logInFailed"),
    LOGIN_PROCESS("logInProcess"),
    CHECK_MSG ("checkMsg"),
    LOGIN_WAIT("logInWait"),
    ADD_USER("addUser"),
    REMOVE_USER("removeUser"),
    VALID_USER_NAME("validUserName");

    private String requestType;

    RequestType(String requestType){
        this.requestType = requestType;
    }

    public String getRequestType() {
        return this.requestType;
    }

    public static RequestType fromString(String requestType) {
        for (RequestType r : RequestType.values()) {
            if (r.requestType.equalsIgnoreCase(requestType)) {
                return r;
            }
        }
        throw new IllegalArgumentException("No constant with text " + requestType + " found");
    }
}
