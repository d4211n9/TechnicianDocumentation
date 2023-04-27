package gui.controllers;

import be.Enum.SystemRole;
import com.jfoenix.controls.JFXButton;
import gui.util.ButtonAccessLevel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import util.SymbolPaths;
import util.ViewPaths;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class MainViewController extends BaseController implements Initializable {
    @FXML
    private VBox sidebar;
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private ImageView ivLogo;

    private ButtonAccessLevel buttonAccessLevel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadImages();
        //loadButton("➕ New User", ViewPaths.LOGIN_VIEW);
        //loadButton("👥 All Users", ViewPaths.LOGIN_VIEW);
        //loadButton("📄 All Projects", ViewPaths.LOGIN_VIEW);
        initializeButtonAccessLevels();
    }

    private void addLoadedButtons() {
        for (Button button : buttonAccessLevel.getButtons()) {
            List<SystemRole> accessLevel = buttonAccessLevel.getAccessLevelsForButton(button);
            
            //if (TODO get logged in system user from models handler)
        }
    }

    private void initializeButtonAccessLevels() {
        List<Button> buttons = loadButtons();

        List<List<SystemRole>> accessLevel = new ArrayList<>();
        accessLevel.add(Arrays.asList(SystemRole.Administrator));

        buttonAccessLevel = new ButtonAccessLevel(buttons, accessLevel);
    }

    private List<Button> loadButtons() {
        List<Button> buttons = new ArrayList<>();
        buttons.add(loadButton("Users", ViewPaths.USERS_VIEW));

        return buttons;
    }

    private Button loadButton(String text, String fxmlPath) {
        JFXButton button = new JFXButton(text);
        button.setFont(Font.font(16));
        button.setPrefWidth(120);
        button.setPrefHeight(60);
        button.setOnAction(e -> mainBorderPane.setCenter(loadView(fxmlPath).getRoot()));

        return button;
        //sidebar.getChildren().add(1, button);
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
