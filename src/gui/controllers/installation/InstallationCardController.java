package gui.controllers.installation;

import be.Installation;
import com.jfoenix.controls.JFXListView;
import gui.controllers.BaseController;
import javafx.collections.FXCollections;
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
            listUsers.setItems(FXCollections.observableList(getModelsHandler()
                    .getInstallationModel()
                    .getSystemUsersAssignedToInstallation(installation.getID())));
        } catch (Exception e) {
            displayError(e);
        }
    }
}
