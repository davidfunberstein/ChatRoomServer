package com.server.thread;

import com.shared.interfaces.RequestType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.PrintWriter;
import java.net.Socket;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;

public class MessageSenderTest {

    MessageSender messageSender = null;
    ClientHandler clientHandler = null;
    @Mock
    ChatRoomManager chatRoomManager;
    @Mock
    Socket socket;

    @Before
    public void setUp()  {
        clientHandler = new ClientHandler(socket, chatRoomManager);
        clientHandler.setQueueMessages("Hi");
        PrintWriter printWriter = Mockito.mock(PrintWriter.class);
        doAnswer(invocation -> null).when(printWriter).println(anyString());
        messageSender = new MessageSender(printWriter, clientHandler);
    }

    @Test
    public void analyseMsgToSendTest(){
        String msg = messageSender.analyseMsgToSend();
        Assert.assertEquals(msg,"Hi");
        Assert.assertEquals(RequestType.LOGIN_PROCESS, messageSender.getActionType());
        Assert.assertEquals("Hi", messageSender.sendMSG("Hi"));
    }

    @Test
    public void runTest(){
        clientHandler.setQueueMessages("david");
        messageSender.setActionType(RequestType.SEND_MSG);
        Thread t = new Thread(messageSender);
        t.start();
        t.interrupt();
        Assert.assertEquals(RequestType.SEND_MSG, messageSender.getActionType());
    }
}