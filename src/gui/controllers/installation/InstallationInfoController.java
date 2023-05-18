package gui.controllers.installation;

import be.Enum.SystemRole;
import be.Installation;
import be.Photo;
import be.SystemUser;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXToggleButton;
import gui.controllers.BaseController;
import gui.controllers.photo.PhotoCardController;
import gui.controllers.photo.PhotoController;
import gui.util.NodeAccessLevel;
import gui.util.TaskExecutor;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import util.SymbolPaths;
import util.ViewPaths;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class InstallationInfoController extends BaseController implements Initializable {
    @FXML
    public FlowPane fpPhotos;
    public ImageView imgPhotoArea;
    public JFXTextArea txtaPhotoDescription;
    @FXML
    private VBox installationInfo, vbUserBtnArea;
    @FXML
    private HBox infoBtnArea, photosBtnArea, drawingBtnArea, deviceBtnArea, hbImage;
    @FXML
    private Label lblName, lblDescription, lblAssignedUsers;
    @FXML
    private JFXListView listUsers;
    @FXML
    private JFXButton assignUser, unAssignUser;
    @FXML
    private JFXToggleButton toggleUsers;
    private NodeAccessLevel buttonAccessLevel;
    private Installation installation;
    private List<Photo> photos;

    public Photo photo;
    private int currentImageIndex = 0;
    private ObservableList<SystemUser> obsAssignedUsers = null;
    private ObservableList<SystemUser> obsUnAssignedUsers = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeButtonAccessLevels();
        userListener();
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


    public void setContent(Installation installation) {
        this.installation = installation;
        lblName.setText(installation.getName());
        lblDescription.setText(installation.getDescription());

        loadUsers();

        fpPhotos.prefWrapLengthProperty().bind(fpPhotos.widthProperty());
        loadPhotosToInstallation();
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
        addAssignUserBtn();
        addUnAssignUserBtn();
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
            controller.setEditContent(installation);
            loadInMainView(loader.getRoot(), installationInfo);
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

            assignUserToInstallation(selectedUser);
        });
    }

    private void assignUserToInstallation(SystemUser selectedUser) {
        try {
            Task<Boolean> assignUserToInstallationTask = getModelsHandler()
                    .getInstallationModel()
                    .assignSystemUserToInstallation(installation.getID(), selectedUser.getEmail());

            assignUserToInstallationTask.valueProperty().addListener((observable, oldValue, newValue) -> {
                if(newValue) {
                    obsAssignedUsers.add(selectedUser);
                    obsUnAssignedUsers.remove(selectedUser);
                } else {
                    displayError(new Throwable("Failed to add the user to the installation"));
                }
            });

            assignUserToInstallationTask.setOnFailed(failedEvent -> displayError(assignUserToInstallationTask.getException()));

            TaskExecutor.executeTask(assignUserToInstallationTask);
        } catch (Exception e) {
            displayError(e);
        }
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

            unAssignUser(selectedUser);
        });
    }

    private void unAssignUser(SystemUser selectedUser) {
        try {
            Task<Boolean> deleteUserAssignedToInstallationTask = getModelsHandler()
                    .getInstallationModel()
                    .deleteSystemUserAssignedToInstallation(installation.getID(), selectedUser.getEmail());

            deleteUserAssignedToInstallationTask.valueProperty().addListener((observable, oldValue, newValue) -> {
                if(newValue) {
                    obsAssignedUsers.remove(selectedUser);
                    obsUnAssignedUsers.add(selectedUser);
                } else {
                    displayError(new Throwable("Failed to remove the user from the installation"));
                }
            });

            deleteUserAssignedToInstallationTask.setOnFailed(failedEvent -> displayError(deleteUserAssignedToInstallationTask.getException()));

            TaskExecutor.executeTask(deleteUserAssignedToInstallationTask);
        } catch (Exception e) {
            displayError(e);
        }
    }

    private void loadUsers() {
        loadAssignedUsers();
        loadUnAssignedUsers();
    }

    private void loadAssignedUsers() {
        try {
            Task<List<SystemUser>> assignedUsersTask = getModelsHandler().getInstallationModel().
                    getSystemUsersAssignedToInstallation(installation.getID());

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

    private void loadUnAssignedUsers() {
        try {
            Task<List<SystemUser>> unAssignedUsersTask = getModelsHandler().getInstallationModel().
                    getSystemUsersNotAssignedToInstallation(installation.getID());

            unAssignedUsersTask.valueProperty().addListener((observable, oldValue, newValue) -> {
                List<SystemUser> unAssignedUsers = newValue;
                obsUnAssignedUsers = FXCollections.observableList(unAssignedUsers);
            });

            unAssignedUsersTask.setOnFailed(event -> displayError(unAssignedUsersTask.getException()));

            TaskExecutor.executeTask(unAssignedUsersTask);
        }
        catch (Exception e) {
            displayError(e);
        }
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

    private void addInfoButton(Button button) {
        infoBtnArea.getChildren().add(button);
    }
    private void addAssignedButton(Button button) {
        vbUserBtnArea.getChildren().add(button);
    }
    private void addPhotosButton(Button button) {
        photosBtnArea.getChildren().add(button);
    }
    private void addDrawingButton(Button button) {
        drawingBtnArea.getChildren().add(button);
    }
    private void addDeviceButton(Button button) {
        deviceBtnArea.getChildren().add(button);
    }

    public void handleBack() {
        getMainController().mainBorderPane.setCenter(getMainController().getLastView());
    }

    public void handleBtnLoadAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select image files");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images",
                "*.png", "*.jpg", "*.gif", "*.tif", "*.bmp"));
        List<File> files = fileChooser.showOpenMultipleDialog(new Stage());

        if (files != null && !files.isEmpty()) {
            files.forEach((File f) ->
            {
                try {
                    byte[] fileContent = Files.readAllBytes(f.toPath());
                    Photo photo = new Photo(installation.getID(), fileContent, f.getName());
                    photo = getModelsHandler().getPhotoModel().uploadPhoto(photo);
                    showPhotoCard(photo);

                } catch (Exception e) {
                    displayError(e);
                }
            });

            updateInstallation();
        }
    }

    private void updateInstallation() {
        try {
            Task<Installation> updateInstallationTask = getModelsHandler()
                    .getInstallationModel()
                    .updateInstallation(installation);

            updateInstallationTask.setOnFailed(event -> displayError(updateInstallationTask.getException()));

            TaskExecutor.executeTask(updateInstallationTask);
        } catch (Exception e) {
            displayError(e);
        }
    }

    public void showPhotoCard(Photo photo) {
        FXMLLoader photoCardLoader = loadView(ViewPaths.PHOTO_CARD);

        VBox photoCard = photoCardLoader.getRoot();
        PhotoCardController cardController = photoCardLoader.getController();
        cardController.setContent(photo);

        photoCard.setOnMouseClicked(event -> {
            System.out.println("photo clicked");
            FXMLLoader loader = openStage(ViewPaths.PHOTO_INFO, "photo info");
            PhotoController photoController = loader.getController();
            photoController.setPhotoInfoContent(photo);

        });

        fpPhotos.getChildren().add(photoCard);
    }
    private void loadPhotosToInstallation() {

        try{
            photos = getModelsHandler().getPhotoModel().getPhotoFromInstallation(installation.getID());
        } catch (Exception e) {
            e.printStackTrace();
            displayError(e);
        }
        if (photos != null) {
            for (Photo p : photos) {
                showPhotoCard(p);
            }
        }
    }


}
