package com.library.view;

import com.library.controller.BukuController;
import com.library.model.Buku;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class BukuView {

    private BukuController bukuController;
    private TextField isbnField;
    private TextField judulField;
    private TextField penulisField;
    private TextField stokField;
    private ComboBox<String> statusComboBox;

    public BukuView(BukuController bukuController) {
        this.bukuController = bukuController;
    }

    public void show(Stage stage) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #ecf0f1, #bdc3c7);");

        Label title = new Label("📘 Manajemen Buku");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        isbnField = new TextField();
        isbnField.setPromptText("ISBN Buku");
        isbnField.setStyle("-fx-background-radius: 8; -fx-padding: 8px;");

        judulField = new TextField();
        judulField.setPromptText("Judul Buku");
        judulField.setStyle("-fx-background-radius: 8; -fx-padding: 8px;");

        penulisField = new TextField();
        penulisField.setPromptText("Penulis Buku");
        penulisField.setStyle("-fx-background-radius: 8; -fx-padding: 8px;");

        stokField = new TextField();
        stokField.setPromptText("Jumlah Stok");
        stokField.setStyle("-fx-background-radius: 8; -fx-padding: 8px;");

        statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll("TERSEDIA", "DIPINJAM");
        statusComboBox.setValue("TERSEDIA");
        statusComboBox.setStyle("-fx-background-radius: 8; -fx-padding: 8px;");

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button tambahBtn = new Button("➕ Tambah Buku");
        tambahBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8;");
        tambahBtn.setOnAction(e -> handleTambah());

        Button editBtn = new Button("✏️ Edit Buku");
        editBtn.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8;");
        editBtn.setOnAction(e -> handleEdit());

        Button hapusBtn = new Button("🗑️ Hapus Buku");
        hapusBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8;");
        hapusBtn.setOnAction(e -> handleHapus());

        Button returnBtn = new Button("🔄 Kembalikan Buku");
        returnBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8;");
        returnBtn.setOnAction(e -> handleReturnBook());

        buttonBox.getChildren().addAll(tambahBtn, editBtn, hapusBtn, returnBtn);

        root.getChildren().addAll(title, isbnField, judulField, penulisField, stokField, statusComboBox, buttonBox);

        Scene scene = new Scene(root, 550, 450);
        stage.setTitle("Manajemen Buku - Perpustakaan");
        stage.setScene(scene);
        stage.show();
    }

    private void handleReturnBook() {
        String judul = judulField.getText();
        Buku buku = bukuController.getBukuByJudul(judul);
        if (buku != null && buku.getStatus() == Buku.Status.DIPINJAM) {
            buku.returnBook();
            bukuController.updateBukuStatus(buku.getId(), Buku.Status.TERSEDIA);
            showAlert(Alert.AlertType.INFORMATION, "Buku berhasil dikembalikan.");
            clearForm();
        } else {
            showAlert(Alert.AlertType.ERROR, "Buku tidak ditemukan atau belum dipinjam.");
        }
    }

    private void handleTambah() {
        String isbn = isbnField.getText();
        String judul = judulField.getText();
        String penulis = penulisField.getText();
        String stokStr = stokField.getText();
        String status = statusComboBox.getValue();

        if (judul.isEmpty() || penulis.isEmpty() || stokStr.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Semua field harus diisi.");
            return;
        }

        try {
            int stok = Integer.parseInt(stokStr);
            Buku buku = new Buku(0, isbn, judul, penulis, Buku.Status.valueOf(status), stok);
            if (bukuController.addBuku(buku)) {
                showAlert(Alert.AlertType.INFORMATION, "Buku berhasil ditambahkan.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Gagal menambahkan buku.");
            }
            clearForm();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Stok harus berupa angka.");
        }
    }

    private void handleEdit() {
        String judul = judulField.getText();
        String status = statusComboBox.getValue();

        Buku buku = bukuController.getBukuByJudul(judul);
        if (buku != null) {
            buku.setStatus(Buku.Status.valueOf(status));
            if (bukuController.updateBukuStatus(buku.getId(), Buku.Status.valueOf(status))) {
                showAlert(Alert.AlertType.INFORMATION, "Buku berhasil diperbarui.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Gagal memperbarui buku.");
            }
            clearForm();
        } else {
            showAlert(Alert.AlertType.ERROR, "Buku tidak ditemukan.");
        }
    }

    private void handleHapus() {
        String judul = judulField.getText();

        Buku buku = bukuController.getBukuByJudul(judul);
        if (buku != null) {
            if (bukuController.deleteBuku(buku.getId())) {
                showAlert(Alert.AlertType.INFORMATION, "Buku berhasil dihapus.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Gagal menghapus buku.");
            }
            clearForm();
        } else {
            showAlert(Alert.AlertType.ERROR, "Buku tidak ditemukan.");
        }
    }

    private void clearForm() {
        judulField.clear();
        penulisField.clear();
        stokField.clear();
        statusComboBox.setValue("TERSEDIA");
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message);
        alert.showAndWait();
    }
}
