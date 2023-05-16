package gui.controllers.photo;

import be.Photo;
import gui.controllers.BaseController;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import util.SymbolPaths;
import java.net.URL;
import java.util.ResourceBundle;

public class PhotoCardController extends BaseController implements Initializable {

    public Label lblPhotoDescription;
    public ImageView imgPhoto;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imgPhoto.setImage(new Image(SymbolPaths.LOGO));
    }

    public void setContent(Photo photo) {
        lblPhotoDescription.setText(photo.getDescription());
        imgPhoto.setImage(photo.getPhoto());
    }

}
