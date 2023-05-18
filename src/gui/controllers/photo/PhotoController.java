package gui.controllers.photo;

import be.Enum.SystemRole;
import be.Photo;
import com.jfoenix.controls.JFXTextArea;
import gui.controllers.TableViewController;
import gui.util.NodeAccessLevel;
import gui.util.TaskExecutor;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;


public class PhotoController extends TableViewController implements Initializable {
    @FXML
    public JFXTextArea txtaPhotoDescription;
    @FXML
    public ImageView imgPhotoArea;

    public Photo photo;
    public HBox buttonArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeButtonAccessLevels();
    }

    private void initializeButtonAccessLevels() {
        buttonAccessLevel = new NodeAccessLevel();

        addConfirmButton();
        addEditButton();
        addDeletePhotoBtn();
        addCancelButton();
    }


    public void setPhotoInfoContent(Photo photo) {
        this.photo = photo;
        txtaPhotoDescription.setText(photo.getDescription());
        imgPhotoArea.setImage(photo.getPhoto());
    }

    private void addDeletePhotoBtn() {
        String deleteText = "🗑 Delete";

        Button btnDeletePhoto = createButton(deleteText);

        NodeAccessLevel descriptionButtonAccess = new NodeAccessLevel();

        descriptionButtonAccess.addNodeAccessLevel(btnDeletePhoto,
                Arrays.asList(SystemRole.Administrator, SystemRole.ProjectManager,
                        SystemRole.Technician, SystemRole.SalesPerson));

        btnDeletePhoto.setOnAction(event -> {
            try {
                Task<Void> deletePhoto = getModelsHandler().getPhotoModel().deletePhoto(photo);

                TaskExecutor.executeTask(deletePhoto);
            } catch (Exception e){
                displayError(e);
            }
        });
        buttonArea.getChildren().add(btnDeletePhoto);
    }

    private void addConfirmButton() {
        String confirmText = "✔ Confirm";

        Button btnConfirm = createButton(confirmText);

        NodeAccessLevel descriptionButtonAccess = new NodeAccessLevel();

        descriptionButtonAccess.addNodeAccessLevel(btnConfirm,
                Arrays.asList(SystemRole.Administrator, SystemRole.ProjectManager,
                        SystemRole.Technician, SystemRole.SalesPerson));

        btnConfirm.setOnAction(event -> {

        });
        buttonArea.getChildren().add(btnConfirm);

    }

    private void addCancelButton() {
        String cancelText = "❌ Cancel";

        Button btnCancel = createButton(cancelText);

        NodeAccessLevel descriptionButtonAccess = new NodeAccessLevel();

        descriptionButtonAccess.addNodeAccessLevel(btnCancel,
                Arrays.asList(SystemRole.Administrator, SystemRole.ProjectManager,
                        SystemRole.Technician, SystemRole.SalesPerson));

        btnCancel.setOnAction(event -> {

        });
        buttonArea.getChildren().add(btnCancel);
    }

    private void addEditButton() {
        String editText = "✏ Edit";

        Button btnEdit = createButton(editText);

        NodeAccessLevel descriptionButtonAccess = new NodeAccessLevel();

        descriptionButtonAccess.addNodeAccessLevel(btnEdit,
                Arrays.asList(SystemRole.Administrator, SystemRole.ProjectManager,
                        SystemRole.Technician, SystemRole.SalesPerson));

        btnEdit.setOnAction(event -> {
            boolean isDescriptionEditable = txtaPhotoDescription.isEditable();

            if (isDescriptionEditable) {
                photo.setDescription(txtaPhotoDescription.getText());

                try {
                    Task<Boolean> updatePhotoTask = getModelsHandler().getPhotoModel().updatePhoto(photo);

                    TaskExecutor.executeTask(updatePhotoTask);
                } catch (Exception e) {
                    displayError(e);
                }
            }
            txtaPhotoDescription.setEditable(!isDescriptionEditable);

            btnEdit.setText(editText);

        });
        buttonArea.getChildren().add(btnEdit);
    }

}

