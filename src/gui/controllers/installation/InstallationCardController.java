package gui.controllers.installation;

import be.Installation;
import gui.controllers.BaseController;
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
    private HBox buttonArea;
    @FXML
    private ImageView imgInstallation;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imgInstallation.setImage(new Image(SymbolPaths.LOGO));
    }
    
    public void setContent(Installation installation) {
        lblName.setText(installation.getName());
        lblDescription.setText(installation.getDescription());
    }
}
