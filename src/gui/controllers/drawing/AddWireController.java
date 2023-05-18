package gui.controllers.drawing;

import be.DeviceType;
import gui.controllers.BaseController;
import gui.util.TaskExecutor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class AddWireController extends BaseController implements Initializable {

    @FXML
    private ComboBox<DeviceType> cbWireIcons;
    @FXML
    private ImageView ivWire;
    @FXML
    private TextField txtName;

    private DrawingController drawingController;
    private ObservableList<DeviceType> allWireShapes = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadAllWireIcons();
        createComboBoxContent();
    }

    public void handleConfirm() {
        DeviceType newDeviceType = createDeviceTypeFromFields();

        if(newDeviceType != null) {
            createDeviceType(newDeviceType);
        }
        handleBack();
    }

    private DeviceType createDeviceTypeFromFields() {
        if(validateInput()) {
            String name = txtName.getText();

            DeviceType selectedIcon = cbWireIcons.getSelectionModel().getSelectedItem();
            String imagePath = selectedIcon.getImagePath();

            DeviceType newDeviceType = new DeviceType(name, imagePath, false);

            return newDeviceType;
        }
        return null;
    }

    private void createDeviceType(DeviceType newDeviceType) {
        try {
            Task<Boolean> createDeviceTask = getModelsHandler()
                    .getDrawingModel().createDeviceType(newDeviceType);

            createDeviceTask.setOnSucceeded(event -> {
                try {
                    getModelsHandler().getDrawingModel().updateAllDeviceTypes();
                    drawingController.loadDeviceTypes();
                } catch (Exception e) {
                    displayError(e);
                }
            });

            createDeviceTask.setOnFailed(event -> displayError(createDeviceTask.getException()));

            TaskExecutor.executeTask(createDeviceTask);
        } catch (Exception e) {
            displayError(e);
        }
    }

    private boolean validateInput() {
        return !txtName.getText().isBlank() &&
                cbWireIcons.getSelectionModel().getSelectedItem() != null;
    }

    public void handleBack() {
        Stage stage = (Stage) ivWire.getParent().getScene().getWindow();
        stage.close();
    }

    private void loadAllWireIcons() {
        try {
            URL resource = AddWireController.class.getResource("/wire_icons/");
            Path dir = Paths.get(resource.toURI());

            Files.walk(dir).forEach(path -> showFile(path.toFile()));
        } catch (IOException e) {
            displayError(e);
        } catch (URISyntaxException e) {
            displayError(e);
        }
    }

    private void showFile(File file) {
        if(file.isFile()) {
            allWireShapes.add(new DeviceType(file.getName(), "/wire_icons/" + file.getName(), false));
        }
    }

    private void createComboBoxContent() {
        cbWireIcons.setItems(allWireShapes);

        cbWireIcons.setCellFactory(new AddWireController.ImageCellFactoryCallback());
        cbWireIcons.setButtonCell(new AddWireController.ListCellImage());

        cbWireIcons.setOnAction(event -> {
            DeviceType selectedIcon = cbWireIcons.getSelectionModel().getSelectedItem();
            if (selectedIcon != null) {
                ivWire.setImage(new Image(selectedIcon.getImagePath()));
            } else {
                ivWire.setImage(null);
            }
        });
    }

    public void setDrawingController(DrawingController drawingController) {
        this.drawingController = drawingController;
    }

    class ListCellImage extends ListCell<DeviceType> {
        @Override
        protected void updateItem(DeviceType item, boolean empty) {
            super.updateItem(item, empty);

            if (item == null || empty) {
                setGraphic(null);
            } else {
                ImageView iv = new ImageView(new Image(item.getImagePath()));
                iv.setFitHeight(30);
                iv.setFitWidth(30);
                setGraphic(iv);
                setText(item.getName().substring(0,item.getName().length()-4)); //Cuts off file type
            }
        }
    }

    class ImageCellFactoryCallback implements Callback<ListView<DeviceType>, ListCell<DeviceType>> {
        @Override
        public ListCell<DeviceType> call(ListView<DeviceType> item) {
            return new AddWireController.ListCellImage();
        }
    }
}
