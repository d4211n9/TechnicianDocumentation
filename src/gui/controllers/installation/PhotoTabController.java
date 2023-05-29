package gui.controllers.installation;

import be.Installation;
import be.Photo;
import com.jfoenix.controls.JFXTextArea;
import gui.controllers.BaseController;
import gui.controllers.photo.PhotoCardController;
import gui.controllers.photo.PhotoController;
import gui.util.TaskExecutor;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import util.ViewPaths;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;
import java.util.ResourceBundle;

public class PhotoTabController extends BaseController implements Initializable {
    @FXML
    private FlowPane fpPhotos;
    @FXML
    private ImageView imgPhotoArea;
    @FXML
    private JFXTextArea txtaPhotoDescription;

    private Installation installation;
    private List<Photo> photos;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            installation = getModelsHandler().getInstallationModel().getSelectedInstallation();
        } catch (Exception e) {
            displayError(e);
        }
        fpPhotos.prefWrapLengthProperty().bind(fpPhotos.widthProperty());
        loadPhotosToInstallation();
    }

    public void showPhotoCard(Photo photo) {
        FXMLLoader photoCardLoader = loadView(ViewPaths.PHOTO_CARD);

        VBox photoCard = photoCardLoader.getRoot();
        PhotoCardController cardController = photoCardLoader.getController();
        cardController.setContent(photo);

        photoCard.setOnMouseClicked(event -> {
            FXMLLoader loader = openStage(ViewPaths.PHOTO_INFO, "Photo info");
            PhotoController photoController = loader.getController();
            photoController.setPhotoInfoContent(photo);
            photoController.setPhotoTabController(this);
        });

        fpPhotos.getChildren().add(photoCard);
    }

    public void loadPhotosToInstallation() {
        fpPhotos.getChildren().clear();
        try{
            Task<ObservableList<Photo>> allPhotosTask = getModelsHandler()
                    .getPhotoModel()
                    .getPhotoFromInstallation(installation.getID());

            allPhotosTask.valueProperty().addListener((observable, oldValue, newValue) -> {
                photos = newValue;

                if (photos != null) {
                    for (Photo p : photos) {
                        showPhotoCard(p);
                    }
                }
            });

            allPhotosTask.setOnFailed(event -> displayError(allPhotosTask.getException()));

            TaskExecutor.executeTask(allPhotosTask);

        } catch (Exception e) {
            displayError(e);
        }
    }

    public void handleBtnLoadAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select image files");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images",
                "*.png", "*.jpg", "*.gif", "*.tif", "*.bmp"));
        List<File> files = fileChooser.showOpenMultipleDialog(new Stage());

        if (files != null && !files.isEmpty()) {
            files.forEach((File f) ->
            {
                try {
                    byte[] fileContent = Files.readAllBytes(f.toPath());
                    Photo photo = new Photo(installation.getID(), fileContent, f.getName());
                    photo = getModelsHandler().getPhotoModel().uploadPhoto(photo);
                    showPhotoCard(photo);

                } catch (Exception e) {
                    displayError(e);
                }
            });

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
}
