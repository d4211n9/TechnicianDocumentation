package gui.controllers.projectControllers;

import be.Address;
import be.Enum.SystemRole;
import be.Project;
import com.jfoenix.controls.JFXButton;
import gui.controllers.BaseController;
import gui.util.NodeAccessLevel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
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
    private TableView<Project> tvProjects;
    @FXML
    private TableColumn<Project, String> tcProjectName, tcClient;
    @FXML
    private TableColumn<Project, Address> tcLocation;
    @FXML
    private TableColumn<Project, Integer> tcID;
    @FXML
    private TableColumn<Project, Date> tcCreated;
    private NodeAccessLevel buttonAccessLevel;

    private JFXButton editButton, deleteButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadTableView();
        addLoadedButtons();
        tvListener();
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

    private void tvListener() {
        tvProjects.setOnMouseClicked(event -> {
            if(isTvSelected()){
                deleteButton.setDisable(false);
                editButton.setDisable(false);
            }else {
                deleteButton.setDisable(true);
                editButton.setDisable(true);
            }
        });
    }
    private boolean isTvSelected() {
        return tvProjects.getSelectionModel().getSelectedItem() != null;
    }

    private void addButton(Button button) {
        buttonArea.getChildren().add(0, button);}

    private void initializeButtonAccessLevels() {
        buttonAccessLevel = new NodeAccessLevel();

        addDeleteBtn();
        addEditBtn();


        buttonAccessLevel.addNodeAccessLevel(
                loadButton("➕📄 Add Project", ViewPaths.ADD_PROJECT_VIEW, projectsView),
                Arrays.asList(SystemRole.Administrator, SystemRole.ProjectManager));
    }

    private void addEditBtn() {
        editButton = createButton("✏ Edit Project");
        buttonAccessLevel.addNodeAccessLevel(editButton,
                Arrays.asList(SystemRole.Administrator, SystemRole.ProjectManager));
        editButton.setDisable(true);

        editButton.setOnMouseClicked(event -> {
            FXMLLoader loader = loadView(ViewPaths.ADD_PROJECT_VIEW);
            AddProjectController controller = loader.getController();
            controller.setEditContent(tvProjects.getSelectionModel().getSelectedItem());
            loadInMainView(loader.getRoot(), projectsView);
        });
    }

    private void addDeleteBtn() {
        deleteButton = createButton("🗑 Delete Project");
        buttonAccessLevel.addNodeAccessLevel(deleteButton,
                Arrays.asList(SystemRole.Administrator, SystemRole.ProjectManager));
        deleteButton.setDisable(true);

        deleteButton.setOnMouseClicked(event -> {
            Object project = tvProjects.getSelectionModel().getSelectedItem();
            if(showQuestionDialog(project.toString(), true)){
                //TODO Delete ned i lagene
            }
        });
    }

    private void loadTableView() {
        tcID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        tcClient.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        tcLocation.setCellValueFactory(new PropertyValueFactory<>("address"));
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
    }

    public void handleDoubleClick(MouseEvent mouseEvent) {
        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
            if(mouseEvent.getClickCount() == 2){
                FXMLLoader loader = loadView(ViewPaths.PROJECT_INFO_VIEW);
                getMainController().mainBorderPane.setCenter(loader.getRoot());
                getMainController().saveLastView(projectsView);

                ProjectInfoController controller = loader.getController();
                Project selected = tvProjects.getSelectionModel().getSelectedItem();
                controller.setContent(selected);
            }
        }
    }
}
