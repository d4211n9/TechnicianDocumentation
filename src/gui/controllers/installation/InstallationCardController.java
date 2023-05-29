package gui.controllers.installation;

import be.Installation;
import be.SystemUser;
import com.jfoenix.controls.JFXListView;
import gui.controllers.BaseController;
import gui.util.TaskExecutor;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import util.SymbolPaths;

import java.util.List;

public class InstallationCardController extends BaseController {
    @FXML
    private Label lblName, lblDescription;
    @FXML
    private ImageView imgInstallation;
    @FXML
    private JFXListView listUsers;
    
    public void setContent(Installation installation) {
        lblName.setText(installation.getName());
        lblDescription.setText(installation.getDescription());
        imgInstallation.setImage(new Image(SymbolPaths.DRAWING));

        loadAssignedUsers(installation.getID());
    }

    private void loadAssignedUsers(int installationId) {
        try {
            Task<List<SystemUser>> usersAssignedToInstallation = getModelsHandler()
                    .getInstallationModel()
                    .getSystemUsersAssignedToInstallation(installationId);

            usersAssignedToInstallation
                    .valueProperty()
                    .addListener((observable, oldValue, newValue) -> listUsers.setItems(FXCollections.observableList(newValue)));

            usersAssignedToInstallation.setOnFailed(event -> displayError(usersAssignedToInstallation.getException()));

            TaskExecutor.executeTask(usersAssignedToInstallation);
        } catch (Exception e) {
            displayError(e);
        }
    }
}
