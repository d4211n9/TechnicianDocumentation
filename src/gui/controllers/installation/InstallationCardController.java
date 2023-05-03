package gui.controllers.installation;

import gui.controllers.BaseController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import util.SymbolPaths;

import java.net.URL;
import java.util.ResourceBundle;

public class InstallationCardController extends BaseController implements Initializable {

    @FXML
    private ImageView imgInstallation;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imgInstallation.setImage(new Image(SymbolPaths.LOGO));
    }
}
