package com.library.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Koneksi {

    private static final String URL = "jdbc:mysql://localhost/library"; // Ganti dengan URL database Anda
    private static final String USER = "root"; // Ganti dengan username database Anda
    private static final String PASS = ""; // Ganti dengan password database Anda

    // Menjaga koneksi statis agar bisa digunakan sepanjang aplikasi
    private static Connection conn;

    // Memastikan hanya ada satu koneksi yang digunakan di seluruh aplikasi
    public static Connection getConnection() {
        if (conn == null || !isConnectionValid()) {
            try {
                // Memuat driver MySQL
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Membuka koneksi ke database
                conn = DriverManager.getConnection(URL, USER, PASS);
                System.out.println("Koneksi berhasil.");
            } catch (ClassNotFoundException e) {
                System.out.println("Driver tidak ditemukan: " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("Koneksi GAGAL: " + e.getMessage());
            }
        }
        return conn;
    }

    private static boolean isConnectionValid() {
        try {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    // Memastikan koneksi hanya ditutup ketika benar-benar tidak dibutuhkan lagi
    public static void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Koneksi ditutup.");
            }
        } catch (SQLException e) {
            System.out.println("Gagal menutup koneksi: " + e.getMessage());
        }
    }
}
