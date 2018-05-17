/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.electriccloud;

import java.io.PrintWriter;
import javax.jms.*;  

/**
 *
 * @author imago
 */
public class SampleListener implements MessageListener {
    
    private PrintWriter out;
    
    void setPrintWriter(PrintWriter out) {
        this.out = out;
    }

    @Override
    public void onMessage(Message message) {
        try {
            TextMessage msg = (TextMessage) message;
            out.println("Received message: " + msg.getText());
        } catch (JMSException e) {
            out.println(e.getMessage());
        }
    }
    
}
