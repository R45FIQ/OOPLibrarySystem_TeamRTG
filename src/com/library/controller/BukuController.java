package com.library.controller;

import com.library.model.Buku;
import com.library.database.Koneksi;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class BukuController {

    // Menambahkan buku baru ke database
    public boolean addBuku(Buku buku) {
        String query = "INSERT INTO buku (isbn, judul, penulis, status, stok) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, buku.getIsbn());
            stmt.setString(2, buku.getJudul());
            stmt.setString(3, buku.getPenulis());
            stmt.setString(4, buku.getStatus().name());  // Menggunakan enum Buku.Status
            stmt.setInt(5, buku.getStok());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Gagal menambahkan buku: " + e.getMessage());
        }
        return false;  // Jika gagal menambahkan buku
    }

    // Mengambil buku berdasarkan ID
    public Buku getBukuById(int id) {
        String query = "SELECT * FROM buku WHERE id = ?";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapToBuku(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil buku dengan ID " + id + ": " + e.getMessage());
        }
        return null;
    }

    // Mendapatkan buku berdasarkan judul
    public Buku getBukuByJudul(String judul) {
        String query = "SELECT * FROM buku WHERE judul = ?";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, judul);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapToBuku(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil buku dengan judul " + judul + ": " + e.getMessage());
        }
        return null;
    }

    // Mendapatkan semua buku
    public Map<Integer, Buku> getAllBuku() {
        Map<Integer, Buku> bukuMap = new HashMap<>();
        String query = "SELECT * FROM buku";

        try (Connection conn = Koneksi.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Buku buku = mapToBuku(rs);
                bukuMap.put(buku.getId(), buku);
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil daftar buku: " + e.getMessage());
        }
        return bukuMap;
    }

    // Memetakan ResultSet ke objek Buku
    private Buku mapToBuku(ResultSet rs) throws SQLException {
        return new Buku(
                rs.getInt("id"),
                rs.getString("isbn"),
                rs.getString("judul"),
                rs.getString("penulis"),
                Buku.Status.valueOf(rs.getString("status")),
                rs.getInt("stok")
        );
    }

    // Mengupdate status buku
    public boolean updateBukuStatus(int id, Buku.Status status) {
        String query = "UPDATE buku SET status = ? WHERE id = ?";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, status.name());
            stmt.setInt(2, id);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Gagal memperbarui status buku dengan ID " + id + ": " + e.getMessage());
        }
        return false;
    }

    // Menghapus buku berdasarkan ID
    public boolean deleteBuku(int id) {
        String query = "DELETE FROM buku WHERE id = ?";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Gagal menghapus buku dengan ID " + id + ": " + e.getMessage());
        }
        return false;
    }

    // Menghitung jumlah total buku
    public long getTotalBooks() {
        String query = "SELECT COUNT(*) FROM buku";

        try (Connection conn = Koneksi.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            System.err.println("Gagal menghitung total buku: " + e.getMessage());
        }
        return 0;
    }

    // Menghitung jumlah buku yang tersedia
    public long getReturnedBooks() {
        String query = "SELECT COUNT(*) FROM buku WHERE status = 'TERSEDIA'";

        try (Connection conn = Koneksi.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            System.err.println("Gagal menghitung buku yang tersedia: " + e.getMessage());
        }
        return 0;
    }

    // Menghitung jumlah buku yang dipinjam
    public long getBorrowedBooks() {
        String query = "SELECT COUNT(*) FROM buku WHERE status = 'DIPINJAM'";

        try (Connection conn = Koneksi.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            System.err.println("Gagal menghitung buku yang dipinjam: " + e.getMessage());
        }
        return 0;
    }
}
