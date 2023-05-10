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
import gui.util.NodeAccessLevel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
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
    private HBox buttonArea, hbUserBtnArea;
    @FXML
    private JFXListView listUsers;
    @FXML
    private JFXToggleButton toggleUsers;
    @FXML
    private Label lblProjectTitle, lblClientName, lblClientLocation, lblClientType, lblClientEmail, lblClientPhone,
            lblCreated, lblProjectLocation, lblAssignedUsers;

    private Client client;
    private Project project;
    private List<Installation> installations;
    private NodeAccessLevel buttonAccessLevel;
    private JFXButton assignUser, unAssignUser;
    private ObservableList<SystemUser> obsAssignedUsers = null;
    private ObservableList<SystemUser> obsUnAssignedUsers = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addLoadedButtons();
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
        lblProjectLocation.setText(project.getAddress().toString());
        lblCreated.setText(project.getCreated()+"");

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
        assignUser.setDisable(true);

        assignUser.setOnAction(event -> {
            SystemUser selectedUser = (SystemUser) listUsers.getSelectionModel().getSelectedItem();
            try {
                getModelsHandler().getProjectModel().assignSystemUserToProject(project.getID(),
                        selectedUser.getEmail());
                obsAssignedUsers.add(selectedUser);
                obsUnAssignedUsers.remove(selectedUser);
            } catch (Exception e) {
                displayError(e);
                e.printStackTrace();
            }
        });
    }

    private void addUnAssignUserBtn() {
        unAssignUser = createButton("➖👤 Remove");
        buttonAccessLevel.addNodeAccessLevel(
                unAssignUser,
                Arrays.asList(SystemRole.Administrator, SystemRole.ProjectManager)); //TODO Korrekt accesslevel?
        addAssignedButton(unAssignUser);
        unAssignUser.setDisable(true);

        unAssignUser.setOnAction(event -> {
            SystemUser selectedUser = (SystemUser) listUsers.getSelectionModel().getSelectedItem();
            try {
                getModelsHandler().getProjectModel().deleteSystemUserAssignedToProject(project.getID(),
                        selectedUser.getEmail());
                obsAssignedUsers.remove(selectedUser);
                obsUnAssignedUsers.add(selectedUser);
            } catch (Exception e) {
                displayError(e);
                e.printStackTrace();
            }
        });
    }

    private void addButton(Button button) {
        buttonArea.getChildren().add(0, button);}

    private void addAssignedButton(Button button) {
        hbUserBtnArea.getChildren().add(button);
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

    private void loadInstallations() {
        try {
            installations = getModelsHandler().getInstallationModel().getAllInstallations(project.getID());
        } catch (Exception e) {
            e.printStackTrace();
            displayError(e);
        }

        for (Installation i : installations) {
            showInstallation(i);
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
        try {
            List<SystemUser> assignedUsers = getModelsHandler().getProjectModel().
                    getSystemUsersAssignedToProject(project.getID());
            obsAssignedUsers = FXCollections.observableList(assignedUsers);

            List<SystemUser> unAssignedUsers = getModelsHandler().getProjectModel().
                    getSystemUsersAssignedToProject(project.getID()); //TODO Hent Un-assigned
            obsUnAssignedUsers = FXCollections.observableList(unAssignedUsers);
        } catch (Exception e) {
            displayError(e);
            e.printStackTrace(); //TODO replace with log to the database?
        }

        listUsers.setItems(obsAssignedUsers);
    }

    private void userListener() {
        listUsers.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            //If a user is selected from the assigned users list we enable the remove button
            if(n != null && toggleUsers.isSelected()) {
                unAssignUser.setDisable(false);
            }
            //If a user is selected from the unassigned users list we enable the add button
            else if (n != null && !toggleUsers.isSelected()) {
                assignUser.setDisable(false);
            }
            //If no user is selected we disable both buttons
            else {
                assignUser.setDisable(true);
                unAssignUser.setDisable(true);
            }
        });
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
}
