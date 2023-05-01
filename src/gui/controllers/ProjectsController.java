package gui.controllers;

import be.Client;
import be.Enum.SystemRole;
import be.Project;
import com.jfoenix.controls.JFXButton;
import gui.util.MainControllerHandler;
import gui.util.NodeAccessLevel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import util.ViewPaths;

import java.net.URL;
import java.util.*;

public class ProjectsController extends BaseController implements Initializable {
    @FXML
    private VBox projectsView;
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
            SystemRole loggedInUserRole = getLoggedInUser();

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
        buttonArea.getChildren().add(0, button);}

    private void initializeButtonAccessLevels() {
        buttonAccessLevel = new NodeAccessLevel();

        buttonAccessLevel.addNodeAccessLevel(
                loadButton("➕📄 Add Project", ViewPaths.ADD_PROJECT_VIEW, projectsView),
                Arrays.asList(SystemRole.Administrator, SystemRole.ProjectManager));

        //TODO Slet, testing
        buttonAccessLevel.addNodeAccessLevel(
                loadButton("👥 Users", ViewPaths.USERS_VIEW, projectsView),
                Arrays.asList(SystemRole.Administrator));

        buttonAccessLevel.addNodeAccessLevel(
                loadButton("👥 Users", ViewPaths.USERS_VIEW, projectsView),
                Arrays.asList(SystemRole.Administrator));
    }

    private void loadTableView() {
        tcClient.setCellValueFactory(new PropertyValueFactory<>("client"));
        tcLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        tcProjectName.setCellValueFactory(new PropertyValueFactory<>("projectName"));
        tcCreated.setCellValueFactory(new PropertyValueFactory<>("created"));
        tvProjects.setItems(allProjects);
    }

    public void handleBack() {
        getMainController().mainBorderPane.setCenter(getMainController().getLastView());
        getMainController().saveLastView(projectsView);
    }
}
