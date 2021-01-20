package communicatorserver;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import communicatorshared.CommunicatorWebSocketMessage;
import communicatorshared.CommunicatorWebSocketMessageOperation;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// https://github.com/jetty-project/embedded-jetty-websocket-examples/tree/master/javax.websocket-example/src/main/java/org/eclipse/jetty/demo

/**
 * Server-side implementation of Communicator using WebSockets for communication.
 * 
 * This code is based on example code from:
 * https://github.com/jetty-project/embedded-jetty-websocket-examples/blob/
 * master/javax.websocket-example/src/main/java/org/eclipse/jetty/
 * demo/EventServerSocket.java
 *
 * @author Nico Kuijpers, based on example project
 */

@ServerEndpoint(value="/communicator/")
public class CommunicatorServerWebSocket {
    
    // All sessions
    private static final List<Session> sessions = new ArrayList<>();
    
    // Map each property to list of sessions that are subscribed to that property
    
    @OnOpen
    public void onConnect(Session session) {
        System.out.println("[WebSocket Connected] SessionID: " + session.getId());
        String message = String.format("[New client with client side session ID]: %s", session.getId());
        sessions.add(session);
        CommunicatorWebSocketMessage wbMessage = null;
        session.getAsyncRemote().sendText(String.valueOf(sessions.size()));
        if(sessions.size() == 2)
        {
            for (Session sess : sessions) {
                // Use asynchronous communication
                sess.getAsyncRemote().sendText("USERDETAILS");
            }
        }
        System.out.println("[#sessions]: " + sessions.size());
    }
    
    @OnMessage
    public void onText(String message, Session session) {
        System.out.println("[WebSocket Session ID] : " + session.getId() + " [Received] : " + message);
        handleMessageFromClient(message, session);
    }
    
    @OnClose
    public void onClose(CloseReason reason, Session session) {
        System.out.println("[WebSocket Session ID] : " + session.getId() + " [Socket Closed]: " + reason);
        sessions.remove(session);
    }
    
    @OnError
    public void onError(Throwable cause, Session session) {
        System.out.println("[WebSocket Session ID] : " + session.getId() + "[ERROR]: ");
        cause.printStackTrace(System.err);
    }

    // Handle incoming message from client
    private void handleMessageFromClient(String jsonMessage, Session session) {
        Gson gson = new Gson();
        CommunicatorWebSocketMessage wbMessage = null;
        try {
            wbMessage = gson.fromJson(jsonMessage,CommunicatorWebSocketMessage.class);
        }
        catch (JsonSyntaxException ex) {
            System.out.println("[WebSocket ERROR: cannot parse Json message " + jsonMessage);
            return;
        }
       
        // Operation defined in message
        CommunicatorWebSocketMessageOperation operation;
        operation = wbMessage.getOperation();
        
        // Process message based on operation
        String details = wbMessage.getDetails();
        if (null != operation && null != details && !"".equals(details)) {
            switch (operation) {
                case USERDETAIL:
                    for (Session sess : sessions) {
                        // Use asynchronous communication
                        System.out.println("\t\t >> Client associated with server side session ID: " + sess.getId());
                        sess.getAsyncRemote().sendText(jsonMessage);
                    }
                    break;
                case MOVEMENT:
                    for (Session sess : sessions) {
                        // Use asynchronous communication
                        String[] check = wbMessage.getDetails().split(" ");
                        if(!sess.getId().equals(check[1]))
                        sess.getAsyncRemote().sendText(jsonMessage);
                    }
                    break;
                default:
                    System.out.println("[WebSocket ERROR: cannot process Json message " + jsonMessage);
                    break;
            }
        }
    }
}