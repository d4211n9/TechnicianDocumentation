package gui.controllers.installation;

import be.Enum.SystemRole;
import be.Installation;
import be.SystemUser;
import com.jfoenix.controls.JFXListView;
import gui.controllers.BaseController;
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

import java.awt.image.BufferedImage;
import java.io.File;
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
    private VBox installationInfo, vbUserBtnArea;
    @FXML
    private HBox infoBtnArea;
    @FXML
    private Label lblName, lblDescription;
    @FXML
    private JFXListView listUsers;
    private NodeAccessLevel buttonAccessLevel;
    private Installation installation;
    private ObservableList<SystemUser> obsAssignedUsers = null;

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

        try {
            //TODO OBS: den her driller på installationer uden tegning
            getModelsHandler().getDrawingModel().setSelectedDrawing(installation.getID());
        } catch (Exception e) {
            displayError(e);
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

    public void handleBack() {
        getMainController().mainBorderPane.setCenter(getMainController().getLastView());
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
            files.forEach((File f) -> images.add(new Image(f.toURI().toString())));
            displayImage();

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

    public void handleDeviceTab() {
        deviceTabController.loadTableView();
    }

    public void handleLoginTab() {
        loginTabController.loadTableView();
    }
}
