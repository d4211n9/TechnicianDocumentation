package gui.controllers.projectControllers;

import be.Enum.ProjectStatus;
import be.Enum.SystemRole;
import be.Project;
import gui.controllers.TableViewController;
import gui.util.NodeAccessLevel;
import gui.util.TaskExecutor;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import util.ViewPaths;

import java.net.URL;
import java.util.*;

public class ProjectsController extends TableViewController implements Initializable {

    @FXML
    private VBox projectsView;
    @FXML
    private TableColumn<Project, String> tcProjectName, tcClient, tcStreet, tcPostalCode, tcCity;
    @FXML
    private TableColumn<Project, ProjectStatus> tcStatus;
    @FXML
    private TableColumn<Project, Date> tcCreated;
    @FXML
    private TextField txtfSearch;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadTableView();
        initializeButtonAccessLevels();
        projectsView.getChildren().add(addButtons());
        tvListener();
        projectBackgroundUpdate();
    }

    private void projectBackgroundUpdate() {
        try {
            List<Runnable> backgroundUpdateList = new ArrayList<>();
            backgroundUpdateList.add(getModelsHandler().getProjectModel());

            backgroundUpdate(backgroundUpdateList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

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
            controller.setEditContent((Project) tableView.getSelectionModel().getSelectedItem());
            loadInMainView(loader.getRoot(), projectsView);
        });
    }

    private void addDeleteBtn() {
        deleteButton = createButton("🗑 Delete Project");
        buttonAccessLevel.addNodeAccessLevel(deleteButton,
                Arrays.asList(SystemRole.Administrator, SystemRole.ProjectManager));
        deleteButton.setDisable(true);

        deleteButton.setOnMouseClicked(event -> {
            Project project = (Project) tableView.getSelectionModel().getSelectedItem();//todo den burde lige vise hvis der er nogle installationer der ikke er færdige
            if(showQuestionDialog(project.toString(), true)){
                handleDelete(project);
            }
        });
    }

    private void handleDelete(Project project) {
        try {
            Task<Void> deleteProjectTask = getModelsHandler().getProjectModel().deleteProject(project);

            deleteProjectTask.setOnSucceeded(event -> handleSearch());
            deleteProjectTask.setOnFailed(event -> displayError(deleteProjectTask.getException()));

            TaskExecutor.executeTask(deleteProjectTask);

        } catch (Exception e) {
            displayError(e);
        }
    }

    private void loadTableView() {
        tcStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tcClient.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        tcStreet.setCellValueFactory(new PropertyValueFactory<>("street"));
        tcPostalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        tcCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        tcProjectName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcCreated.setCellValueFactory(new PropertyValueFactory<>("created"));
        try {
            tableView.setItems(getModelsHandler().getProjectModel().getAllProjects());
        } catch (Exception e) {
            displayError(e);
        }
    }

    public void handleSearch() {
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
        if(mouseEvent.getButton().equals(MouseButton.PRIMARY) && isTvSelected()){
            if(mouseEvent.getClickCount() == 2){
                FXMLLoader loader = loadView(ViewPaths.PROJECT_INFO_VIEW);
                getMainController().mainBorderPane.setCenter(loader.getRoot());
                getMainController().saveLastView(projectsView);

                ProjectInfoController controller = loader.getController();
                Project selected = (Project) tableView.getSelectionModel().getSelectedItem();
                controller.setContent(selected);
            }
        }
    }
}
