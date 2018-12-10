package com.server.thread;

import com.shared.interfaces.RequestType;

import java.util.*;

public class ChatRoomManager implements Observer {
    private Map<String,ClientHandler> clientHandlerMap;
    private Set<ClientHandler> clientsProcessInit;
    private final String ERROR_MESSAGE = "ERROR: The user target not found.";

    public ChatRoomManager() {
        this.clientsProcessInit = new HashSet<>();
        this.clientHandlerMap = new HashMap<>();
    }

    public void addNewClient(ClientHandler clientHandler){
        clientsProcessInit.add(clientHandler);
    }

    @Override
    public void update(Observable o, Object arg) {
        MessageReceiver messageReceiver = (MessageReceiver)o;
        ClientEvent newEvent = (ClientEvent) arg;
        switch (newEvent.getRequestType()){
            case LOGIN_FAILED:
                setActionType(messageReceiver, RequestType.LOGIN_FAILED); // Notify that connection failed
                break;
            case EXIT:
                String nameRemove = messageReceiver.getName();
                setActionType(messageReceiver, RequestType.EXIT);
                if(newEvent.getClientHandler() != null){
                    removeUser(newEvent.getClientHandler());
                }
                removeUser(nameRemove);
                break;
            case VALID_USER_NAME:
                initUserNameOfLogin(newEvent, messageReceiver);
                break;
            case SEND_MSG:
                sendMSGToUserTarget(newEvent);
                break;
        }
    }

    protected boolean initUserNameOfLogin(ClientEvent newEvent, MessageReceiver messageReceiver){
        String nameLogIn = newEvent.getMsg();
        if (isCorrectUserNameToLogIn(nameLogIn)){
            clientHandlerMap.put(nameLogIn, newEvent.getClientHandler());
            clientsProcessInit.remove(newEvent.getClientHandler());
            messageReceiver.setUserName(nameLogIn);
            return true;
        }
        setActionType(messageReceiver, RequestType.LOGIN_FAILED);
        return false;
    }

    private void setActionType(MessageReceiver messageReceiver, RequestType requestEnum){
        messageReceiver.setActionType(requestEnum);
    }

    protected boolean isCorrectUserNameToLogIn(String name){
        if(clientHandlerMap.get(name) == null || name.trim().isEmpty()){//trim
            return true;
        }
        return false;
    }

    protected void sendMSGToUserTarget(ClientEvent msgFromSender){//"target"
        String senderName = msgFromSender.getSourceName();
        String messageBody = msgFromSender.getMsg(); //message
        if(clientHandlerMap.size() > 1) {
            for (Map.Entry<String, ClientHandler> entry : clientHandlerMap.entrySet()) {
                if(!entry.getKey().equals(msgFromSender.getSourceName())) {
                    entry.getValue().setQueueMessages(createMSG(senderName, messageBody, false));
                    entry.getValue().setActionType(RequestType.SEND_MSG);
                }
            }
        }
        else{
            sendErrorUserTarget(senderName);
            return;
        }
    }

    protected void sendErrorUserTarget(String userSender){
        ClientHandler clientSender = clientHandlerMap.get(userSender);
        clientSender.setQueueMessages(createMSG(null, null, true));
        clientSender.setActionType(RequestType.SEND_MSG);
    }

    protected String createMSG(String messagePrefix, String messageBody, boolean errorMSG){
        if(errorMSG){
            return String.format(ERROR_MESSAGE);
        }
        return String.format("New message from %s: %s",messagePrefix, messageBody);
    }

    protected void removeUser(String name){
        clientHandlerMap.remove(name);
    }

    protected boolean removeUser(ClientHandler clientHandler){
        return clientsProcessInit.remove(clientHandler);
    }
}
