package gui.controllers;

import be.Enum.SystemRole;
import be.Project;
import gui.util.NodeAccessLevel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import util.ViewPaths;

import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class ProjectsController extends BaseController implements Initializable {
    @FXML
    private TextField txtfSearch;
    @FXML
    private VBox projectsView;
    @FXML
    private HBox buttonArea;
    @FXML
    private TableView tvProjects;
    @FXML
    private TableColumn<Project, String> tcLocation, tcProjectName;
    @FXML
    private TableColumn<Project, Integer> tcClient;
    @FXML
    private TableColumn<Project, Date> tcCreated;
    private NodeAccessLevel buttonAccessLevel;

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

    private void addButton(Button button) {buttonArea.getChildren().add(0, button);}

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
        tcClient.setCellValueFactory(new PropertyValueFactory<>("clientID")); //TODO Ændre fra at vise ID til kundens navn
        tcLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        tcProjectName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcCreated.setCellValueFactory(new PropertyValueFactory<>("created"));
        try {
            tvProjects.setItems(getModelsHandler().getProjectModel().getAllProjects());
        } catch (Exception e) {
            displayError(e);
        }
    }

    public void handleSearch(KeyEvent keyEvent) {
        try {
            getModelsHandler().getProjectModel().search(txtfSearch.getText());
        } catch (Exception e) {
            displayError(e);
        }
    }

    public void handleBack() {
        getMainController().mainBorderPane.setCenter(getMainController().getLastView());
        getMainController().saveLastView(projectsView);
    }
}
