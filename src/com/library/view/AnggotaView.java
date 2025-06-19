package com.library.view;

import com.library.controller.AnggotaController;
import com.library.model.Anggota;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class AnggotaView {

    private AnggotaController anggotaController;
    private TextField usernameField, namaField, alamatField;
    private PasswordField passwordField;
    private ToggleGroup roleGroup;
    private boolean onlyManageMode = false;

    private TableView<UserData> userTable;
    private ObservableList<UserData> users = FXCollections.observableArrayList();

    public AnggotaView() {
        anggotaController = new AnggotaController();
    }

    public AnggotaView(boolean onlyManageMode) {
        this();
        this.onlyManageMode = onlyManageMode;
    }

    public void show(Stage stage) {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: #34495e; -fx-border-radius: 10; -fx-background-radius: 10;");

        // Header with separate color
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(10));
        header.setStyle("-fx-background-color: #2c3e50; -fx-background-radius: 8;");

        Label titleLabel = new Label(onlyManageMode ? " Manajemen Pengguna" : " Pendaftaran Pengguna");
        titleLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: white;");
        header.getChildren().add(titleLabel);

        VBox content = new VBox(10);
        content.setAlignment(Pos.CENTER);

        if (!onlyManageMode) {
            usernameField = createTextField("Username");
            passwordField = createPasswordField();
            namaField = createTextField("Nama Lengkap");
            alamatField = createTextField("Alamat Email");
            HBox roleBox = createRoleToggleGroup();

            Button registerButton = new Button(" Daftar");
            registerButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 8; -fx-font-weight: bold;");
            addHoverEffect(registerButton);
            registerButton.setOnAction(e -> handleRegister(stage));

            Button exitButton = new Button("Keluar");
            exitButton.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white; -fx-background-radius: 8; -fx-font-weight: bold;");
            addHoverEffect(exitButton);
            exitButton.setOnAction(e -> handleExit(stage));

            content.getChildren().addAll(usernameField, passwordField, namaField, alamatField, roleBox, registerButton, exitButton);
        } else {
            TextField manageUsernameField = createTextField("Username Baru");
            PasswordField managePasswordField = createPasswordField();

            Button addBtn = new Button("➕ Tambah Pengguna");
            addBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8;");
            addHoverEffect(addBtn);
            addBtn.setOnAction(e -> {
                String username = manageUsernameField.getText();
                String password = managePasswordField.getText();
                if (!username.isEmpty() && !password.isEmpty()) {
                    users.add(new UserData(username, password));
                    manageUsernameField.clear();
                    managePasswordField.clear();
                }
            });

            Button deleteBtn = new Button("🗑️ Hapus Pengguna");
            deleteBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8;");
            addHoverEffect(deleteBtn);
            deleteBtn.setOnAction(e -> {
                UserData selected = userTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    users.remove(selected);
                }
            });

            HBox manageBox = new HBox(10, addBtn, deleteBtn);
            manageBox.setAlignment(Pos.CENTER);

            userTable = new TableView<>(users);
            TableColumn<UserData, String> userCol = new TableColumn<>("Username");
            userCol.setCellValueFactory(data -> data.getValue().usernameProperty());
            TableColumn<UserData, String> passCol = new TableColumn<>("Password");
            passCol.setCellValueFactory(data -> data.getValue().passwordProperty());
            userTable.getColumns().addAll(userCol, passCol);
            userTable.setPrefHeight(200);

            Button exitButton = new Button("Keluar");
            exitButton.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white; -fx-background-radius: 8; -fx-font-weight: bold;");
            addHoverEffect(exitButton);
            exitButton.setOnAction(e -> handleExit(stage));

            content.getChildren().addAll(new Label("👥 Manajemen Pengguna"), manageUsernameField, managePasswordField, manageBox, userTable, exitButton);
        }

        root.getChildren().addAll(header, content);

        Scene scene = new Scene(root, 540, 680);
        stage.setTitle("Manajemen Pengguna - Perpustakaan");
        stage.setScene(scene);
        stage.show();
    }

    private TextField createTextField(String prompt) {
        TextField textField = new TextField();
        textField.setPromptText(prompt);
        textField.setMaxWidth(250);
        textField.setStyle("-fx-background-radius: 8; -fx-padding: 8px; -fx-border-color: #dcdde1; -fx-border-width: 1;");
        return textField;
    }

    private PasswordField createPasswordField() {
        PasswordField pf = new PasswordField();
        pf.setPromptText("Password");
        pf.setMaxWidth(250);
        pf.setStyle("-fx-background-radius: 8; -fx-padding: 8px; -fx-border-color: #dcdde1; -fx-border-width: 1;");
        return pf;
    }

    private HBox createRoleToggleGroup() {
        roleGroup = new ToggleGroup();
        ToggleButton adminButton = new ToggleButton("Admin");
        ToggleButton mahasiswaButton = new ToggleButton("Mahasiswa");
        adminButton.setToggleGroup(roleGroup);
        mahasiswaButton.setToggleGroup(roleGroup);
        adminButton.setSelected(true);

        HBox roleBox = new HBox(10, adminButton, mahasiswaButton);
        roleBox.setAlignment(Pos.CENTER);
        return roleBox;
    }

    private void addHoverEffect(Button button) {
        button.setOnMouseEntered(e -> button.setStyle(button.getStyle() + "-fx-opacity: 0.85;"));
        button.setOnMouseExited(e -> button.setStyle(button.getStyle().replace("-fx-opacity: 0.85;", "")));
    }

    private void handleRegister(Stage stage) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String nama = namaField.getText();
        String alamat = alamatField.getText();
        String role = ((ToggleButton) roleGroup.getSelectedToggle()).getText();

        try {
            anggotaController.registerAnggota(username, password, nama, alamat, role);
        } catch (IllegalArgumentException ex) {
            new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
            return;
        }

        new Alert(Alert.AlertType.INFORMATION, "Pendaftaran berhasil!").showAndWait();
        openLoginView(stage);
    }

    private void openLoginView(Stage stage) {
        stage.close();
        new LoginView().show(new Stage());
    }

    private void handleExit(Stage stage) {
        stage.close();
        new LoginView().show(new Stage());
    }

    public static class UserData {
        private final javafx.beans.property.SimpleStringProperty username;
        private final javafx.beans.property.SimpleStringProperty password;

        public UserData(String username, String password) {
            this.username = new javafx.beans.property.SimpleStringProperty(username);
            this.password = new javafx.beans.property.SimpleStringProperty(password);
        }

        public javafx.beans.property.StringProperty usernameProperty() {
            return username;
        }

        public javafx.beans.property.StringProperty passwordProperty() {
            return password;
        }
    }
}