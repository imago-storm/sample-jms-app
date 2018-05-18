/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.electriccloud;

/**
 *
 * @author imago
 */
public class Constants {
    
    public static final String CONNECTION_FACTORY = "SampleConnectionFactory";
    public static final String QUEUE = "SampleQueue";
    public static final String TOPIC = "SampleTopic";
    public static final String MESSAGE = "Sample Message";
    
    public static final String CONNECTION_FACTORY_PARAMETER = "connectionFactory";
    public static final String QUEUE_PARAMETER = "queue";
    public static final String TOPIC_PARAMETER = "topic";
    public static final String COMMAND_PARAMETER = "command";
    public static final String MESSAGE_PARAMETER = "message";
    
    public static final String SEND = "send";
    public static final String RECEIVE = "receive";
    
    
    public static String withDefault(String initial, String def) {
        if (initial == null || initial.equals("")) {
            return def;
        }
        else {
            return initial;
        }
    } 
    
}
