/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package communicatorclient;

/**
 * Message to be sent from client to client through a Communicator.
 * All clients that are subscribed to the property of a message
 * will receive a copy of this message.
 * @author Nico Kuijpers
 */
public class CommunicatorMessage {

    // Content of the message
    private String details;

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
