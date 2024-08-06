package com.pace.RegistrationServlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/deletePasswordServlet")
public class deletePasswordServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String tableName = request.getParameter("tableName");

        request.setAttribute("id", id);
        request.setAttribute("tableName", tableName);
        request.getRequestDispatcher("deletePassword.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String tableName = request.getParameter("tableName");

        if (id == null || tableName == null || id.isEmpty() || tableName.isEmpty()) {
            request.setAttribute("error", "Invalid input parameters.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        Connection con = null;
        PreparedStatement pst = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pace?useSSL=false", "root", "Escobar#123");

            String query = "DELETE FROM " + tableName + " WHERE id = ?";
            pst = con.prepareStatement(query);
            pst.setInt(1, Integer.parseInt(id));
            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                response.sendRedirect("showPasswordServlet"); // Redirect to show passwords servlet
            } else {
                request.setAttribute("error", "Failed to delete the password entry.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred. Please try again later.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } finally {
            try {
                if (pst != null) pst.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
