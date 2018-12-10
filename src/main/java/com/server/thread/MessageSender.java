package com.server.thread;

import com.shared.interfaces.RequestType;
import com.shared.interfaces.SenderMSG;

import java.io.PrintWriter;

public class MessageSender implements SenderMSG, Runnable {
    private PrintWriter sender;
    private ClientHandler clientHandler;

    public MessageSender(PrintWriter printWriter, ClientHandler clientHandler) {
        this.sender = printWriter;
        this.clientHandler = clientHandler;
    }

    @Override
    public String sendMSG(String msg) {
        sender.println(msg);
        return msg;
    }

    @Override
    public void setActionType(RequestType requestEnum){
        clientHandler.setActionType(requestEnum);
    }

    @Override
    public RequestType getActionType(){
        return clientHandler.getActionType();
    }

    protected String analyseMsgToSend(){
        String sendMsg;
        if ((sendMsg = clientHandler.popQueueMessages()) != null) {
            sendMSG(RequestType.SEND_MSG.getRequestType());
            return sendMsg;
        }
        return null;
    }
    @Override
    public void run() {
        try {
            while (true) {
                switch (getActionType()) {
                    case SEND_MSG:
                        sendMSG(analyseMsgToSend());
                        setActionType(RequestType.CHECK_MSG);
                        break;
                    case LOGIN_SUCCESS:
                        sendMSG(RequestType.LOGIN_SUCCESS.getRequestType());
                        setActionType(RequestType.CHECK_MSG);
                        break;
                    case EXIT:
                        sendMSG(RequestType.EXIT.getRequestType());
                        sender.close();
                        return;
                    case LOGIN_FAILED:
                        sendMSG(RequestType.LOGIN_FAILED.getRequestType());
                        setActionType(RequestType.LOGIN_PROCESS);
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e){
            sendMSG(RequestType.EXIT.getRequestType());
            sender.close();
        }
    }
}
