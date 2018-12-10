package com.server.thread;

import com.shared.interfaces.RequestType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.BufferedReader;
import java.net.Socket;

public class ClientHandlerTest {

    ChatRoomManager chatRoomManager = null;
    ClientHandler clientHandler1 = null;
    ClientHandler clientHandler2 = null;

    MessageReceiver receiver1 = null;
    MessageReceiver receiver2 = null;

    @Mock
    Socket socket;
    @Mock
    BufferedReader bufferedReader;
    @Before
    public void setUp(){
        this.chatRoomManager = new ChatRoomManager();

        this.clientHandler1 = new ClientHandler(socket, chatRoomManager);
        chatRoomManager.addNewClient(clientHandler1);
        ClientEvent clientEvent1 = new ClientEvent("david", RequestType.VALID_USER_NAME,null, clientHandler1);
        receiver1 = new MessageReceiver(bufferedReader, clientHandler1);
        chatRoomManager.initUserNameOfLogin(clientEvent1, receiver1);
        receiver1.addObserver(chatRoomManager);

        this.clientHandler2 = new ClientHandler(socket, chatRoomManager);
        chatRoomManager.addNewClient(clientHandler2);
        ClientEvent clientEvent2 = new ClientEvent("sasi", RequestType.VALID_USER_NAME,null, clientHandler2);
        receiver2 = new MessageReceiver(bufferedReader, clientHandler2);
        chatRoomManager.initUserNameOfLogin(clientEvent2, receiver2);
        receiver2.addObserver(chatRoomManager);
    }

    @Test
    public void getActionType() {
        ClientHandler clientHandler3 = new ClientHandler(socket, chatRoomManager);
        chatRoomManager.addNewClient(clientHandler3);
        Assert.assertEquals(RequestType.LOGIN_PROCESS, clientHandler3.getActionType());

        ClientEvent clientEvent3 = EventFactory.getClientEvent("", null, clientHandler3, false);
        MessageReceiver receiver3 = new MessageReceiver(bufferedReader, clientHandler3);
        chatRoomManager.initUserNameOfLogin(clientEvent3, receiver3);
        receiver3.addObserver(chatRoomManager);
        receiver3.notifySender(clientEvent3);
        Assert.assertEquals(RequestType.LOGIN_FAILED,clientHandler3.getActionType());
    }

    @Test
    public void setQueueMessages() {
        clientHandler1.setActionType(RequestType.LOGIN_SUCCESS);
        Assert.assertEquals(RequestType.LOGIN_SUCCESS, clientHandler1.getActionType());

    }

    @Test
    public void receiveMSGObserver() {
        clientHandler1.setActionType(RequestType.CHECK_MSG);
        String readLine = "Hi all..";
        ClientEvent clientEvent = receiver1.checkMSG(readLine);
        receiver1.notifySender(clientEvent);
        Assert.assertEquals("New message from david: Hi all..",clientHandler2.popQueueMessages());
    }
}