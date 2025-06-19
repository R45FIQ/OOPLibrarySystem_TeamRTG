package com.library.view;

import com.library.controller.BukuController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ReportView {

    private BukuController bukuController;

    public ReportView(BukuController bukuController) {
        this.bukuController = bukuController;
    }

    public void show(Stage stage) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #ecf0f1, #bdc3c7);");

        Label titleLabel = new Label("📊 Laporan Peminjaman Buku");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        long tersedia = bukuController.getReturnedBooks();
        long dipinjam = bukuController.getBorrowedBooks();
        long total = tersedia + dipinjam;

        double persenTersedia = total > 0 ? (tersedia * 100.0 / total) : 0;
        double persenDipinjam = total > 0 ? (dipinjam * 100.0 / total) : 0;

        PieChart chart = new PieChart();
        chart.getData().add(new PieChart.Data("Tersedia: " + tersedia + " buku (" + String.format("%.1f", persenTersedia) + "%)", tersedia));
        chart.getData().add(new PieChart.Data("Dipinjam: " + dipinjam + " buku (" + String.format("%.1f", persenDipinjam) + "%)", dipinjam));
        chart.setTitle("Status Buku");

        Button viewReportBtn = new Button("📄 Lihat Laporan Detail");
        viewReportBtn.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8;");
        viewReportBtn.setOnAction(e -> showDetailReport());

        root.getChildren().addAll(titleLabel, chart, viewReportBtn);

        Scene scene = new Scene(root, 500, 500);
        stage.setTitle("Laporan - Perpustakaan");
        stage.setScene(scene);
        stage.show();
    }

    private void showDetailReport() {
        Stage detailStage = new Stage();
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_LEFT);
        root.setStyle("-fx-background-color: #ffffff;");

        Label label = new Label("📚 Detail Laporan Buku:");
        label.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        VBox list = new VBox(5);
        for (var entry : bukuController.getAllBuku().values()) {
            String info = "ID: " + entry.getId() +
                    " | Judul: " + entry.getJudul() +
                    " | Penulis: " + entry.getPenulis() +
                    " | Status: " + entry.getStatus() +
                    " | Stok: " + entry.getStok();
            list.getChildren().add(new Label(info));
        }

        ScrollPane scrollPane = new ScrollPane(list);
        scrollPane.setFitToWidth(true);

        root.getChildren().addAll(label, scrollPane);

        Scene scene = new Scene(root, 500, 400);
        detailStage.setTitle("Detail Laporan Buku");
        detailStage.setScene(scene);
        detailStage.show();
    }
}
