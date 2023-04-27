package gui.controllers;

import be.Enum.SystemRole;
import com.jfoenix.controls.JFXButton;
import gui.util.NodeAccessLevel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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

    private NodeAccessLevel nodeAccessLevel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadImages();
        addLoadedButtons();
    }

    private void addLoadedButtons() {
        initializeButtonAccessLevels();

        try {
            SystemRole loggedInUserRole = getModelsHandler()//gets logged in user role
                    .getSystemUserModel()
                    .getLoggedInSystemUser()
                    .getValue()
                    .getRole();

            for (Node button : nodeAccessLevel.getNodes()) { //checks if the users has access to the btn, and adds it
                List<SystemRole> accessLevel = nodeAccessLevel.getAccessLevelsForNode(button);
                if(accessLevel.contains(loggedInUserRole)){
                    sidebar.getChildren().add(1, button);
                }
            }
        } catch (Exception e) {
            displayError(e);
        }


    }

    private void initializeButtonAccessLevels() {
        nodeAccessLevel = new NodeAccessLevel();

        nodeAccessLevel.addButtonAccessLevel( //todo make guide for adding btns
                loadButton("Users", ViewPaths.USERS_VIEW),
                Arrays.asList(SystemRole.Administrator));
    }

    private Button loadButton(String text, String fxmlPath) {
        JFXButton button = new JFXButton(text);
        button.setFont(Font.font(16));
        button.setPrefWidth(120);
        button.setPrefHeight(60);
        button.setOnAction(e -> mainBorderPane.setCenter(loadView(fxmlPath).getRoot()));

        return button;
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
