package gui.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import util.SymbolPaths;
import util.ViewPaths;

import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController extends BaseController implements Initializable {
    @FXML
    private VBox sidebar;
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private ImageView ivLogo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadImages();
        loadButton("➕ New User", ViewPaths.LOGIN_VIEW);
        loadButton("👥 All Users", ViewPaths.LOGIN_VIEW);
        loadButton("📄 All Projects", ViewPaths.LOGIN_VIEW);
    }

    private void loadButton(String text, String fxmlPath) {
        JFXButton button = new JFXButton(text);
        button.setFont(Font.font(16));
        button.setPrefWidth(120);
        button.setPrefHeight(60);
        button.setOnAction(e -> mainBorderPane.setCenter(loadView(fxmlPath).getRoot()));

        sidebar.getChildren().add(1, button);
    }

    private void loadImages() {
        Image logo = new Image(SymbolPaths.LOGO_NO_BG);
        ivLogo.setImage(logo);
    }

    private void close() {
        Stage stage = (Stage) mainBorderPane.getScene().getWindow();
        stage.close();
    }

    public void handleLogout() {
        close();
    }
}
