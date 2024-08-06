package com.pace.RegistrationServlet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.pace.RegistrationServlet.ShowPasswordsServlet.PasswordEntry;

public class passwordEntryDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/pace?useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "Escobar#123";

    public PasswordEntry getPasswordEntryById(String tableName, int id) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "SELECT * FROM " + tableName + " WHERE id = ?";
            try (PreparedStatement pst = con.prepareStatement(query)) {
                pst.setInt(1, id);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        PasswordEntry entry = new PasswordEntry();
                        entry.setId(rs.getInt("id"));
                        entry.setWebsiteName(rs.getString("website_name"));
                        entry.setEmail(rs.getString("email"));
                        entry.setPassword(rs.getString("password"));
                        entry.setMobileNumber(rs.getString("mobile_number"));
                        entry.setDescription(rs.getString("description"));
                        return entry;
                    } else {
                        return null;
                    }
                }
            }
        }
    }

    public void updatePasswordEntry(String tableName, com.pace.RegistrationServlet.PasswordEntry entry) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "UPDATE " + tableName + " SET website_name = ?, email = ?, password = ?, mobile_number = ?, description = ? WHERE id = ?";
            try (PreparedStatement pst = con.prepareStatement(query)) {
                pst.setString(1, entry.getWebsiteName());
                pst.setString(2, entry.getEmail());
                pst.setString(3, entry.getPassword());
                pst.setString(4, entry.getMobileNumber());
                pst.setString(5, entry.getDescription());
                pst.setInt(6, entry.getId());
                pst.executeUpdate();
            }
        }
    }
}
