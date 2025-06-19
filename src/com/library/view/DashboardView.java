package com.library.view;

import com.library.controller.BukuController;
import com.library.model.Buku;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Map;

public class DashboardView {

    private BukuController bukuController;
    private Button logoutButton;
    private Label dashboardLabel;
    private Label dataBookLabel;
    private Label borrowBookLabel;
    private Label memberBookLabel;
    private Label manageBookLabel;
    private Label manageUserLabel;
    private Label reportLabel;
    private Label katalogLabel;
    private String role;

    public DashboardView(BukuController bukuController) {
        this.bukuController = bukuController;
    }

    public void show(Stage stage, String username, String role) {
        this.role = role;

//        bukuController.addHardCodedBooks();

        BorderPane root = new BorderPane();
        root.setTop(createHeader(username));
        root.setLeft(createSidebar(role));
        root.setCenter(createContent(role));
        root.setBottom(createLogoutButton());

        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Dashboard - Perpustakaan");
        stage.setScene(scene);
        stage.show();
    }

    private VBox createHeader(String username) {
        VBox header = new VBox(0);
        header.setPadding(new Insets(0));

        HBox topHeader = new HBox(0);
        topHeader.setPadding(new Insets(10));
        topHeader.setAlignment(Pos.TOP_CENTER);
        topHeader.setStyle("-fx-background-color: #34495e; -fx-text-fill: white;");

        Label welcomeLabel = new Label("Selamat datang, " + username);
        welcomeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");
        topHeader.getChildren().add(welcomeLabel);

        HBox bottomHeader = new HBox(10);
        bottomHeader.setAlignment(Pos.CENTER);
        bottomHeader.setStyle("-fx-background-color: #34495e; -fx-text-fill: white;");

        dashboardLabel = new Label("Dashboard");
        dataBookLabel = new Label("Data Book");
        borrowBookLabel = new Label("Borrow Book");

        dataBookLabel.setOnMouseClicked(e -> handleDBook());
        borrowBookLabel.setOnMouseClicked(e -> handleBorrowBook());

        bottomHeader.getChildren().addAll(dashboardLabel, dataBookLabel, borrowBookLabel);

        styleLabel(dashboardLabel);
        styleLabel(dataBookLabel);
        styleLabel(borrowBookLabel);

        header.getChildren().addAll(topHeader, bottomHeader);
        return header;
    }

    private void styleLabel(Label label) {
        label.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px; -fx-font-size: 14px;");
        addHoverEffect(label);
    }

    private void addHoverEffect(Label label) {
        label.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px; -fx-font-size: 14px;");
        label.setOnMouseEntered(e -> label.setStyle("-fx-background-color: rgba(255,255,255,0.15); -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px; -fx-font-size: 14px; -fx-background-radius: 5px;"));
        label.setOnMouseExited(e -> label.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px; -fx-font-size: 14px;"));
    }

    private VBox createSidebar(String role) {
        VBox sidebar = new VBox(20);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: #34495e;-fx-font-weight: bold; -fx-text-fill: white;");

        Label manageBookLabel = new Label("Manage Book");
        Label manageUserLabel = new Label("Manage User");
        Label viewReportLabel = new Label("View Report");
        katalogLabel = new Label("");

//        addHoverEffectHalus(manageBookLabel);
//        addHoverEffectHalus(manageUserLabel);
//        addHoverEffectHalus(viewReportLabel);
//        addHoverEffectHalus(katalogLabel);

        katalogLabel.setOnMouseClicked(e -> handleDBook());

        if ("admin".equalsIgnoreCase(role)) {
            sidebar.getChildren().addAll(manageBookLabel, manageUserLabel, viewReportLabel);
        } else {
            sidebar.getChildren().addAll(katalogLabel);
        }

        addActionToSidebarLabels(manageBookLabel, manageUserLabel, viewReportLabel);
        return sidebar;
    }

    private void addActionToSidebarLabels(Label manageBookLabel, Label manageUserLabel, Label viewReportLabel) {
        manageBookLabel.setOnMouseClicked(e -> handleDataBook());
        manageUserLabel.setOnMouseClicked(e -> handleManageUser());
        viewReportLabel.setOnMouseClicked(e -> handleReportBook());
    }

    private VBox createContent(String role) {
        VBox content = new VBox(20);
        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(20));

        Label title = new Label("Dashboard " + ("admin".equalsIgnoreCase(role) ? "Admin" : "Anggota"));
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        HBox stats = createStats();
        VBox bookList = createBookList();

        content.getChildren().addAll(title, stats, new Separator(), bookList);
        return content;
    }

