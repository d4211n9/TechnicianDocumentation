package gui.controllers;

import be.Client;
import be.Enum.SystemRole;
import be.Project;
import com.jfoenix.controls.JFXButton;
import gui.util.NodeAccessLevel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import util.ViewPaths;

import java.net.URL;
import java.util.*;

public class ProjectsController extends BaseController implements Initializable {
    @FXML
    private HBox buttonArea;
    @FXML
    private TableView tvProjects;
    @FXML
    private TableColumn<Project, String> tcClient, tcLocation, tcProjectName;
    @FXML
    private TableColumn<Project, Date> tcCreated;
    private NodeAccessLevel buttonAccessLevel;

    private ObservableList<Project> allProjects = FXCollections.observableList(new ArrayList<>()); //TODO Slet, testing

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadTableView();
        addLoadedButtons();

    }

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

                if(accessLevel.contains(loggedInUserRole)) addButton((Button) button);
            }
        } catch (Exception e) {
            displayError(e);
        }
    }

    private void addButton(Button button) {
        buttonArea.getChildren().add(0, button);
    }

    private void initializeButtonAccessLevels() {
        buttonAccessLevel = new NodeAccessLevel();

        //TODO Slet, testing
        buttonAccessLevel.addNodeAccessLevel(
                loadButton("👥 Users", ViewPaths.USERS_VIEW),
                Arrays.asList(SystemRole.Administrator));

        buttonAccessLevel.addNodeAccessLevel(
                loadButton("👥 Users", ViewPaths.USERS_VIEW),
                Arrays.asList(SystemRole.Administrator));

        buttonAccessLevel.addNodeAccessLevel(
                loadButton("👥 Users", ViewPaths.USERS_VIEW),
                Arrays.asList(SystemRole.Administrator));

    }

    private Button loadButton(String text, String fxmlPath) {
        JFXButton button = new JFXButton(text);
        button.setFont(Font.font(16));
        button.setPrefWidth(150);
        button.setPrefHeight(60);
        //button.setOnAction(e -> mainBorderPane.setCenter(loadView(fxmlPath).getRoot()));

        return button;
    }

    private void loadTableView() {
        tcClient.setCellValueFactory(new PropertyValueFactory<>("client"));
        tcLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        tcProjectName.setCellValueFactory(new PropertyValueFactory<>("projectName"));
        tcCreated.setCellValueFactory(new PropertyValueFactory<>("created"));

        //TODO Slet, testing
        Calendar date = Calendar.getInstance();
        Client c1 = new Client("EASV", "Spangsbjerg Kirkevej 103, 6700 Esbjerg", "info@easv365.dk", "76133200");
        Client c2 = new Client("EASV", "Alsion 4, 6400 Sønderborg", "info@easv365.dk", "74403200");
        Project p1 = new Project(c1, "C3 Vindue", date.getTime());
        Project p2 = new Project(c1, "Innovatorium", date.getTime());
        Project p3 = new Project(c2, "Lobby", date.getTime());
        allProjects.add(p1);
        allProjects.add(p2);
        allProjects.add(p3);
        tvProjects.setItems(allProjects);
    }
}
