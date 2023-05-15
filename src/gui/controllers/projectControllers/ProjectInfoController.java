package gui.controllers.projectControllers;

import be.Client;
import be.Enum.SystemRole;
import be.Installation;
import be.Project;
import be.SystemUser;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXToggleButton;
import gui.controllers.BaseController;
import gui.controllers.installation.CreateInstallationController;
import gui.controllers.installation.InstallationCardController;
import gui.controllers.installation.InstallationInfoController;
import gui.controllers.userController.CreateUserController;
import gui.util.NodeAccessLevel;
import gui.util.TaskExecutor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import util.ViewPaths;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ProjectInfoController extends BaseController implements Initializable {
    @FXML
    private VBox projectsView;
    @FXML
    private FlowPane fpInstallations;
    @FXML
    private HBox hbUserBtnArea;
    @FXML
    private JFXListView listUsers;
    @FXML
    private JFXToggleButton toggleUsers;
    @FXML
    private ComboBox cbStatus;
    @FXML
    private Label lblProjectTitle, lblClientName, lblClientLocation, lblClientType, lblClientEmail, lblClientPhone,
            lblCreated, lblProjectLocation, lblAssignedUsers, lblDescription;

    private Client client;
    private Project project;
    private List<Installation> installations;
    private JFXButton assignUser, unAssignUser;
    private ObservableList<SystemUser> obsAssignedUsers = null;
    private ObservableList<SystemUser> obsUnAssignedUsers = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeButtonAccessLevels();
        projectsView.getChildren().add(addButtons());
    }

    public void setContent(Project project) {
        this.project = project;
        client = project.getClient();

        lblClientName.setText(client.getName());
        lblClientLocation.setText(client.getAddress().toString());
        lblClientType.setText(client.getType());
        lblClientEmail.setText(client.getEmail());
        lblClientPhone.setText(client.getPhone());

        lblProjectTitle.setText(project.getName());
        lblDescription.setText(project.getDescription());
        lblProjectLocation.setText(project.getAddress().toString());
        lblCreated.setText(project.getCreated()+"");

        loadComboBoxOptions();
        cbStatus.getSelectionModel().select(project.getStatus());

        loadInstallations();
        loadUsers();
    }
    public void handleBack() {
        getMainController().mainBorderPane.setCenter(getMainController().getLastView());
    }


    private void initializeButtonAccessLevels() {
        buttonAccessLevel = new NodeAccessLevel();

        addAssignUserBtn();
        addUnAssignUserBtn();
        addCreateInstallationBtn();
    }

    private void addCreateInstallationBtn() {
        Button btnAddInstallation = createButton("➕🛠 Installation");
        buttonAccessLevel.addNodeAccessLevel(
                btnAddInstallation,
                Arrays.asList(SystemRole.Administrator, SystemRole.ProjectManager, SystemRole.Technician));
        btnAddInstallation.setOnMouseClicked(event -> {
            getMainController().saveLastView(projectsView);
            FXMLLoader createLoader = loadView(ViewPaths.CREATE_INSTALLATION);
            VBox createInstallation = createLoader.getRoot();
            CreateInstallationController controller = createLoader.getController();
            controller.setContent(project);
            getMainController().mainBorderPane.setCenter(createInstallation);
        });
    }

    private void addAssignUserBtn() {
        assignUser = createButton("➕👤 Add");
        buttonAccessLevel.addNodeAccessLevel(
                assignUser,
                Arrays.asList(SystemRole.Administrator, SystemRole.ProjectManager)); //TODO Korrekt accesslevel?
        addAssignedButton(assignUser);

        assignUser.setOnMouseClicked(event -> {
                FXMLLoader loader = loadView("/gui/views/projectViews/AddUserToProjectView.fxml");
                AddUserToProjectController controller = loader.getController();
                loadInMainView(loader.getRoot(), projectsView);
                controller.setProject(project);
        });
    }

    private void addUnAssignUserBtn() {
        unAssignUser = createButton("➖👤 Remove");
        buttonAccessLevel.addNodeAccessLevel(
                unAssignUser,
                Arrays.asList(SystemRole.Administrator, SystemRole.ProjectManager)); //TODO Korrekt accesslevel?
        addAssignedButton(unAssignUser);

        unAssignUser.setOnAction(event -> {
            FXMLLoader loader = loadView("/gui/views/projectViews/RemoveUserFormProjectView.fxml");
            RemoveUserFromProject controller = loader.getController();
            loadInMainView(loader.getRoot(), projectsView);
            controller.setContent(project);
        });
    }

    private void unAssignUser(SystemUser selectedUser) {
        try {
            Task<Void> deleteUserAssignedToProjectTask = getModelsHandler().getProjectModel().deleteSystemUserAssignedToProject(project.getID(),
                    selectedUser.getEmail());

            deleteUserAssignedToProjectTask.setOnSucceeded(succeddedEvent -> {
                obsAssignedUsers.remove(selectedUser);
                obsUnAssignedUsers.add(selectedUser);
            });

            deleteUserAssignedToProjectTask.setOnFailed(failedEvent -> displayError(deleteUserAssignedToProjectTask.getException()));

            TaskExecutor.executeTask(deleteUserAssignedToProjectTask);
        } catch (Exception e) {
            displayError(e);
        }
    }

    private void addAssignedButton(Button button) {
        hbUserBtnArea.getChildren().add(button);
    }


    private void loadInstallations() {
        try {
            Task<ObservableList<Installation>> allInstallationsTask = getModelsHandler()
                    .getInstallationModel()
                    .getAllInstallations(project.getID());

            allInstallationsTask.valueProperty().addListener((observable, oldValue, newValue) -> {
                installations = newValue;

                for (Installation i : installations) {
                    showInstallation(i);
                }
            });

            allInstallationsTask.setOnFailed(event -> displayError(allInstallationsTask.getException()));

            TaskExecutor.executeTask(allInstallationsTask);

        } catch (Exception e) {
            displayError(e);
        }
    }

    private void showInstallation(Installation i) {
        FXMLLoader cardLoader = loadView(ViewPaths.INSTALLATION_CARD);
        Pane installationCard = cardLoader.getRoot();
        InstallationCardController cardController = cardLoader.getController();
        cardController.setContent(i);
        installationCard.setOnMouseClicked(event -> {
            getMainController().saveLastView(projectsView);

            FXMLLoader infoLoader = loadView(ViewPaths.INSTALLATION_INFO);
            VBox installationInfo = infoLoader.getRoot();
            InstallationInfoController infoController = infoLoader.getController();
            infoController.setContent(i);
            getMainController().mainBorderPane.setCenter(installationInfo);
        });
        fpInstallations.getChildren().add(installationCard);
    }

    private void loadUsers() {
        loadAssignedUsers();
        loadUnAssignedUsers();
    }

    private void loadAssignedUsers() {
        try {
            Task<List<SystemUser>> assignedUsersTask = getModelsHandler().getProjectModel().
                    getSystemUsersAssignedToProject(project.getID());

            assignedUsersTask.valueProperty().addListener((observable, oldValue, newValue) -> {
                obsAssignedUsers = FXCollections.observableList(newValue);

                listUsers.setItems(obsAssignedUsers);
            });

            assignedUsersTask.setOnFailed(event -> displayError(assignedUsersTask.getException()));

            TaskExecutor.executeTask(assignedUsersTask);
        }
        catch (Exception e) {
            displayError(e);
        }
    }

    private void loadUnAssignedUsers() {
        try {
            Task<List<SystemUser>> unAssignedUsersTask = getModelsHandler().getProjectModel().
                    getSystemUsersAssignedToProject(project.getID()); //TODO Hent Un-assigned

            unAssignedUsersTask.valueProperty().addListener((observable, oldValue, newValue) -> {
                obsUnAssignedUsers = FXCollections.observableList(newValue);
            });

            unAssignedUsersTask.setOnFailed(event -> displayError(unAssignedUsersTask.getException()));

            TaskExecutor.executeTask(unAssignedUsersTask);
        }
        catch (Exception e) {
            displayError(e);
        }
    }

    /**
     * Switches between showing list of assigned and unassigned users
     */
    public void handleToggleUsers() {
        listUsers.getSelectionModel().select(null); //De-select user to avoid "hanging" selection
        if(toggleUsers.isSelected()) {
            listUsers.setItems(obsAssignedUsers);
            lblAssignedUsers.setText("Users Assigned");
        } else {
            listUsers.setItems(obsUnAssignedUsers);
            lblAssignedUsers.setText("Users Not Assigned");
        }
    }

    private void loadComboBoxOptions() {
        try {
            cbStatus.setItems(getModelsHandler().getProjectModel().getAllStatuses());
        } catch (Exception e) {
            displayError(e);
        }
    }
}
