/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.electriccloud;

import static com.electriccloud.Constants.*;
import java.io.IOException;
import java.io.PrintWriter;
import javax.jms.*;
import javax.naming.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author imago
 */
public class JMSTopic extends HttpServlet {


    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet JMSTopic</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet JMSTopic at " + request.getContextPath() + "</h1>");
            out.println("<pre>");
            String cfName = withDefault(request.getParameter(CONNECTION_FACTORY_PARAMETER), CONNECTION_FACTORY);
            String topicName = withDefault(request.getParameter(TOPIC_PARAMETER), TOPIC);
            String command = withDefault(request.getParameter(COMMAND_PARAMETER), "");
            String message = withDefault(request.getParameter(MESSAGE_PARAMETER), MESSAGE);
            try {
                      
                switch(command) {
                    case SEND:
                        sendMessage(out, message, cfName, topicName);
                        break;
                    case RECEIVE:
                        receiveMessage(out, cfName, topicName);
                        String timeToWait = withDefault(request.getParameter("ttl"), "5000");
                        Long time = Long.parseLong(timeToWait);
                        Thread.sleep(time);
                        break;
                    default:
                        receiveMessage(out, cfName, topicName);
                        sendMessage(out, message, cfName, topicName);
                        Thread.sleep(5000);
                }
            } catch (Throwable e) {
               e.printStackTrace(out);
            }
            out.println("</pre>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    
    void sendMessage(PrintWriter out, String message, String cfName, String topicName) throws NamingException, JMSException {
        InitialContext ctx = new InitialContext();
        out.println("Looking for CF " + cfName);
        TopicConnectionFactory cf = (TopicConnectionFactory) ctx.lookup(cfName);
        out.println("Found Connection Factory " + cf.toString());
        TopicConnection tc = cf.createTopicConnection();
        tc.start();
        TopicSession session = tc.createTopicSession(true, Session.AUTO_ACKNOWLEDGE);
        Topic t = (Topic) ctx.lookup(topicName);
        out.println("Found topic: " + t.getTopicName());
        TopicPublisher publisher = session.createPublisher(t);
        TextMessage msg = session.createTextMessage();
        msg.setText(message);
        publisher.publish(msg, Message.DEFAULT_DELIVERY_MODE, 0, 5000);
        out.println("Sent message: " + message);
        System.out.println("Sent message " + message);
        tc.close();      
    }
    
    
    void receiveMessage(PrintWriter out, String cfName, String topicName) throws NamingException, JMSException, InterruptedException {
        InitialContext ctx = new InitialContext();
        out.println("Looking for Connection Factory " + cfName);
        TopicConnectionFactory cf = (TopicConnectionFactory) ctx.lookup(cfName);
        out.println("Found Connection Factory " + cf.toString());
        TopicConnection tc = cf.createTopicConnection();
        tc.start();
        TopicSession session = tc.createTopicSession(true, Session.AUTO_ACKNOWLEDGE);
        Topic t = (Topic) ctx.lookup(topicName);
        out.println("Found topic: " + t.getTopicName());
        TopicSubscriber receiver = session.createSubscriber(t);
        SampleListener listener = new SampleListener();
        listener.setPrintWriter(out);
        receiver.setMessageListener(listener);
        out.println("Subscriber is ready...");
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
