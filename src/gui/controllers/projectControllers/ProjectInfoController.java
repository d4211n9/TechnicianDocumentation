package gui.controllers.projectControllers;

import be.Client;
import be.Enum.SystemRole;
import be.Project;
import com.jfoenix.controls.JFXButton;
import gui.controllers.BaseController;
import gui.util.NodeAccessLevel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import util.ViewPaths;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ProjectInfoController extends BaseController implements Initializable {
    public HBox hbUserBtnArea;
    @FXML
    private VBox projectsView;
    @FXML
    private FlowPane fpStaff, fpInstallations;
    @FXML
    private HBox buttonArea1;
    @FXML
    private Label lblProjectTitle, lblClientName, lblClientLocation, lblClientType, lblClientEmail, lblClientPhone, lblCreated, lblProjectLocation;

    private Client client;
    private Project project;

    private NodeAccessLevel buttonAccessLevel;

    private JFXButton editButton, deleteButton;

    public void setContent(Project project) {
        this.project = project;
        client = project.getClient();

        lblProjectTitle.setText(project.getName());
        lblClientName.setText(client.getName());
        lblClientLocation.setText(client.getLocation()); //TODO Obs.: Client og Project kan have forskellige locations, bør vi vise clients her?
        lblClientType.setText(client.getType());
        lblClientEmail.setText(client.getEmail());
        lblClientPhone.setText(client.getPhone());
        lblProjectLocation.setText(client.getLocation());
        lblCreated.setText(client.getLocation());

        //TODO Slet, tester InstallationCard in action
        FXMLLoader loader1 = loadView(ViewPaths.INSTALLATION_CARD);
        FXMLLoader loader2 = loadView(ViewPaths.INSTALLATION_CARD);
        HBox installationCard1 = loader1.getRoot();
        HBox installationCard2 = loader2.getRoot();
        fpInstallations.getChildren().add(installationCard1);
        fpInstallations.getChildren().add(installationCard2);
    }
    public void handleBack(ActionEvent actionEvent) {
    }


    private void initializeButtonAccessLevels() {
        buttonAccessLevel = new NodeAccessLevel();

        addRemoveAssignedUserBtn();
        addAssignUserBtn();
    }

    private void addAssignUserBtn() {//todo lav om til at slette ting fra view
        editButton = createButton("✏ Assign");
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
        deleteButton = createButton("🗑 Delete");
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
        hbUserBtnArea.getChildren().add(0, button);
        System.out.println("heeeey");}


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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeButtonAccessLevels();
        addLoadedButtons();
        System.out.println("mfkoe");
    }
}
