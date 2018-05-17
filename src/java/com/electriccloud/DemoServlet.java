/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.electriccloud;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import javax.naming.*;
import javax.sql.*;


/**
 *
 * @author imago
 */
public class DemoServlet extends HttpServlet {

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
            out.println("<title>Servlet DemoServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            String dsName = request.getParameter("ds_name");
            out.println("<h1>Servlet DemoServlet at " + request.getContextPath() + "</h1>");
            out.println("<pre>");
            try {
                testDB(out, dsName);
            } catch (Throwable e) {
                e.printStackTrace(out);
            }
            out.println("</pre>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    
    void testDB(PrintWriter out, String dsName) throws NamingException, SQLException {
        Context ctx = new InitialContext();
       
        if (dsName == null || dsName.equals("")) {
            dsName = "sampleDataSource";
        }
        out.println("Looking for DS named " + dsName);
        DataSource ds = (DataSource) ctx.lookup(dsName);
        out.println("DS found");
       
        Connection connection = ds.getConnection();
        connection.setAutoCommit(true);
        out.println("Connected to db...");
        
        Statement stmt = connection.createStatement();
        String table = "TEST_DB.TEST_TABLE";
        try {
            stmt.execute("SELECT * FROM " + table);
            ResultSet rs = stmt.getResultSet();
            out.println("Querying " + table);
            while(rs.next()) {
                out.println("ID: " + rs.getString("ID"));
            }
        } catch (SQLException e) {
            out.println(e.getMessage());
        }
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
