package gui.controllers.installation;

import be.Enum.SystemRole;
import be.Installation;
import be.Photo;
import be.SystemUser;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXToggleButton;
import gui.controllers.BaseController;
import gui.util.NodeAccessLevel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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
    private VBox installationInfo, vbUserBtnArea;
    @FXML
    private HBox infoBtnArea, photosBtnArea, drawingBtnArea, deviceBtnArea, hbImage;
    @FXML
    private Label lblName, lblDescription, lblAssignedUsers;
    @FXML
    private JFXListView listUsers;
    @FXML
    private ImageView imgPhoto;
    @FXML
    private JFXButton assignUser, unAssignUser;
    @FXML
    private JFXToggleButton toggleUsers;

    private NodeAccessLevel buttonAccessLevel;
    private Installation installation;
    private final List<Image> images = new ArrayList<>();
    private int currentImageIndex = 0;
    private ObservableList<SystemUser> obsAssignedUsers = null;
    private ObservableList<SystemUser> obsUnAssignedUsers = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeButtonAccessLevels();
        userListener();

        hbImage.widthProperty().addListener((observable, oldValue, newValue) ->
                imgPhoto.setFitWidth((Double) newValue));
        hbImage.heightProperty().addListener((observable, oldValue, newValue) ->
                imgPhoto.setFitHeight((Double) newValue));
    }


    public void setContent(Installation installation) {
        this.installation = installation;
        lblName.setText(installation.getName());
        lblDescription.setText(installation.getDescription());

        loadUsers();

        if(installation.getDrawingBytes() != null) {
            Platform.runLater(() -> loadPhotos());
        }
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
                if(getModelsHandler().getInstallationModel().assignSystemUserToInstallation(
                        installation.getID(),
                        selectedUser.getEmail())) {
                    obsAssignedUsers.add(selectedUser);
                    obsUnAssignedUsers.remove(selectedUser);
                } else {
                    displayError(new Throwable("Failed to add the user to the installation"));
                }
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
                if(getModelsHandler().getInstallationModel().deleteSystemUserAssignedToInstallation(
                        installation.getID(),
                        selectedUser.getEmail())) {
                    obsAssignedUsers.remove(selectedUser);
                    obsUnAssignedUsers.add(selectedUser);
                } else {
                    displayError(new Throwable("Failed to remove the user from the installation"));
                }
            } catch (Exception e) {
                displayError(e);
                e.printStackTrace();
            }
        });
    }

    private void loadUsers() {
        try {
            List<SystemUser> assignedUsers = getModelsHandler().getInstallationModel().
                    getSystemUsersAssignedToInstallation(installation.getID());
            obsAssignedUsers = FXCollections.observableList(assignedUsers);

            List<SystemUser> unAssignedUsers = getModelsHandler().getInstallationModel().
                    getSystemUsersNotAssignedToInstallation(installation.getID());
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

    private void loadPhotos() {
        ByteArrayInputStream bais = new ByteArrayInputStream(installation.getDrawingBytes());
        try {
            images.add(convertToFxImage(ImageIO.read(bais)));
            displayImage();
        } catch (IOException e) {
            displayError(e);
        }
    }

    private static Image convertToFxImage(BufferedImage image) {
        WritableImage wr = null;
        if (image != null) {
            wr = new WritableImage(image.getWidth(), image.getHeight());
            PixelWriter pw = wr.getPixelWriter();
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    pw.setArgb(x, y, image.getRGB(x, y));
                }
            }
        }

        return new ImageView(wr).getImage();
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
                    //TODO opret Photo med byte[] constructor
                    byte[] fileContent = Files.readAllBytes(f.toPath());
                    Photo photo = new Photo(installation.getID(), fileContent, "billede beskrivelse..");
                    //getPhotoModel.Create(photo);
                    //installation.setDrawingBytes(fileContent);
                } catch (Exception e) {
                    displayError(e);
                }
                //images.add(new Image(f.toURI().toString()));
            });
            displayImage();
            try {
                getModelsHandler().getInstallationModel().updateInstallation(installation);
            } catch (Exception e) {
                displayError(e);
            }
        }
    }

    public void handleBtnPreviousAction() {
        if (!images.isEmpty()) {
            currentImageIndex = (currentImageIndex - 1 + images.size()) % images.size();
            displayImage();
        }
    }

    public void handleBtnNextAction() {
        if (!images.isEmpty()) {
            currentImageIndex = (currentImageIndex + 1) % images.size();
            displayImage();
        }
    }

    private void displayImage() {
        if (!images.isEmpty()) {
            imgPhoto.setImage(images.get(currentImageIndex));
        }
    }
}
