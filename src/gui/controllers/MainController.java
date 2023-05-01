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
import java.util.Stack;

public class MainController extends BaseController implements Initializable {
    @FXML
    private VBox sidebar, homeView;
    @FXML
    public BorderPane mainBorderPane;
    @FXML
    private ImageView ivLogo;

    private NodeAccessLevel buttonAccessLevel;
    private Stack<Node> viewHistory = new Stack<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadImages();
        addLoadedButtons();
    }

    private void addButtonToSidebar(Button button) {
        sidebar.getChildren().add(1, button);
    }

    /**
     * Adds buttons in the sidebar menu,
     * based on the logged-in user's role.
     */
    private void addLoadedButtons() {
        initializeButtonAccessLevels();

        try {
            //Gets the logged-in user's role
            SystemRole loggedInUserRole = getModelsHandler()
                    .getSystemUserModel()
                    .getLoggedInSystemUser()
                    .getValue()
                    .getRole();

            // Loops through the buttons and adds them to the sidebar if the user has the right access level
            for (Node button : buttonAccessLevel.getNodes()) {

                List<SystemRole> accessLevel = buttonAccessLevel.getAccessLevelsForNode(button);

                if(accessLevel.contains(loggedInUserRole)) addButtonToSidebar((Button) button);
            }
        } catch (Exception e) {
            displayError(e);
        }
    }

    /**
     * Adds all buttons to the buttonAccessLevel variable
     *
     * HOW TO USE:
     * To add a new button with given access levels:
     * buttonAccessLevel.addNodeAccessLevel(
     *                      loadButton("*button text*",
     *                      *path of the view to open when clicked*),
     *                      Arrays.asList(*what roles you want to access this button*, ...))
     */
    private void initializeButtonAccessLevels() {
        buttonAccessLevel = new NodeAccessLevel();

        buttonAccessLevel.addNodeAccessLevel(
                loadButton("👥 Users", ViewPaths.USERS_VIEW),
                Arrays.asList(SystemRole.Administrator));

        buttonAccessLevel.addNodeAccessLevel(
                loadButton("📁 Projects", ViewPaths.PROJECTS_VIEW),
                Arrays.asList(SystemRole.Administrator, SystemRole.ProjectManager, SystemRole.SalesPerson, SystemRole.Technician));

        buttonAccessLevel.addNodeAccessLevel(
                loadButton("💰 Clients", ViewPaths.CLIENTS_VIEW),
                Arrays.asList(SystemRole.Administrator, SystemRole.ProjectManager, SystemRole.SalesPerson, SystemRole.Technician));
    }

    private Button loadButton(String text, String fxmlPath) {
        JFXButton button = new JFXButton(text);
        button.setFont(Font.font(16));
        button.setPrefWidth(120);
        button.setPrefHeight(60);
        button.setOnAction(e -> {
            mainBorderPane.setCenter(loadView(fxmlPath).getRoot());
            saveLastView(homeView);
        });

        return button;
    }

    public void saveLastView(Node node) {
        viewHistory.push(node);
    }

    public Node getLastView() {
        return viewHistory.pop();
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
