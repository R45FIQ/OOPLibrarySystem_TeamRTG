package com.library.controller;

import com.library.model.Anggota;
import com.library.database.Koneksi;

import java.sql.*;

public class AuthController {

    // Verifikasi login berdasarkan username dan password
    public boolean validateLogin(String username, String password, String role) {
        // Normalisasi username dan role agar tidak terpengaruh kapitalisasi
        String normalizedUsername = normalize(username);
        String normalizedRole = normalize(role);

        // Query untuk mencari pengguna berdasarkan username dan password
        String query = "SELECT * FROM users WHERE username = ? AND password = ? AND role = ?";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set parameter pada query
            stmt.setString(1, normalizedUsername);
            stmt.setString(2, password);  // Perhatikan keamanan, password harus dienkripsi dalam aplikasi nyata
            stmt.setString(3, normalizedRole);

            // Eksekusi query
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return true; // Jika ada data yang cocok, login berhasil
                } else {
                    System.out.println("Username, password, atau role salah.");
                    return false; // Jika tidak ada data yang cocok
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Jika terjadi error pada query
    }

    // Helper method to normalize strings to avoid case sensitivity issues
    private String normalize(String input) {
        return input != null ? input.trim().toLowerCase() : "";
    }

    // Menambahkan pengguna baru (misalnya saat pendaftaran anggota)
    public boolean addUser(Anggota anggota) {
        String query = "INSERT INTO users (username, password, nama, alamatEmail, role) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, anggota.getUsername());
            stmt.setString(2, anggota.getPassword());  // Pastikan untuk mengenkripsi password di aplikasi nyata
            stmt.setString(3, anggota.getNama());
            stmt.setString(4, anggota.getAlamatEmail());
            stmt.setString(5, anggota.getRole());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Jika berhasil menambah anggota

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Kembalikan false jika terjadi error
        }
    }

    // Menghapus pengguna berdasarkan username
    public boolean removeUser(String username) {
        String query = "DELETE FROM users WHERE username = ?";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Jika berhasil menghapus anggota

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Mendapatkan pengguna berdasarkan username
    public Anggota getUser(String username) {
        String query = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapToAnggota(rs); // Map the result to Anggota object
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Jika pengguna tidak ditemukan
    }

    // Pemetaan ResultSet ke objek Anggota
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
}
