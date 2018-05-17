/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.electriccloud;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.naming.*;  
import javax.jms.*;  

/**
 *
 * @author imago
 */
public class JMSServlet extends HttpServlet {
    
    private final static String CONNECTION_FACTORY = "SampleConnectionFactory";
    private final static String QUEUE = "SampleQueue";

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
            String message = request.getParameter("message");
            if (message == null || message.equals("")) {
                message = "Sample Message";
            }
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet JMSServlet</title>");  
            String connectionFactoryName = request.getParameter("connection_factory_name");
            String queueName = request.getParameter("queue_name");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet JMSServlet at " + request.getContextPath() + "</h1>");
            out.println("<pre>");
            String command = request.getParameter("command");
            if (command == null) {
                command = "send";
            }
            switch(command) {
                case "send": 
                    try {
                        sendMessage(out, message, connectionFactoryName, queueName);
                    } catch (Throwable e) {
                        e.printStackTrace(out);
                    }
                
                    break;
                case "receive": 
                    try {
                        receiveMessage(out, connectionFactoryName, queueName);
                    } catch (Throwable e) {
                        e.printStackTrace(out);
                    }
                    break;
                default:
                    out.println("Don't know command " + command);
            }
          
            out.println("</pre>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    
    void sendMessage(PrintWriter out, String message, String cfName, String queueName) throws NamingException, JMSException {
        InitialContext ctx = new InitialContext();
        if (cfName == null || cfName.equals("")) {
            cfName = CONNECTION_FACTORY;
        }
        if (queueName == null || queueName.equals("")) {
            queueName = QUEUE;
        }
        QueueConnectionFactory f = (QueueConnectionFactory) ctx.lookup(cfName);
        out.println("Found connection factory " + cfName);
        QueueConnection conn = f.createQueueConnection();
        out.println("Created connection...");
        conn.start();
        QueueSession session = conn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = (Queue) ctx.lookup(queueName);
        out.println("Found queue " + queueName);
        QueueSender sender = session.createSender(queue);
        TextMessage msg = session.createTextMessage();
        msg.setText(message);
        sender.send(msg);
        out.println("Sent message to " + queue.getQueueName());
        conn.close();
    }
    
    
    void receiveMessage(PrintWriter out, String cfName, String queueName) throws JMSException, NamingException, InterruptedException {
        if (cfName == null || cfName.equals("")) {
            cfName = CONNECTION_FACTORY;
        }
        if (queueName == null || queueName.equals("")) {
            queueName = QUEUE;
        }
        InitialContext ctx = new InitialContext();
        QueueConnectionFactory f = (QueueConnectionFactory) ctx.lookup(cfName);
        QueueConnection conn = f.createQueueConnection();
        conn.start();
        QueueSession session = conn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = (Queue) ctx.lookup(queueName);
        QueueReceiver receiver = session.createReceiver(queue);
        SampleListener listener = new SampleListener();
        listener.setPrintWriter(out);
        out.println("Waiting for messages...");
        receiver.setMessageListener(listener);
        Thread.sleep(5000);
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
