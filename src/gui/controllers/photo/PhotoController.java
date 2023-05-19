package gui.controllers.photo;

import be.Enum.SystemRole;
import be.Photo;
import com.jfoenix.controls.JFXTextArea;
import gui.controllers.BaseController;
import gui.controllers.installation.PhotoTabController;
import gui.util.NodeAccessLevel;
import gui.util.TaskExecutor;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;


public class PhotoController extends BaseController implements Initializable {
    @FXML
    private JFXTextArea txtaDescription;
    @FXML
    private VBox vBoxBackground;
    @FXML
    private ImageView imgPhotoArea;
    @FXML
    private HBox buttonArea;

    private Photo photo;
    private PhotoTabController photoTabController;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeButtonAccessLevels();
    }

    private void initializeButtonAccessLevels() {
        buttonAccessLevel = new NodeAccessLevel();

        addEditButton();
        addDeletePhotoBtn();

    }


    public void setPhotoInfoContent(Photo photo) {
        this.photo = photo;
        txtaDescription.setText(photo.getDescription());
        imgPhotoArea.setImage(photo.getPhoto());

        imgPhotoArea.fitWidthProperty().bind(vBoxBackground.widthProperty());
        imgPhotoArea.fitHeightProperty().bind(vBoxBackground.heightProperty());
    }

    private void addDeletePhotoBtn() {
        String deleteText = "🗑 Delete";

        Button btnDeletePhoto = createButton(deleteText);

        NodeAccessLevel descriptionButtonAccess = new NodeAccessLevel();

        descriptionButtonAccess.addNodeAccessLevel
                (btnDeletePhoto,
                Arrays.asList(SystemRole.Administrator, SystemRole.ProjectManager, SystemRole.Technician));

        btnDeletePhoto.setOnAction(event -> {
            if (showQuestionDialog(photo.getDescription(), true)) {
                try {
                    Task<Void> deletePhoto = getModelsHandler().getPhotoModel().deletePhoto(photo);

                    deletePhoto.setOnSucceeded(event1 -> photoTabController.loadPhotosToInstallation());
                    deletePhoto.setOnFailed(e -> displayError(deletePhoto.getException()));

                    TaskExecutor.executeTask(deletePhoto);
                    closeWindow();

                } catch (Exception e){
                    displayError(e);
                }
            }
        });
        buttonArea.getChildren().add(btnDeletePhoto);
    }

    private void addEditButton() {
        String editText = "✏ Edit";
        String saveText = "💾 Save";

        Button btnEditSaveDescription = createButton(editText);

        NodeAccessLevel descriptionButtonAccess = new NodeAccessLevel();

        descriptionButtonAccess.addNodeAccessLevel(
                btnEditSaveDescription,
                Arrays.asList(SystemRole.Administrator, SystemRole.ProjectManager, SystemRole.Technician));

        btnEditSaveDescription.setOnAction(event -> {
            boolean isDescriptionEditable = txtaDescription.isEditable();

            if (isDescriptionEditable) {
                photo.setDescription(txtaDescription.getText());

                try {
                    Task<Boolean> updatePhotoTask = getModelsHandler().getPhotoModel().updatePhoto(photo);

                    updatePhotoTask.setOnSucceeded(event1 -> photoTabController.loadPhotosToInstallation());
                    updatePhotoTask.setOnFailed(e -> displayError(updatePhotoTask.getException()));

                    TaskExecutor.executeTask(updatePhotoTask);
                }
                catch (Exception e) {
                    displayError(e);
                }
            }

            txtaDescription.setEditable(!isDescriptionEditable);

            btnEditSaveDescription.setText(!isDescriptionEditable ? saveText : editText);
        });

        buttonArea.getChildren().add(btnEditSaveDescription);
    }

    private void closeWindow() {

        Stage stage = (Stage) imgPhotoArea.getScene().getWindow();
        stage.close();
    }

    public void setPhotoTabController(PhotoTabController photoTabController) {
        this.photoTabController = photoTabController;
    }

}

