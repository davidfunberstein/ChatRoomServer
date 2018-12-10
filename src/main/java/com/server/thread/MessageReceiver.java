package com.server.thread;

import com.shared.interfaces.ReceiverMSG;
import com.shared.interfaces.RequestType;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Observable;

public class MessageReceiver extends Observable implements ReceiverMSG, Runnable {

    private BufferedReader reader;
    private ClientHandler clientHandler;

    public MessageReceiver(BufferedReader bufferedReader, ClientHandler clientHandler) {
        this.reader = bufferedReader;
        this.clientHandler = clientHandler;
    }

    public String getName(){
        return clientHandler.getUserName();
    }
    @Override
    public void run() {
        try {
            receiveMSG();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUserName(String name){
        clientHandler.setUserName(name);
        setActionType(RequestType.LOGIN_SUCCESS);
    }

    @Override
    public void setActionType(RequestType requestEnum){
        clientHandler.setActionType(requestEnum);
    }

    @Override
    public RequestType getActionType(){
        return clientHandler.getActionType();
    }

    protected ClientEvent checkMSG(String line){
        ClientEvent clientEvent;
        clientEvent = EventFactory.getClientEvent(line, clientHandler.getUserName(), null,true);
        if(clientEvent == null){
            clientHandler.setQueueMessages("ERROR : Incorrect message structure");
            clientHandler.setActionType(RequestType.SEND_MSG);
            return null;
        }
        return clientEvent;
    }

    protected void notifySender(ClientEvent clientEvent){
        setChanged();
        notifyObservers(clientEvent);
    }

    protected ClientEvent loginProcess(String line){
        ClientEvent clientEvent;
        clientEvent = EventFactory.getClientEvent(line,null,clientHandler,false);
        System.out.println("Receive server start");
        setChanged();
        notifyObservers(clientEvent);
        return clientEvent;
    }

    @Override
    public void receiveMSG() throws IOException{
        ClientEvent clientEvent;
        try {
            while(true) {
                String line = reader.readLine();
                switch (getActionType()) {
                    case CHECK_MSG:
                        clientEvent = checkMSG(line);
                        if(clientEvent != null) {
                            notifySender(clientEvent);
                            if (clientEvent.getRequestType().equals(RequestType.EXIT)) {
                                clientHandler.setActionType(RequestType.EXIT);
                                reader.close();
                                return;
                            }
                        }
                        break;
                    case LOGIN_PROCESS:
                        loginProcess(line);
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e){
            clientHandler.setActionType(RequestType.EXIT);
            setChanged();
            notifyObservers(EventFactory.getClientEvent(null, clientHandler.getUserName(), clientHandler,true));
            reader.close();
            return;
        }
    }
}

