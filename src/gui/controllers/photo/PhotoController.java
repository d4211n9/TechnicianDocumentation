package gui.controllers.photo;

import be.Photo;
import com.jfoenix.controls.JFXTextArea;
import gui.controllers.BaseController;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;


public class PhotoController extends BaseController {
    @FXML
    public JFXTextArea txtaPhotoDescription;
    @FXML
    public ImageView imgPhotoArea;

    public Photo photo;


    public void setPhotoInfoContent(Photo photo) {
        this.photo = photo;
        txtaPhotoDescription.setText(photo.getDescription());
        imgPhotoArea.setImage(photo.getPhoto());

    }




}
