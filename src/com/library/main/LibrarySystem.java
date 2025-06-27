package com.library.main;


import com.library.database.Koneksi;
import com.library.view.LoginView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
//main program
public class LibrarySystem extends Application {
   // Method untuk memulai aplikasi dan menampilkan tampilan login
    @Override
    public void start(Stage stage) {
        // Membuat instance LoginView dan menampilkannya
        LoginView loginView = new LoginView();
        loginView.show(stage); // Menampilkan tampilan login
    }
    // Method utama untuk menjalankan aplikasi
    public static void main(String[] args) {
        Koneksi.getConnection();
        launch(args);

    }
}
