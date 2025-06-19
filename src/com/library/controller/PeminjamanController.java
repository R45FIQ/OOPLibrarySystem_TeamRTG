package com.library.controller;

import com.library.model.Anggota;
import com.library.model.Buku;
import com.library.model.Peminjaman;
import com.library.model.Buku.Status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PeminjamanController {

    private Map<Integer, Buku> bukuDatabase; // Database buku
    private Map<Integer, Anggota> anggotaDatabase; // Database anggota
    private List<Peminjaman> riwayatPeminjaman = new ArrayList<>(); // Riwayat peminjaman buku

    public PeminjamanController(Map<Integer, Buku> bukuDatabase, Map<Integer, Anggota> anggotaDatabase) {
        this.bukuDatabase = bukuDatabase;
        this.anggotaDatabase = anggotaDatabase;
    }

    // Menampilkan daftar buku yang tersedia untuk dipinjam
    public List<Buku> getAvailableBooks() {
        List<Buku> availableBooks = new ArrayList<>();
        for (Buku buku : bukuDatabase.values()) {
            if (buku.getStatus() == Status.TERSEDIA) {
                availableBooks.add(buku);
            }
        }
        return availableBooks;
    }

    // Peminjaman buku oleh anggota
    public String pinjamBuku(int anggotaId, int bukuId) {
        Anggota anggota = anggotaDatabase.get(anggotaId);
        Buku buku = bukuDatabase.get(bukuId);

        if (anggota == null) {
            return "Anggota tidak ditemukan.";
        }

        if (buku == null) {
            return "Buku tidak ditemukan.";
        }

        if (buku.getStatus() == Status.DIPINJAM) {
            return "Buku sudah dipinjam.";
        }

        Peminjaman peminjaman = new Peminjaman(anggota, buku, "2025-06-18"); // Menambahkan tanggal pinjam
        riwayatPeminjaman.add(peminjaman);

        buku.setStatus(Status.DIPINJAM); // Update status buku menjadi dipinjam
        return "Peminjaman berhasil!";
    }

    // Mengembalikan buku oleh anggota
    public boolean returnBook(int anggotaId, int bukuId) {
        Anggota anggota = anggotaDatabase.get(anggotaId);
        Buku buku = bukuDatabase.get(bukuId);

        if (anggota == null) {
            return false;  // Anggota tidak ditemukan
        }

        if (buku == null) {
            return false;  // Buku tidak ditemukan
        }

        if (buku.getStatus() == Status.TERSEDIA) {
            return false;  // Buku belum dipinjam
        }

        // Proses pengembalian buku
        riwayatPeminjaman.removeIf(peminjaman -> peminjaman.getAnggota().getId() == anggotaId && peminjaman.getBuku().getId() == bukuId);
        buku.setStatus(Status.TERSEDIA); // Update status buku menjadi tersedia
        return true;  // Pengembalian buku sukses
    }

    // Menampilkan riwayat peminjaman untuk anggota tertentu
    public List<Peminjaman> getRiwayatPeminjaman(int anggotaId) {
        List<Peminjaman> riwayat = new ArrayList<>();
        for (Peminjaman peminjaman : riwayatPeminjaman) {
            if (peminjaman.getAnggota().getId() == anggotaId) {
                riwayat.add(peminjaman);  // Menambahkan peminjaman ke riwayat jika cocok dengan anggota
            }
        }
        return riwayat;
    }

    // Cek status buku
    public String cekStatusBuku(int bukuId) {
        Buku buku = bukuDatabase.get(bukuId);
        if (buku == null) {
            return "Buku tidak ditemukan.";
        }
        return "Status Buku: " + buku.getStatus();  // Mengembalikan status buku
    }
}
