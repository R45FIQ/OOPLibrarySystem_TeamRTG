package com.library.model;

public class Buku {

    private final int id;
    private final String isbn;
    private final String judul;
    private final String penulis;
    private Status status;
    private int stok;

    public Buku(int id, String isbn, String judul, String penulis, Status status, int stok) {
        this.id = id;
        this.isbn = isbn;
        this.judul = judul;
        this.penulis = penulis;
        this.status = status;
        this.stok = stok;
    }

    public int getId() {
        return id;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getJudul() {
        return judul;
    }

    public String getPenulis() {
        return penulis;
    }

    public Status getStatus() {
        return status;
    }

    public int getStok() {
        return stok;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    public void returnBook() {
        if (this.status == Status.DIPINJAM) {
            this.status = Status.TERSEDIA;
            this.stok++;
            System.out.println("Buku '" + judul + "' berhasil dikembalikan.");
        } else {
            System.out.println("Buku '" + judul + "' tidak sedang dipinjam.");
        }
    }

    public enum Status {
        TERSEDIA,
        DIPINJAM
    }

    @Override
    public String toString() {
        return "Buku{" +
                "id=" + id +
                ", judul='" + judul + '\'' +
                ", penulis='" + penulis + '\'' +
                ", status=" + status +
                ", stok=" + stok +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Buku buku = (Buku) obj;
        return id == buku.id && judul.equals(buku.judul);
    }

    @Override
    public int hashCode() {
        return 31 * Integer.hashCode(id) + judul.hashCode();
    }
}
