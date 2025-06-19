package com.library.main;


import com.library.database.Koneksi;
import com.library.view.LoginView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;

public class LibrarySystem extends Application {

    @Override
    public void start(Stage stage) {
        // Membuat instance LoginView dan menampilkannya
        LoginView loginView = new LoginView();
        loginView.show(stage); // Menampilkan tampilan login
    }

    public static void main(String[] args) {
        Koneksi.getConnection();
        launch(args);

    }
}
