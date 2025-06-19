package com.library.controller;

import com.library.model.Anggota;
import com.library.database.Koneksi;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnggotaController {

    // Helper method to get a Connection to the database
    private Connection getConnection() throws SQLException {
        return Koneksi.getConnection();
    }

    // Method to map the ResultSet to an Anggota object
    private Anggota mapToAnggota(ResultSet rs) throws SQLException {
        return new Anggota(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("nama"),
                rs.getString("alamatEmail"),
                rs.getString("role")
        );
    }

    // Register new anggota in the database
    public boolean registerAnggota(String username, String password, String nama, String alamatEmail, String role) {
        String query = "INSERT INTO users (username, password, nama, alamatEmail, role) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);  // Ensure password is encrypted in a real application
            stmt.setString(3, nama);
            stmt.setString(4, alamatEmail);
            stmt.setString(5, role);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // If one row is affected, the registration is successful

        } catch (SQLException e) {
            System.err.println("Error registering anggota: " + e.getMessage());
            return false; // Registration failed
        }
    }

    // Verify login based on username and password
    public Anggota verifyLogin(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapToAnggota(rs); // Map the result to Anggota object
                }
            }

        } catch (SQLException e) {
            System.err.println("Error verifying login: " + e.getMessage());
        }
        return null; // If login fails
    }

    // Get anggota by username
    public Anggota getAnggotaByUsername(String username) {
        String query = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapToAnggota(rs); // Map the result to Anggota object
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching anggota by username: " + e.getMessage());
        }
        return null; // If anggota not found
    }

    // Get anggota by ID
    public Anggota getAnggotaById(int id) {
        String query = "SELECT * FROM users WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapToAnggota(rs); // Map the result to Anggota object
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching anggota by ID: " + e.getMessage());
        }
        return null; // If anggota not found
    }

    // Get all anggota from the database
    public List<Anggota> getAllAnggota() {
        List<Anggota> anggotaList = new ArrayList<>();
        String query = "SELECT * FROM users";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                anggotaList.add(mapToAnggota(rs)); // Map each row to Anggota object
            }

        } catch (SQLException e) {
            System.err.println("Error fetching all anggota: " + e.getMessage());
        }
        return anggotaList;
    }

    // Remove an anggota by username
    public boolean removeAnggota(String username) {
        String query = "DELETE FROM users WHERE username = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // If one row is affected, deletion is successful

        } catch (SQLException e) {
            System.err.println("Error deleting anggota: " + e.getMessage());
        }
        return false; // Deletion failed
    }
}
