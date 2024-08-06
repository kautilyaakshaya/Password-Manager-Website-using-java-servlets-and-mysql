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

@WebServlet("/addPasswordServlet")
public class AddPasswordServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session != null) {
            // Get user details from the session
            String userId = (String) session.getAttribute("id");
            String Usermail = (String) session.getAttribute("umail");
            String username = (String) session.getAttribute("uname");
            String userpassword = (String) session.getAttribute("upwd");

            if (userId != null && userpassword != null && username != null) {
                request.setAttribute("userId", userId);
                request.setAttribute("userMail", userpassword);
                request.setAttribute("userName", username);
            } else {
                response.sendRedirect("login.jsp");
                return;
            }
        } else {
            response.sendRedirect("login.jsp");
            return;
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session != null) {
            // Get user details from the session
            String userId = (String) session.getAttribute("id");
            String Usermail = (String) session.getAttribute("umail");
            String username = (String) session.getAttribute("name");
            String userpassword = (String) session.getAttribute("upwd");

            if (userId != null && userpassword != null && username != null) {
                String tableName = username+userpassword+userId ;

                String websiteName = request.getParameter("websiteName");
                String email = request.getParameter("email");
                String password = request.getParameter("password");
                String mobileNumber = request.getParameter("mobile");
                String description = request.getParameter("description");

                Connection con = null;
                PreparedStatement pst = null;

                try {
                    // Load MySQL JDBC driver
                    Class.forName("com.mysql.cj.jdbc.Driver");

                    // Establish database connection
                    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pace?useSSL=false", "root", "Escobar#123");

                    // Check if table exists for the user
                    if (!tableExists(con, tableName)) {
                        // Create table if it doesn't exist
                        String createTableQuery = "CREATE TABLE " + tableName + " ("
                                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                                + "website_name VARCHAR(255),"
                                + "email VARCHAR(255),"
                                + "password VARCHAR(255),"
                                + "mobile_number VARCHAR(20),"
                                + "description TEXT"
                                + ")";
                        pst = con.prepareStatement(createTableQuery);
                        pst.executeUpdate();
                    }

                    // Insert password details into the user's table
                    String insertQuery = "INSERT INTO " + tableName + " (website_name, email, password, mobile_number, description) VALUES (?, ?, ?, ?, ?)";
                    pst = con.prepareStatement(insertQuery);
                    pst.setString(1, websiteName);
                    pst.setString(2, email);
                    pst.setString(3, password);
                    pst.setString(4, mobileNumber);
                    pst.setString(5, description);

                    int rowsInserted = pst.executeUpdate();

                    if (rowsInserted > 0) {
                        // Password inserted successfully
                        session.setAttribute("status", "success");
                        session.setAttribute("alert", "Password added successfully!");
                    } else {
                        // Handle insertion failure
                        session.setAttribute("status", "failed");
                        session.setAttribute("alert", "Failed to add password. Please try again.");
                    }

                    // Redirect to index.jsp
                    response.sendRedirect("index.jsp");
                } catch (ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                    // Handle errors - redirect to an error page or display an error message
                    session.setAttribute("status", "error");
                    session.setAttribute("alert", "An error occurred. Please try again later.");
                    response.sendRedirect("error.jsp");
                } finally {
                    // Close prepared statement and database connection
                    try {
                        if (pst != null) {
                            pst.close();
                        }
                        if (con != null) {
                            con.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                // If user details are not in session, redirect to login page
                response.sendRedirect("login.jsp");
            }
        } else {
            // If session is null, redirect to login page
            response.sendRedirect("login.jsp");
        }
    }

    // Method to check if a table exists in the database
    private boolean tableExists(Connection con, String tableName) throws SQLException {
        boolean tableExists = false;
        ResultSet tables = con.getMetaData().getTables(null, null, tableName, null);
        if (tables.next()) {
            tableExists = true;
        }
        return tableExists;
    }
}