    private HBox createStats() {
        HBox stats = new HBox(20);
        stats.setAlignment(Pos.CENTER);
        stats.setPadding(new Insets(10));

        long totalBooks = bukuController.getTotalBooks();
        long returnedBooks = bukuController.getReturnedBooks();
        long borrowedBooks = bukuController.getBorrowedBooks();

        Label TotalBookLabel = new Label("Total Book\n" + totalBooks);
        Label ReturnBookLabel = new Label("Return Book\n" + returnedBooks);
        Label BorrowBookLabel = new Label("Borrow Book\n" + borrowedBooks);

        String labelStyle = "-fx-background-radius: 10px; -fx-padding: 15px; -fx-font-size: 16px; -fx-font-weight: bold;";

        TotalBookLabel.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;" + labelStyle);
        ReturnBookLabel.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white;" + labelStyle);
        BorrowBookLabel.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;" + labelStyle);

        stats.getChildren().addAll(TotalBookLabel, ReturnBookLabel, BorrowBookLabel);
        return stats;
    }

    private VBox createBookList() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(10));
        box.setAlignment(Pos.TOP_LEFT);

        Label label = new Label("📚 Daftar Buku:");
        label.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        VBox list = new VBox(5);
        Map<Integer, Buku> data = bukuController.getAllBuku();  // Mendapatkan Map dari Buku
        if (data.isEmpty()) {
            list.getChildren().add(new Label("Tidak ada data buku."));
        } else {
            for (Buku buku : data.values()) {  // Menggunakan values() untuk mendapatkan buku dari Map
                String info = "ID: " + buku.getId() +
                        " | Judul: " + buku.getJudul() +
                        " | Penulis: " + buku.getPenulis() +
                        " | Status: " + buku.getStatus() +
                        " | Stok: " + buku.getStok();
                list.getChildren().add(new Label(info));  // Menampilkan buku dalam label
            }
        }

        box.getChildren().addAll(label, list);
        return box;
    }

    private VBox createLogoutButton() {
        VBox logoutBox = new VBox();
        logoutBox.setAlignment(Pos.BOTTOM_RIGHT);
        logoutBox.setPadding(new Insets(20));

        logoutButton = new Button("Keluar");
        logoutButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        logoutButton.setOnAction(e -> handleLogout());

        logoutBox.getChildren().add(logoutButton);
        return logoutBox;
    }

    private void handleLogout() {
        System.out.println("Logout berhasil.");
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.close();
        new LoginView().show(new Stage());
    }

    private void handleDataBook() {
        System.out.println("Buka Data Book");
        new BukuView(bukuController).show(new Stage());
    }

    private void handleDBook() {
        System.out.println("Buka Katalog Buku");
        new KatalogView(bukuController).show(new Stage());
    }

    private void handleBorrowBook() {
        System.out.println("Buka Borrow Book");
        PeminjamanView peminjamanView = new PeminjamanView(bukuController); // Mengirim objek BukuController ke PeminjamanView
        peminjamanView.show(new Stage());  // Menampilkan PeminjamanView
    }

    private void handleMemberBook() {
        System.out.println("Buka Member Book");
    }

    private void handleManageUser() {
        System.out.println("Buka Laporan");
        new AnggotaView(true).show(new Stage());
    }

    private void handleReportBook() {
        System.out.println("Buka Laporan");
        ReportView reportView = new ReportView(bukuController); // Kirim controller
        reportView.show(new Stage()); // Tampilkan window laporan
    }

    private void handlePeminjaman() {
        System.out.println("Buka Peminjaman");
    }

    private void handleLaporan() {
        System.out.println("Buka Laporan");
    }
}
