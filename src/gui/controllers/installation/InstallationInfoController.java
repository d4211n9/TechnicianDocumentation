package gui.controllers.installation;

import be.Enum.SystemRole;
import be.Installation;
import be.SystemUser;
import com.jfoenix.controls.JFXListView;
import gui.controllers.BaseController;
import gui.controllers.drawing.DrawingController;
import gui.controllers.projectControllers.ProjectInfoController;
import gui.util.NodeAccessLevel;
import gui.util.TaskExecutor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import util.ViewPaths;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class InstallationInfoController extends BaseController implements Initializable {

    @FXML
    private DeviceTabController deviceTabController;
    @FXML
    private LoginTabController loginTabController;
    @FXML
    private DrawingController backgroundController;
    @FXML
    private VBox installationInfo, vbUserBtnArea;
    @FXML
    private HBox infoBtnArea;
    @FXML
    private Label lblName, lblDescription;
    @FXML
    private JFXListView listUsers;

    private ProjectInfoController projectInfoController;
    private NodeAccessLevel buttonAccessLevel;
    private Installation installation;
    private ObservableList<SystemUser> obsAssignedUsers = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeButtonAccessLevels();
    }

    private void installationBackgroundUpdate() {
        try {
            List<Runnable> backgroundUpdateList = new ArrayList<>();
            backgroundUpdateList.add(getModelsHandler().getInstallationModel());

            backgroundUpdate(backgroundUpdateList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setContent(Installation installation, ProjectInfoController projectInfoController) {
        this.installation = installation;
        this.projectInfoController = projectInfoController;

        lblName.setText(installation.getName());
        lblDescription.setText(installation.getDescription());

        loadUsers();
    }

    /**
     * Shows buttons relevant for each tab.
     *
     * Add new by specifying:
     *      Button [btnName] = createButton("[Button text]");
     *      buttonAccessLevel.addNodeAccessLevel(
     *      [btnName],
     *      Arrays.asList([SystemRoles with access]);
     *      add[Tab]Button([btnName]);
     */
    private void initializeButtonAccessLevels() {
        buttonAccessLevel = new NodeAccessLevel();

        addEditBtn();
        addDeleteBtn();
    }

    private void addDeleteBtn() {
        Button btnDelete = createButton("🗑 Delete");
        buttonAccessLevel.addNodeAccessLevel(
                btnDelete,
                Arrays.asList(SystemRole.Administrator, SystemRole.ProjectManager, SystemRole.Technician));
        addInfoButton(btnDelete);

        btnDelete.setOnAction(event -> {
            if(showQuestionDialog(installation.getName(), true)) {
                handleDelete();
            }
        });
    }

    private void addEditBtn() {
        Button btnEditInfo = createButton("✏ Edit info");
        buttonAccessLevel.addNodeAccessLevel(
                btnEditInfo,
                Arrays.asList(SystemRole.Administrator, SystemRole.ProjectManager, SystemRole.Technician));
        addInfoButton(btnEditInfo);

        btnEditInfo.setOnAction(event -> {
            FXMLLoader loader = loadView(ViewPaths.CREATE_INSTALLATION);
            CreateInstallationController controller = loader.getController();
            controller.setEditContent(installation, projectInfoController);
            loadInMainView(loader.getRoot(), projectInfoController.projectInfoView);
        });
    }

    private void loadUsers() {
        loadAssignedUsers();
    }

    private void loadAssignedUsers() {
        try {
            Task<List<SystemUser>> assignedUsersTask = getModelsHandler()
                    .getProjectModel()
                    .getSystemUsersAssignedToProject(installation.getProjectID());

            assignedUsersTask.valueProperty().addListener((observable, oldValue, newValue) -> {
                List<SystemUser> assignedUsers = newValue;
                obsAssignedUsers = FXCollections.observableList(assignedUsers);

                listUsers.setItems(obsAssignedUsers);
            });

            assignedUsersTask.setOnFailed(event -> displayError(assignedUsersTask.getException()));

            TaskExecutor.executeTask(assignedUsersTask);
        }
        catch (Exception e) {
            displayError(e);
        }
    }

    private void addInfoButton(Button button) {
        infoBtnArea.getChildren().add(button);
    }

    private void handleDelete() {
        try {
            Task<Void> deleteInstallationTask = getModelsHandler().getInstallationModel().deleteInstallation(installation);

            deleteInstallationTask.setOnSucceeded(event -> {
                projectInfoController.loadInstallations();
                handleBack();
            });

            deleteInstallationTask.setOnFailed(event -> displayError(deleteInstallationTask.getException()));

            TaskExecutor.executeTask(deleteInstallationTask);
        } catch (Exception e) {
            displayError(e);
        }
    }

    public void handleBack() {
        getMainController().mainBorderPane.setCenter(getMainController().getLastView());
    }

    public void handleDeviceTab() {
        deviceTabController.setInstallation(installation);
        deviceTabController.loadTableView();
    }

    public void handleLoginTab() {
        loginTabController.setInstallation(installation);
        loginTabController.loadTableView();
    }

    public void handleDrawingTab() {
        backgroundController.setInstallation(installation);
    }
}
