package com.server.thread;

import com.shared.interfaces.RequestType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

import static org.mockito.Mockito.doAnswer;

public class MessageReceiverTest {

    MessageReceiver messageReceiver = null;
    @Mock
    ChatRoomManager chatRoomManager;
    @Mock
    Socket socket;
    @Before
    public void setUp(){
        ClientHandler clientHandler = new ClientHandler(socket, chatRoomManager);
        clientHandler.setQueueMessages("Hi");
        BufferedReader bufferedReader = Mockito.mock(BufferedReader.class);
        try {
            doAnswer(invocationOnMock -> null).when(bufferedReader).readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        messageReceiver = new MessageReceiver(bufferedReader, clientHandler);
    }
    @Test
    public void getName() {
    }

    @Test
    public void run() {
    }

    @Test
    public void setUserNameTest() {
        messageReceiver.setUserName("david");
        Assert.assertEquals(messageReceiver.getName(), "david");
    }

    @Test
    public void setActionType() {
        Assert.assertEquals(messageReceiver.getActionType(), RequestType.LOGIN_PROCESS);
    }

    @Test
    public void checkMSGTest() {
        ClientEvent sendEmptyMessage = messageReceiver.checkMSG("");
        Assert.assertEquals(sendEmptyMessage, null);

        ClientEvent sendCorrectMessage = messageReceiver.checkMSG("Hi");
        Assert.assertEquals(sendCorrectMessage.getRequestType(), RequestType.SEND_MSG);
    }

    @Test
    public void loginProcessTest(){
        ClientEvent initNameFailed = messageReceiver.loginProcess("");
        Assert.assertEquals(initNameFailed.getRequestType(), RequestType.LOGIN_FAILED);

        ClientEvent initNameSeccess = messageReceiver.loginProcess("david");
        Assert.assertEquals(initNameSeccess.getRequestType(), RequestType.VALID_USER_NAME);

    }
}