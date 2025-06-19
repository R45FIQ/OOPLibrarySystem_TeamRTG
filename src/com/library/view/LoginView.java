package com.library.view;

import com.library.controller.AuthController;
import com.library.controller.BukuController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LoginView {

    private static final String BTN_STYLE = "-fx-background-color:#d32f2f;-fx-text-fill:white;-fx-padding:6 32;";
    private TextField userField;
    private PasswordField passField;
    private ToggleGroup roleGroup;
    private AuthController authController;

    public LoginView() {
        authController = new AuthController(); // Initialize AuthController
    }

    public void show(Stage stage) {
        StackPane root = new StackPane();
        VBox loginBox = buildLoginBox(stage);
        ImageView bgView = buildBackgroundView();

        root.getChildren().addAll(bgView, loginBox);
        StackPane.setAlignment(loginBox, Pos.CENTER);

        Scene scene = new Scene(root, 700, 450);
        bindBgSize(bgView, scene);

        stage.setTitle("Login – Library");
        stage.setScene(scene);
        stage.show();
    }

    private VBox buildLoginBox(Stage stage) {
        Label title = createTitleLabel();
        HBox roleBox = createRoleToggleGroup();
        userField = createUserField();
        passField = createPasswordField();

        HBox footerBox = createFooterBox(stage);
        Button loginBtn = createLoginButton(stage);

        VBox box = new VBox(12, title, roleBox, userField, passField, footerBox, loginBtn);
        box.setPadding(new Insets(24));
        box.setAlignment(Pos.CENTER);
        box.setMaxWidth(320);
        box.setMaxHeight(320);
        box.setStyle("-fx-background-color:rgba(255,255,255,0.85);-fx-background-radius:12;");
        return box;
    }

    private Label createTitleLabel() {
        Label title = new Label("Login");
        title.setStyle("-fx-font-size:18px;-fx-font-weight:bold;");
        return title;
    }

    private HBox createRoleToggleGroup() {
        roleGroup = new ToggleGroup();
        ToggleButton adminT = new ToggleButton("Admin");
        ToggleButton memberT = new ToggleButton("Mahasiswa");
        adminT.setToggleGroup(roleGroup);
        memberT.setToggleGroup(roleGroup);
        adminT.setSelected(true);  // Default selected role is "Admin"

//        addHoverEffectToToggle(adminT);
//        addHoverEffectToToggle(memberT);
        HBox roleBox = new HBox(8, adminT, memberT);
        roleBox.setAlignment(Pos.CENTER);
        return roleBox;

    }

    private TextField createUserField() {
        TextField userField = new TextField();
        userField.setPromptText("Username");
        return userField;
    }

    private PasswordField createPasswordField() {
        PasswordField passField = new PasswordField();
        passField.setPromptText("Password");
        return passField;
    }

    private HBox createFooterBox(Stage stage) {
        Hyperlink signUpLink = new Hyperlink("Sign Up");
        signUpLink.setOnAction(e -> handleSignUp(stage));
        signUpLink.setStyle("-fx-text-fill:blue;");

        HBox signUpBox = new HBox(signUpLink);
        signUpBox.setAlignment(Pos.CENTER_LEFT);

        Hyperlink forgotLink = new Hyperlink("Forgot password?");
        forgotLink.setOnAction(e -> new Alert(Alert.AlertType.INFORMATION, "Menu lupa password belum tersedia.").showAndWait());
        forgotLink.setStyle("-fx-text-fill:blue;");

        HBox footerBox = new HBox(20, signUpBox, forgotLink);
        footerBox.setAlignment(Pos.CENTER);
        return footerBox;
    }

    private Button createLoginButton(Stage stage) {
        Button loginBtn = new Button("Login");
        loginBtn.setStyle(BTN_STYLE);
        loginBtn.setOnAction(e -> handleLogin(stage));
        return loginBtn;
    }

    private ImageView buildBackgroundView() {
        Image img = new Image(getClass().getResource("/com/library/image/bg.png").toExternalForm());
        ImageView view = new ImageView(img);
        view.setPreserveRatio(false);
        return view;
    }

    private void bindBgSize(ImageView bg, Scene scene) {
        bg.fitWidthProperty().bind(scene.widthProperty());
        bg.fitHeightProperty().bind(scene.heightProperty());
    }

    private void handleLogin(Stage stage) {
        String role = getSelectedRole();  // Get the selected role (Admin/Mahasiswa)
        if (role == null) {
            new Alert(Alert.AlertType.ERROR, "Harap pilih peran (Admin/Mahasiswa)!").showAndWait();
            return;  // Stop login process if no role is selected
        }

        String username = userField.getText().trim();  // Trim to avoid extra spaces
        String password = passField.getText().trim();  // Trim to avoid extra spaces

        // Validate login using AuthController
        boolean isValid = authController.validateLogin(username, password, role);
        if (isValid) {
            new Alert(Alert.AlertType.INFORMATION, "Login berhasil sebagai " + role).showAndWait();
            stage.close(); // Menutup halaman login

            // Membuat objek BukuController dan mengirimkannya ke DashboardView
            BukuController bukuController = new BukuController(); // Membuat objek BukuController
            new DashboardView(bukuController).show(new Stage(), username, role); // Beralih ke DashboardView
        } else {
            new Alert(Alert.AlertType.ERROR, "Username atau password salah!").showAndWait();
        }
    }

    private String getSelectedRole() {
        ToggleButton selectedToggle = (ToggleButton) roleGroup.getSelectedToggle();
        return selectedToggle != null ? selectedToggle.getText() : null;  // Return the role or null if none is selected
    }

    private void handleSignUp(Stage stage) {
        AnggotaView anggotaView = new AnggotaView();  // Create an instance of AnggotaView
        anggotaView.show(stage);  // Display the AnggotaView
    }
}
