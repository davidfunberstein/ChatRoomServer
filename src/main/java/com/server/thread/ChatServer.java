package com.server.thread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {

    private int port;
    private ChatRoomManager chatRoomManager;

    protected ChatServer(int portAdders) {
        this.port = portAdders;
        this.chatRoomManager = new ChatRoomManager();
    }


    protected void createServer(){
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Chat ChatServer is listening on port " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New user connected");
                ClientHandler clientHandler = new ClientHandler(socket, chatRoomManager);
                Thread addNewClientHandler = new Thread(clientHandler);
                addNewClientHandler.start();
                chatRoomManager.addNewClient(clientHandler);
            }
        } catch (IOException ex) {
            System.out.println("Error in the server: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Syntax: java ChatServer <port-number>");
            System.exit(0);
        }
        int port = Integer.parseInt(args[0]);
        ChatServer server = new ChatServer(port);
        server.createServer();
    }
}
