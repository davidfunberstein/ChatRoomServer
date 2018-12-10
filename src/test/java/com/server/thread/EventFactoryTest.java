package com.server.thread;

import com.shared.interfaces.RequestType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.net.Socket;

public class EventFactoryTest {

    ChatRoomManager chatRoomManager = null;
    @Mock
    Socket socket;
    @Before
    public void setUpTest() {
        chatRoomManager = new ChatRoomManager();
        ClientHandler clientHandler1 = new ClientHandler(socket, chatRoomManager);
        clientHandler1.setUserName("david");
        chatRoomManager.addNewClient(clientHandler1);

        ClientHandler clientHandler2 = new ClientHandler(socket, chatRoomManager);
        clientHandler1.setUserName("dor");
        chatRoomManager.addNewClient(clientHandler2);
    }

    @Test
    public void splitMSGTest() {
        String[] msgSplit = EventFactory.splitMSGBySpace("Hi, how are you");
        Assert.assertEquals(4, msgSplit.length);

        String[] emptyMsg = EventFactory.splitMSGBySpace("");
        Assert.assertNull(emptyMsg);
    }

    @Test
    public void createClientEventTest() {
        ClientEvent clientEvent = EventFactory.createClientEvent(null, "david", null, true);
        RequestType requestEnumExit = clientEvent.getRequestType();
        Assert.assertTrue(RequestType.EXIT.equals(requestEnumExit));

        ClientEvent clientEventName = EventFactory.createClientEvent("david", null, null,false);
        RequestType requestEnumValidName = clientEventName.getRequestType();
        Assert.assertTrue(RequestType.VALID_USER_NAME.equals(requestEnumValidName));
    }
}