/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package communicatorshared;

/**
 * Message to be sent from client to server and vice versa  using WebSockets.
 * @author Nico Kuijpers
 */
public class CommunicatorWebSocketMessage {
    
    // Operation that is requested at client side
    public CommunicatorWebSocketMessageOperation operation;

    // Content
    public String details;

    public CommunicatorWebSocketMessageOperation getOperation() {
        return operation;
    }

    public void setOperation(CommunicatorWebSocketMessageOperation operation) {
        this.operation = operation;
    }

    public String getDetails()
    {
        return details;
    }

    public void setDetails(String details)
    {
        this.details = details;
    }
}
