package com.library.view;

import com.library.controller.PeminjamanController;
import com.library.controller.BukuController;
import com.library.model.Peminjaman;
import com.library.model.Buku;
import com.library.model.Anggota;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class PeminjamanView {

    private PeminjamanController peminjamanController;
    private BukuController bukuController;
    private TextField anggotaIdField, bukuIdField;
    private DatePicker tanggalPinjamPicker;

    public PeminjamanView(BukuController bukuController) {
        this.bukuController = bukuController;
        // Create the necessary maps for PeminjamanController
        Map<Integer, Buku> bukuDatabase = new HashMap<>();
        Map<Integer, Anggota> anggotaDatabase = new HashMap<>();

        // Initialize PeminjamanController with the maps
        peminjamanController = new PeminjamanController(bukuDatabase, anggotaDatabase);
    }

    public void show(Stage stage) {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        // Membuat elemen input
        anggotaIdField = createAnggotaIdField();
        bukuIdField = createBukuIdField();
        tanggalPinjamPicker = createTanggalPinjamPicker();
        Button borrowButton = createBorrowButton();
        Button returnButton = createReturnButton();

        // Menambahkan elemen ke root
        root.getChildren().addAll(anggotaIdField, bukuIdField, tanggalPinjamPicker, borrowButton, returnButton);

        // Tambahkan pemanggilan untuk menampilkan total buku
        showTotalBooks();

        Scene scene = new Scene(root, 400, 300);
        stage.setTitle("Peminjaman Buku - Perpustakaan");
        stage.setScene(scene);
        stage.show();
    }

    private TextField createAnggotaIdField() {
        TextField textField = new TextField();
        textField.setPromptText("ID Anggota");
        return textField;
    }

    private TextField createBukuIdField() {
        TextField textField = new TextField();
        textField.setPromptText("ID Buku");
        return textField;
    }

    private DatePicker createTanggalPinjamPicker() {
        return new DatePicker();
    }

    private Button createBorrowButton() {
        Button button = new Button("Pinjam Buku");
        button.setOnAction(e -> handleBorrowBook());
        return button;
    }

    private Button createReturnButton() {
        Button button = new Button("Kembalikan Buku");
        button.setOnAction(e -> handleReturnBook());
        return button;
    }

    private void handleBorrowBook() {
        try {
            int anggotaId = Integer.parseInt(anggotaIdField.getText());
            int bukuId = Integer.parseInt(bukuIdField.getText());
            String tanggalPinjam = tanggalPinjamPicker.getValue().toString();

            // Create Peminjaman object using Anggota and Buku IDs
            Peminjaman peminjaman = new Peminjaman(anggotaId, bukuId, tanggalPinjam);
            peminjamanController.pinjamBuku(anggotaId, bukuId);  // Peminjaman buku

            new Alert(Alert.AlertType.INFORMATION, "Buku berhasil dipinjam!").showAndWait();
            resetFields();
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "ID Anggota dan ID Buku harus berupa angka!").showAndWait();
        }
    }

    private void handleReturnBook() {
        try {
            int anggotaId = Integer.parseInt(anggotaIdField.getText());
            int bukuId = Integer.parseInt(bukuIdField.getText());

            boolean success = peminjamanController.returnBook(anggotaId, bukuId);
            if (success) {
                new Alert(Alert.AlertType.INFORMATION, "Buku berhasil dikembalikan!").showAndWait();
                resetFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "Buku tidak ditemukan!").showAndWait();
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "ID Anggota dan ID Buku harus berupa angka!").showAndWait();
        }
    }

    private void resetFields() {
        anggotaIdField.clear();
        bukuIdField.clear();
        tanggalPinjamPicker.setValue(null);
    }

    // Menampilkan total buku yang ada
    private void showTotalBooks() {
        long totalBooks = bukuController.getTotalBooks(); // Mendapatkan total buku
        System.out.println("Total Buku: " + totalBooks);  // Menampilkan total buku
    }
}
