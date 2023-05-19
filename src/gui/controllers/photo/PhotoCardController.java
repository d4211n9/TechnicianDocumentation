package gui.controllers.photo;

import be.Photo;
import gui.controllers.BaseController;
import gui.controllers.ErrorDisplayController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import util.SymbolPaths;
import util.ViewPaths;

import java.net.URL;
import java.util.ResourceBundle;

public class PhotoCardController extends BaseController implements Initializable {

    public VBox photoCard;
    @FXML
    private Label lblPhotoDescription;
    @FXML
    private ImageView imgPhoto;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //imgPhoto.setImage(new Image(SymbolPaths.LOGO));
    }

    public void setContent(Photo photo) {
        lblPhotoDescription.setText(photo.getDescription());
        imgPhoto.setImage(photo.getPhoto());
    }

}
