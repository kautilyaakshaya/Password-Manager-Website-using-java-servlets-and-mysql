package com.pace.RegistrationServlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String umail = request.getParameter("username");
        String upwd = request.getParameter("password");
        HttpSession session = request.getSession();
        Connection con = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pace?useSSL=false&allowPublicKeyRetrieval=true", "root", "Escobar#123");
            PreparedStatement pst = con.prepareStatement("select * from users where umail = ? and upwd = ?");
            pst.setString(1, umail);
            pst.setString(2, upwd);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                // Set user session attribute
                session.setAttribute("id", rs.getString("id"));
                session.setAttribute("name", rs.getString("uname")); // corrected attribute name
                session.setAttribute("umail", umail);
                session.setAttribute("upwd", upwd);

                // Redirect to index.jsp
                response.sendRedirect("index.jsp");
            } else {
                // If login fails, set status attribute and redirect to login.jsp with status parameter
                response.sendRedirect("login.jsp?status=failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
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
