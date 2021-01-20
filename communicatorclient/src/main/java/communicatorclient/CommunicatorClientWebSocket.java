package communicatorclient;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import communicatorshared.CommunicatorWebSocketMessage;
import communicatorshared.CommunicatorWebSocketMessageOperation;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

// https://github.com/jetty-project/embedded-jetty-websocket-examples/tree/master/javax.websocket-example/src/main/java/org/eclipse/jetty/demo

/**
 * Client-side implementation of abstract class Communicator using 
 * WebSockets for communication.
 * 
 * This code is based on the example code from:
 * https://github.com/jetty-project/embedded-jetty-websocket-examples/blob/
 * master/javax.websocket-example/src/main/java/org/eclipse/jetty/
 * demo/EventServerSocket.java
 *
 * @author Nico Kuijpers
 */
@ClientEndpoint
public class CommunicatorClientWebSocket extends Communicator {
    
    // Singleton
    private static CommunicatorClientWebSocket instance = null;
    
    /**
     * The local websocket uri to connect to.
     */
    private final String uri = "ws://localhost:8095/communicator/";
    
    private Session session;

    private String message;
    
    private Gson gson = null;
    
    // Status of the webSocket client
    boolean isRunning = false;
    
    // Private constructor (singleton pattern)
    private CommunicatorClientWebSocket()
    {
        gson = new Gson();
    }
    
    /**
     * Get singleton instance of this class.
     * Ensure that only one instance of this class is created.
     * @return instance of client web socket
     */
    public static CommunicatorClientWebSocket getInstance() {
        if (instance == null) {
            System.out.println("[WebSocket Client create singleton instance]");
            instance = new CommunicatorClientWebSocket();
        }
        return instance;
    }

    /**
     *  Start the connection.
     */
    @Override
    public void start() {
        System.out.println("[WebSocket Client start connection]");
        if (!isRunning) {
            startClient();
            isRunning = true;
        }
    }

    @Override
    public void stop() {
        System.out.println("[WebSocket Client stop]");
        if (isRunning) {
            stopClient();
            isRunning = false;
        }
    }

    @Override
    public void sendDetails(String details)
    {
        CommunicatorWebSocketMessage message = new CommunicatorWebSocketMessage();
        message.setDetails(details);
        message.setOperation(CommunicatorWebSocketMessageOperation.USERDETAIL);
        sendMessageToServer(message);
    }

    @Override
    public void sendMove(String position)
    {
        CommunicatorWebSocketMessage message = new CommunicatorWebSocketMessage();
        message.setDetails(position);
        message.setOperation(CommunicatorWebSocketMessageOperation.MOVEMENT);
        sendMessageToServer(message);
    }

    @OnOpen
    public void onWebSocketConnect(Session session){
        this.session = session;
    }

    @OnMessage
    public void onWebSocketText(String message, Session session){
        this.message = message;
        processMessage(message);
    }

    @OnError
    public void onWebSocketError(Session session, Throwable cause) {
    }
    
    @OnClose
    public void onWebSocketClose(CloseReason reason){
        session = null;
    }
    
    private void sendMessageToServer(CommunicatorWebSocketMessage message) {
        String jsonMessage = gson.toJson(message);
        // Use asynchronous communication
        session.getAsyncRemote().sendText(jsonMessage);
    }
    
    /**
     * Get the latest message received from the websocket communication.
     * @return The message from the websocket communication
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set the message, but no action is taken when the message is changed.
     * @param message the new message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Start a WebSocket client.
     */
    private void startClient() {
        System.out.println("[WebSocket Client start]");
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI(uri));
            
        } catch (IOException | URISyntaxException | DeploymentException ex) {
            // do something useful eventually
            ex.printStackTrace();
        }
    }

    /**
     * Stop the client when it is running.
     */
    private void stopClient(){
        try {
            session.close();

        } catch (IOException ex){
            // do something useful eventually
            ex.printStackTrace();
        }
    }
    
    // Process incoming json message
    private void processMessage(String jsonMessage)
    {
        if(jsonMessage.length() == 1)
        {
            this.setChanged();
            this.notifyObservers(jsonMessage);
            return;
        }

        if(jsonMessage.equals("USERDETAILS"))
        {
            CommunicatorMessage commMessage = new CommunicatorMessage();
            commMessage.setDetails("USERDETAILS");
            this.setChanged();
            this.notifyObservers(jsonMessage);
            return;
        }

        // Parse incoming message
        CommunicatorWebSocketMessage wsMessage;
        try {
            wsMessage = gson.fromJson(jsonMessage, CommunicatorWebSocketMessage.class);
        }
        catch (JsonSyntaxException ex) {
            System.out.println("[WebSocket Client ERROR: cannot parse Json message " + jsonMessage);
            return;
        }

        // Obtain content from message
        String details = wsMessage.getDetails();
        if (details == null || "".equals(details)) {
            System.out.println("[WebSocket Client ERROR: message without content]");
            return;
        }
        
        // Create instance of CommunicaterMessage for observers
        CommunicatorMessage commMessage = new CommunicatorMessage();
        commMessage.setDetails(details);
        
        // Notify observers
        this.setChanged();
        this.notifyObservers(commMessage);
    }
}
