package gui.controllers.photo;

import be.Photo;
import gui.controllers.BaseController;
import gui.controllers.ErrorDisplayController;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import util.SymbolPaths;
import util.ViewPaths;

import java.net.URL;
import java.util.ResourceBundle;

public class PhotoCardController extends BaseController implements Initializable {

    public Label lblPhotoDescription;
    public ImageView imgPhoto;
    public Label lblPhotoName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //imgPhoto.setImage(new Image(SymbolPaths.LOGO));
    }

    public void setContent(Photo photo) {
        lblPhotoDescription.setText(photo.getDescription());

        FileChooser fileChooser = new FileChooser();
        lblPhotoName.setText(fileChooser.getInitialFileName());

    }

    public VBox createPhotoCard(Photo photo) {
        VBox photoCard = null;

        try{
            photoCard = FXMLLoader.load(getClass().getResource("/gui/views/photoViews/PhotoCard.fxml"));
            //photoCard = ViewPaths.PHOTO_CARD;

        } catch (Exception e) {
            displayError(e);
        }

        Image img = new Image(SymbolPaths.LOGO);
        ImageView imgView = new ImageView(img);

        imgView.setPreserveRatio(true);
        imgView.setFitWidth(200);
        imgView.setFitHeight(160);

        //lblPhotoDescription = photo.getDescription();

        return photoCard;
    }

}
