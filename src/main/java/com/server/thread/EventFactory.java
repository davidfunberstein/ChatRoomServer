package com.server.thread;

import com.shared.interfaces.RequestType;

public class EventFactory {

    public static ClientEvent getClientEvent(String orgMSG, String sender, ClientHandler clientHandler, boolean registeredUser){
        return createClientEvent(orgMSG, sender, clientHandler, registeredUser);
    }

    protected static String[] splitMSGBySpace(String msg){
        String[] splitMSG;
        splitMSG = msg.split(" ");
        if((splitMSG.length == 1 && !splitMSG[0].isEmpty()) || splitMSG.length > 1){
            return splitMSG;
        }
        return null;
    }

    protected static ClientEvent createClientEvent(String orgMessage, String senderName, ClientHandler clientHandler, boolean registeredUser){
        if(orgMessage == null){
            return createClientEventToExit(clientHandler);
        }
        else if(!registeredUser){
            return initNewName(orgMessage, clientHandler);
        }
        else if(orgMessage.equals("exit")){
            return new ClientEvent(null, RequestType.EXIT, senderName, clientHandler);
        }
        else if(notEmptyMSG(orgMessage)) {
            return new ClientEvent(orgMessage, RequestType.SEND_MSG, senderName, clientHandler);
        }
        return null;
    }

    private static ClientEvent initNewName(String orgMessage,ClientHandler clientHandler){
        String[] splitMSG = splitMSGBySpace(orgMessage); // trim
        if(splitMSG == null || splitMSG.length == 0 || orgMessage.isEmpty()){
            return new ClientEvent(null, RequestType.LOGIN_FAILED, null, clientHandler);
        }
        return new ClientEvent(splitMSG[0], RequestType.VALID_USER_NAME, null, clientHandler);
    }

    private static ClientEvent createClientEventToExit(ClientHandler clientHandler){
        return new ClientEvent(null, RequestType.EXIT, null, clientHandler);
    }

    protected static boolean notEmptyMSG(String msg){
        if (splitMSGBySpace(msg) == null){
            return false;
        }
        else {
            return true;
        }
    }
}
