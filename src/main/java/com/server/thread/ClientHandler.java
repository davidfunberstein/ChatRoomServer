package com.server.thread;

import com.shared.interfaces.RequestType;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ClientHandler implements Runnable{
    private Socket socket;
    private String userName;
    private MessageReceiver receiver;
    private MessageSender sender;
    private ConcurrentLinkedQueue<String> queueMessages;
    private RequestType actionType;
    private ChatRoomManager chatRoomManager;

    public ClientHandler(Socket socket, ChatRoomManager chatRoomManager){
        this.socket = socket;
        queueMessages = new ConcurrentLinkedQueue<>();
        actionType = RequestType.LOGIN_PROCESS;
        this.chatRoomManager = chatRoomManager;
    }

    public void setActionType(RequestType requestEnum){
        actionType = requestEnum;
    }

    public RequestType getActionType(){
        return actionType;
    }

    public void setQueueMessages(String msg){
        queueMessages.add(msg);
    }

    public String popQueueMessages(){
        return queueMessages.poll();
    }

    public String getUserName() {
        return userName;
    }

    public boolean setUserName(String newUserName){
        if(newUserName != null){
            this.userName = newUserName;
            return true;
        }
        else {
            return false;
        }
    }

    protected void initReceiveSenderThreads(){
        PrintWriter printWriter = null;
        BufferedReader bufferedReader = null;
        try {
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            InputStream input = socket.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(input));
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        receiver = new MessageReceiver(bufferedReader, this);
        receiver.addObserver(chatRoomManager);
        sender = new MessageSender(printWriter, this);
        Thread receiverThread = new Thread(receiver);
        Thread senderThread = new Thread(sender);
        receiverThread.start();
        senderThread.start();
    }

    public void run(){
        initReceiveSenderThreads();
    }

}
