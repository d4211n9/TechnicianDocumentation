package gui.controllers.installation;

import be.Installation;
import be.SystemUser;
import com.jfoenix.controls.JFXListView;
import gui.controllers.BaseController;
import gui.util.TaskExecutor;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import util.SymbolPaths;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class InstallationCardController extends BaseController implements Initializable {
    @FXML
    private Label lblName, lblDescription;
    @FXML
    private ImageView imgInstallation;
    @FXML
    private JFXListView listUsers;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imgInstallation.setImage(new Image(SymbolPaths.LOGO));
    }
    
    public void setContent(Installation installation) {
        lblName.setText(installation.getName());
        lblDescription.setText(installation.getDescription());
        try {
            Task<List<SystemUser>> usersAssignedToInstallation = getModelsHandler()
                    .getInstallationModel()
                    .getSystemUsersAssignedToInstallation(installation.getID());

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
