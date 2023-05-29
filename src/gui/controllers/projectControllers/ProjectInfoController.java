package gui.controllers.projectControllers;

import be.Client;
import be.Enum.SystemRole;
import be.Installation;
import be.Project;
import be.SystemUser;
import bll.util.PDFGenerator;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import gui.controllers.BaseController;
import gui.controllers.installation.*;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import util.ViewPaths;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ProjectInfoController extends BaseController implements Initializable {
    @FXML
    public VBox projectInfoView;
    @FXML
    private FlowPane fpInstallations;
    @FXML
    private ScrollPane spInstallation;
    @FXML
    private HBox hbUserBtnArea, hbProjectDescription;
    @FXML
    private JFXListView listUsers;
    @FXML
    private ComboBox cbStatus;
    @FXML
    private Label lblProjectTitle, lblClientName, lblClientLocation, lblClientType, lblClientEmail, lblClientPhone,
            lblCreated, lblProjectLocation;
    @FXML
    private JFXTextArea txtaDescription;

    private Client client;
    private Project project;
    private List<Installation> installations;
    private JFXButton assignUser, unAssignUser;
    private ObservableList<SystemUser> obsAssignedUsers = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeButtonAccessLevels();
        projectInfoView.getChildren().add(addButtons());

        fpInstallations.prefWrapLengthProperty().bind(spInstallation.widthProperty());
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
        txtaDescription.setText(project.getDescription());
        lblProjectLocation.setText(project.getAddress().toString());
        lblCreated.setText(project.getCreated()+"");

        loadComboBoxOptions();
        cbStatus.getSelectionModel().select(project.getStatus());

        loadInstallations();
        loadAssignedUsers();
    }
    public void handleBack() {
        getMainController().mainBorderPane.setCenter(getMainController().getLastView());
    }


    private void initializeButtonAccessLevels() {
        buttonAccessLevel = new NodeAccessLevel();

        addPrintButton();
        addUnAssignUserBtn();
        addAssignUserBtn();
        addCreateInstallationBtn();
        addEditSaveDescriptionBtn();
    }

    private void addPrintButton() {
        Button printButton = createButton("🖨 Print");
        buttonAccessLevel.addNodeAccessLevel(
                printButton,
                Arrays.asList(SystemRole.Administrator, SystemRole.ProjectManager, SystemRole.SalesPerson));
        printButton.setOnMouseClicked(event -> {
            File destination = new DirectoryChooser().showDialog(projectInfoView.getParent().getScene().getWindow());
            if (destination != null) {
                try {
                    PDFGenerator.generateProjectPdf(project, installations, destination.getAbsolutePath());
                } catch (Exception e) {
                    displayError(e);
                }
            }
        });
    }

    private void addEditSaveDescriptionBtn() {
        String editText = "✏ Edit";
        String saveText = "💾 Save";

        Button btnEditSaveDescription = createButton(editText);

        NodeAccessLevel descriptionButtonAccess = new NodeAccessLevel();

        descriptionButtonAccess.addNodeAccessLevel(
                btnEditSaveDescription,
                Arrays.asList(SystemRole.Administrator, SystemRole.ProjectManager));

        btnEditSaveDescription.setOnAction(event -> {
            boolean isDescriptionEditable = txtaDescription.isEditable();

            if (isDescriptionEditable) {
                project.setDescription(txtaDescription.getText());

                try {
                    Task<Boolean> updateProjectTask = getModelsHandler().getProjectModel().updateProject(project);

                    TaskExecutor.executeTask(updateProjectTask);
                }
                catch (Exception e) {
                    displayError(e);
                }
            }

            txtaDescription.setEditable(!isDescriptionEditable);

            btnEditSaveDescription.setText(!isDescriptionEditable ? saveText : editText);
        });

        hbProjectDescription.getChildren().add(btnEditSaveDescription);
    }

    private void addCreateInstallationBtn() {
        Button btnAddInstallation = createButton("➕🛠 Installation");
        buttonAccessLevel.addNodeAccessLevel(
                btnAddInstallation,
                Arrays.asList(SystemRole.Administrator, SystemRole.ProjectManager, SystemRole.Technician));
        btnAddInstallation.setOnMouseClicked(event -> {
            FXMLLoader createLoader = loadView(ViewPaths.CREATE_INSTALLATION);
            VBox createInstallation = createLoader.getRoot();
            CreateInstallationController controller = createLoader.getController();
            controller.setContent(project, this);
            getMainController().mainBorderPane.setCenter(createInstallation);
            getMainController().saveLastView(projectInfoView);
        });
    }

    private void addAssignUserBtn() {
        assignUser = createButton("➕👤 Add");
        buttonAccessLevel.addNodeAccessLevel(
                assignUser,
                Arrays.asList(SystemRole.Administrator, SystemRole.ProjectManager));
        addAssignedButton(assignUser);

        assignUser.setOnMouseClicked(event -> {
                FXMLLoader loader = loadView(ViewPaths.ADD_TO_PROJECT);
                AddUserToProjectController controller = (AddUserToProjectController) loader.getController();
                loadInMainView(loader.getRoot(), projectInfoView);
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
            FXMLLoader loader = loadView(ViewPaths.REMOVE_FROM_PROJECT);
            RemoveUserFromProject controller = loader.getController();
            loadInMainView(loader.getRoot(), projectInfoView);
            controller.setContent(project);
        });
    }

    private void addAssignedButton(Button button) {
        hbUserBtnArea.getChildren().add(button);
    }


    public void loadInstallations() {
        fpInstallations.getChildren().clear();

        try {
            Task<ObservableList<Installation>> allInstallationsTask = getModelsHandler()
                    .getInstallationModel()
                    .getAllInstallations(project.getID());

            allInstallationsTask.valueProperty().addListener((observable, oldValue, newValue) -> {
                installations = newValue;

                for (Installation i : installations) {
                    try {
                        showInstallation(i);
                    } catch (Exception e) {
                        displayError(e);
                    }
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
            try {
                getModelsHandler().getInstallationModel().setSelectedInstallation(i);
            } catch (Exception e) {
                displayError(e);
            }

            FXMLLoader infoLoader = loadView(ViewPaths.INSTALLATION_INFO);
            InstallationInfoController infoController = (InstallationInfoController) infoLoader.getController();
            infoController.setContent(i, this);

            getMainController().mainBorderPane.setCenter(infoLoader.getRoot());
            getMainController().saveLastView(projectInfoView);
        });
        fpInstallations.getChildren().add(installationCard);
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

    private void loadComboBoxOptions() {
        try {
            cbStatus.setItems(getModelsHandler().getProjectModel().getAllStatuses());
        } catch (Exception e) {
            displayError(e);
        }
    }
}
