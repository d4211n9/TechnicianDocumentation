package gui.controllers.projectControllers;

import be.Client;
import be.Enum.SystemRole;
import be.Installation;
import be.Project;
import com.jfoenix.controls.JFXButton;
import gui.controllers.BaseController;
import gui.controllers.installation.InstallationCardController;
import gui.controllers.installation.InstallationInfoController;
import gui.util.NodeAccessLevel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import util.ViewPaths;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class ProjectInfoController extends BaseController {
    @FXML
    private VBox projectsView;
    @FXML
    private FlowPane fpStaff, fpInstallations;
    @FXML
    private HBox buttonArea;
    @FXML
    private Label lblProjectTitle, lblClientName, lblClientLocation, lblClientType, lblClientEmail, lblClientPhone, lblCreated, lblProjectLocation;

    private Client client;
    private Project project;
    private List<Installation> installations;

    private NodeAccessLevel buttonAccessLevel;

    private JFXButton editButton, deleteButton;

    public void setContent(Project project) {
        this.project = project;
        client = project.getClient();

        lblProjectTitle.setText(project.getName());
        lblClientName.setText(client.getName());
        lblClientLocation.setText(client.getLocation());
        lblClientType.setText(client.getType());
        lblClientEmail.setText(client.getEmail());
        lblClientPhone.setText(client.getPhone());
        lblProjectLocation.setText(client.getLocation());
        lblCreated.setText(project.getCreated()+"");

        loadInstallations();
    }
    public void handleBack() {
        getMainController().mainBorderPane.setCenter(getMainController().getLastView());
    }


    private void initializeButtonAccessLevels() {
        buttonAccessLevel = new NodeAccessLevel();

        addRemoveAssignedUserBtn();
        addAssignUserBtn();

        buttonAccessLevel.addNodeAccessLevel(
                loadButton("➕📄 Add Project", ViewPaths.ADD_PROJECT_VIEW, projectsView),
                Arrays.asList(SystemRole.Administrator, SystemRole.ProjectManager));
    }

    private void addAssignUserBtn() {//todo lav om til at slette ting fra view
        editButton = createButton("✏ Edit Project");
        buttonAccessLevel.addNodeAccessLevel(editButton,
                Arrays.asList(SystemRole.Administrator, SystemRole.ProjectManager));
        editButton.setDisable(true);

        editButton.setOnMouseClicked(event -> {
            FXMLLoader loader = loadView(ViewPaths.ADD_PROJECT_VIEW);
            AddProjectController controller = loader.getController();
            // todo controller.setEditContent(tvProjects.getSelectionModel().getSelectedItem());
            loadInMainView(loader.getRoot(), projectsView);
        });
    }

    private void addRemoveAssignedUserBtn() {
        deleteButton = createButton("🗑 Delete Project");
        buttonAccessLevel.addNodeAccessLevel(deleteButton,
                Arrays.asList(SystemRole.Administrator, SystemRole.ProjectManager));
        deleteButton.setDisable(true);
        deleteButton.setOnMouseClicked(event -> {
            //todo Object project = tvProjects.getSelectionModel().getSelectedItem();
            if(showQuestionDialog(project.toString(), true)){
                //TODO Delete ned i lagene
            }
        });
    }
    private void addButton(Button button) {
        buttonArea.getChildren().add(0, button);}


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
}
