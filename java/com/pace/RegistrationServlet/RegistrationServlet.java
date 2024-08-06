package com.pace.RegistrationServlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uname = request.getParameter("name");
        String umail = request.getParameter("email");
        String upwd = request.getParameter("pass");
        String umobile = request.getParameter("contact");
        RequestDispatcher dispatcher = null;
        Connection con = null;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pace?useSSL=false", "root", "Escobar#123");
            
            // Check if email already exists
            PreparedStatement checkEmailStmt = con.prepareStatement("SELECT COUNT(*) FROM users WHERE umail = ?");
            checkEmailStmt.setString(1, umail);
            ResultSet rs = checkEmailStmt.executeQuery();
            rs.next();
            int emailCount = rs.getInt(1);
            
            if (emailCount > 0) {
                // Email already exists
                request.setAttribute("status", "email_exists");
                dispatcher = request.getRequestDispatcher("registration.jsp");
            } else {
                // Insert new user
                PreparedStatement pst = con.prepareStatement("INSERT INTO users (uname, umail, upwd, umobile) VALUES (?, ?, ?, ?)");
                pst.setString(1, uname);
                pst.setString(2, umail);
                pst.setString(3, upwd);
                pst.setString(4, umobile);

                int rowCount = pst.executeUpdate();
                if (rowCount > 0) {
                    request.setAttribute("status", "success");
                } else {
                    request.setAttribute("status", "failure");
                }
                dispatcher = request.getRequestDispatcher("registration.jsp");
            }
            dispatcher.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("status", "error");
            dispatcher = request.getRequestDispatcher("registration.jsp");
            dispatcher.forward(request, response);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
