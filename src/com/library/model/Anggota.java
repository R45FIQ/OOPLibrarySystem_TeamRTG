package com.library.model;

public class Anggota {

    private int id;
    private String username;
    private String password;
    private String nama;
    private String alamatEmail;
    private String role;

    // Konstruktor dengan parameter id
    public Anggota(int id, String username, String password, String nama, String alamatEmail, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nama = nama;
        this.alamatEmail = alamatEmail;
        this.role = role;
    }

    // Getter dan Setter untuk id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter dan Setter untuk username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getter dan Setter untuk password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getter dan Setter untuk nama
    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    // Getter dan Setter untuk alamatEmail
    public String getAlamatEmail() {
        return alamatEmail;
    }

    public void setAlamatEmail(String alamatEmail) {
        this.alamatEmail = alamatEmail;
    }

    // Getter dan Setter untuk role
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Override toString untuk debugging atau log
    @Override
    public String toString() {
        return "Anggota{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", nama='" + nama + '\'' +
                ", alamatEmail='" + alamatEmail + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
