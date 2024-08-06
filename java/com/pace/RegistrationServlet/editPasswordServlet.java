package com.pace.RegistrationServlet;
import com.pace.RegistrationServlet.PasswordEntry;
import com.pace.RegistrationServlet.passwordEntryDAO;


import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/editPasswordServlet")
public class editPasswordServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String websiteName = request.getParameter("websiteName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String mobileNumber = request.getParameter("mobileNumber");
        String description = request.getParameter("description");
        String tableName = request.getParameter("tableName");

        PasswordEntry entry = new PasswordEntry();
        entry.setId(Integer.parseInt(id));
        entry.setWebsiteName(websiteName);
        entry.setEmail(email);
        entry.setPassword(password);
        entry.setMobileNumber(mobileNumber);
        entry.setDescription(description);

        passwordEntryDAO dao = new passwordEntryDAO();

        try {
            dao.updatePasswordEntry(tableName, entry);
            response.sendRedirect("showPasswordServlet");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred. Please try again later.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}