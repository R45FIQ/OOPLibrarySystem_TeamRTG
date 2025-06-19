package com.library.model;

import com.library.controller.AnggotaController;
import com.library.controller.BukuController;

public class Peminjaman {

    private Anggota anggota;
    private Buku buku;
    private String tanggalPinjam;

    // Constructor with Anggota, Buku, and tanggalPinjam
    public Peminjaman(Anggota anggota, Buku buku, String tanggalPinjam) {
        this.anggota = anggota;
        this.buku = buku;
        this.tanggalPinjam = tanggalPinjam;
    }

    // Constructor with AnggotaId, BukuId and tanggalPinjam
    public Peminjaman(int anggotaId, int bukuId, String tanggalPinjam) {
        // Retrieve Anggota and Buku objects using their IDs
        this.anggota = new AnggotaController().getAnggotaById(anggotaId);
        this.buku = new BukuController().getBukuById(bukuId);
        this.tanggalPinjam = tanggalPinjam;
    }

    // Getters and Setters
    public Anggota getAnggota() {
        return anggota;
    }

    public Buku getBuku() {
        return buku;
    }

    public String getTanggalPinjam() {
        return tanggalPinjam;
    }

    // Optional: Override toString() for debugging
    @Override
    public String toString() {
        return "Peminjaman{" +
                "anggota=" + anggota.getNama() +  // Assuming Anggota has a getNama() method
                ", buku=" + buku.getJudul() +     // Assuming Buku has a getJudul() method
                ", tanggalPinjam='" + tanggalPinjam + '\'' +
                '}';
    }
}
