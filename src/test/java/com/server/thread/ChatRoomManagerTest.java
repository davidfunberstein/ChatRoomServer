package com.server.thread;
import com.shared.interfaces.RequestType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import java.net.Socket;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;

public class ChatRoomManagerTest {
    ChatRoomManager chatRoomManager = null;
    ClientHandler clientHandler1 = null;
    ClientHandler clientHandler2 = null;
    @Mock
    Socket socket;

    MessageReceiver messageReceiver = null;

    @Before
    public void setUp(){
        messageReceiver = Mockito.mock(MessageReceiver.class);
        doAnswer(invocationOnMock -> null).when(messageReceiver).setUserName(anyString());
        chatRoomManager = new ChatRoomManager();
        clientHandler1 = new ClientHandler(socket, chatRoomManager);
        chatRoomManager.addNewClient(clientHandler1);
        clientHandler1.setUserName("david");

        ClientEvent clientEvent = new ClientEvent("david", RequestType.VALID_USER_NAME,null, clientHandler1);
        chatRoomManager.initUserNameOfLogin(clientEvent, messageReceiver);

        clientHandler2 = new ClientHandler(socket, chatRoomManager);
        chatRoomManager.addNewClient(clientHandler2);

    }

    @Test
    public void removeUserTest() {
        ClientEvent clientEvent = new ClientEvent("david", RequestType.VALID_USER_NAME,null, clientHandler1);

        Assert.assertEquals(chatRoomManager.initUserNameOfLogin(clientEvent, messageReceiver), false);

        chatRoomManager.removeUser("david");
        Assert.assertEquals(chatRoomManager.initUserNameOfLogin(clientEvent, messageReceiver), true);

        chatRoomManager.addNewClient(clientHandler2);
        Assert.assertTrue(chatRoomManager.removeUser(clientHandler2));

        Assert.assertFalse(chatRoomManager.removeUser(clientHandler1));
    }

    @Test
    public void sendErrorUserTargetTest(){
        chatRoomManager.sendErrorUserTarget("david");
        String msgFromQueue = clientHandler1.popQueueMessages();
        Assert.assertEquals("ERROR: The user target not found.", msgFromQueue);
    }

    @Test
    public void isCorrectUserNameToLogInTest() {
        boolean correctSameName = chatRoomManager.isCorrectUserNameToLogIn("david");
        boolean correctDifName = chatRoomManager.isCorrectUserNameToLogIn("mosh");
        boolean correctNull = chatRoomManager.isCorrectUserNameToLogIn(null);
        Assert.assertFalse(correctSameName);
        Assert.assertTrue(correctDifName);
        Assert.assertTrue(correctNull);
    }

    @Test
    public void createMSGTest() {
        String userSender = "david";
        String messageBody = "Hi";
        String errorMsg = chatRoomManager.createMSG(userSender, messageBody, true);
        Assert.assertEquals("ERROR: The user target not found.",errorMsg);

        String newMsg = chatRoomManager.createMSG(userSender, messageBody, false);
        Assert.assertEquals(String.format("New message from %s: %s",userSender, messageBody), newMsg);

        ClientEvent clientEventMsg = new ClientEvent(messageBody, RequestType.SEND_MSG, userSender, clientHandler1);
        chatRoomManager.sendMSGToUserTarget(clientEventMsg);
        ClientEvent clientEvent = new ClientEvent("asi", RequestType.VALID_USER_NAME,null, clientHandler2);
        chatRoomManager.initUserNameOfLogin(clientEvent, messageReceiver);
        chatRoomManager.sendMSGToUserTarget(clientEventMsg);
        String msgFromAnotherUserQueue = clientHandler2.popQueueMessages();
        Assert.assertEquals(String.format("New message from %s: %s",userSender, messageBody), msgFromAnotherUserQueue);

    }
}