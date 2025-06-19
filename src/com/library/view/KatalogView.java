package com.library.view;

import com.library.controller.BukuController;
import com.library.model.Buku;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class KatalogView {

    private BukuController bukuController;

    public KatalogView(BukuController bukuController) {
        this.bukuController = bukuController;
    }

    public void show(Stage stage) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: #ecf0f1;");

        Label title = new Label("Katalog Buku");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        FlowPane katalogPane = new FlowPane();
        katalogPane.setHgap(20);
        katalogPane.setVgap(20);
        katalogPane.setAlignment(Pos.CENTER);
        katalogPane.setPadding(new Insets(20));

        for (Buku buku : bukuController.getAllBuku().values()) {
            VBox card = createBookCard(buku);
            katalogPane.getChildren().add(card);
        }

        root.getChildren().addAll(title, katalogPane);

        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Katalog Buku - Perpustakaan");
        stage.setScene(scene);
        stage.show();
    }

    private VBox createBookCard(Buku buku) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(10));
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; -fx-border-radius: 10; -fx-background-radius: 10;");

        ImageView cover = new ImageView();
        try {
            String filename = "/com/library/image/cover_" + buku.getId() + ".png";
            Image img = new Image(getClass().getResourceAsStream(filename));
            cover.setImage(img);
        } catch (Exception e) {
            cover.setImage(new Image(getClass().getResourceAsStream("/com/library/image/default.png")));
        }
        cover.setFitWidth(100);
        cover.setFitHeight(150);
        cover.setPreserveRatio(true);

        Label judulLabel = new Label(buku.getJudul());
        judulLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Button detailBtn = new Button("Selengkapnya");
        detailBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");
        detailBtn.setOnAction(e -> showBookDetail(buku));

        card.getChildren().addAll(cover, judulLabel, detailBtn);
        return card;
    }

    private void showBookDetail(Buku buku) {
        Stage detailStage = new Stage();
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER_LEFT);

        Label judul = new Label("Judul: " + buku.getJudul());
        Label penulis = new Label("Penulis: " + buku.getPenulis());
        Label status = new Label("Status: " + buku.getStatus());
        Label stok = new Label("Stok: " + buku.getStok());

        Button pinjamBtn = new Button("📚 Pinjam Buku");
        pinjamBtn.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-font-weight: bold;");
        pinjamBtn.setOnAction(e -> {
            if (buku.getStok() > 0) {
                buku.setStok(buku.getStok() - 1);
                if (buku.getStok() == 0) {
                    buku.setStatus(Buku.Status.DIPINJAM);
                }
                new Alert(Alert.AlertType.INFORMATION, "Buku berhasil dipinjam!").showAndWait();
                detailStage.close();
            } else {
                buku.setStatus(Buku.Status.DIPINJAM);
                new Alert(Alert.AlertType.WARNING, "Stok habis, buku tidak dapat dipinjam.").showAndWait();
            }
        });

        layout.getChildren().addAll(judul, penulis, status, stok, pinjamBtn);

        Scene scene = new Scene(layout, 300, 250);
        detailStage.setTitle("Detail Buku");
        detailStage.setScene(scene);
        detailStage.show();
    }
}
