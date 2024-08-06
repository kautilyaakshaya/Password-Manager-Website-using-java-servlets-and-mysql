package com.pace.RegistrationServlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/showPasswordServlet")
public class ShowPasswordsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session != null) {
            String userId = (String) session.getAttribute("id");
            String usermail = (String) session.getAttribute("umail");
            String username = (String) session.getAttribute("name");
            String userpassword = (String) session.getAttribute("upwd");

            if (userId != null && userpassword != null && username != null) {
                request.setAttribute("userId", userId);
                request.setAttribute("userMail", usermail);
                request.setAttribute("userName", username);
                String tableName = username + userpassword + userId;
                request.setAttribute("tableName", tableName); // Set tableName as a request attribute

                Connection con = null;
                PreparedStatement pst = null;
                ResultSet rs = null;

                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pace?useSSL=false", "root", "Escobar#123");

                    String query = "SELECT * FROM " + tableName;
                    pst = con.prepareStatement(query);
                    rs = pst.executeQuery();

                    List<PasswordEntry> passwordList = new ArrayList<>();
                    while (rs.next()) {
                        PasswordEntry entry = new PasswordEntry();
                        entry.setId(rs.getInt("id"));
                        entry.setWebsiteName(rs.getString("website_name"));
                        entry.setEmail(rs.getString("email"));
                        entry.setPassword(rs.getString("password"));
                        entry.setMobileNumber(rs.getString("mobile_number"));
                        entry.setDescription(rs.getString("description"));
                        passwordList.add(entry);
                    }

                    request.setAttribute("passwordList", passwordList);
                    request.getRequestDispatcher("ShowPwd.jsp").forward(request, response);

                } catch (ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                    request.setAttribute("error", "An error occurred. Please try again later.");
                    request.getRequestDispatcher("error.jsp").forward(request, response);
                } finally {
                    try {
                        if (rs != null) rs.close();
                        if (pst != null) pst.close();
                        if (con != null) con.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                response.sendRedirect("login.jsp");
            }
        } else {
            response.sendRedirect("login.jsp");
        }
    }

    public static class PasswordEntry {
        private int id;
        private String websiteName;
        private String email;
        private String password;
        private String mobileNumber;
        private String description;

        // Getters and Setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getWebsiteName() { return websiteName; }
        public void setWebsiteName(String websiteName) { this.websiteName = websiteName; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getMobileNumber() { return mobileNumber; }
        public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
}
