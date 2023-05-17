package gui.controllers.drawing;

import be.DeviceType;
import gui.controllers.BaseController;
import gui.util.TaskExecutor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Callback;
import util.InputValidator;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class AddDeviceController extends BaseController implements Initializable {

    @FXML
    private TextField txtName;
    @FXML
    private ComboBox<DeviceType> cbDeviceIcons;
    @FXML
    private ImageView ivDevice;
    @FXML
    private CheckBox chbLoginDetails;
    private ObservableList<DeviceType> allDeviceIcons = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadAllDeviceIcons();
        createComboBoxContent();
    }

    private void createComboBoxContent() {
        cbDeviceIcons.setItems(allDeviceIcons);

        cbDeviceIcons.setCellFactory(new ImageCellFactoryCallback());
        cbDeviceIcons.setButtonCell(new ListCellImage());

        cbDeviceIcons.setOnAction(event -> {
            DeviceType selectedIcon = cbDeviceIcons.getSelectionModel().getSelectedItem();
            if (selectedIcon != null) {
                ivDevice.setImage(new Image(selectedIcon.getImagePath()));
            } else {
                ivDevice.setImage(null);
            }
        });
    }

    public void handleBack() {
        Stage stage = (Stage) ivDevice.getParent().getScene().getWindow();
        stage.close();
    }

    public void handleConfirm() {
        System.out.println("Pressed confirm");
        DeviceType newDeviceType = createDeviceTypeFromFields();
        System.out.println(newDeviceType.getName() + ", " + newDeviceType.getImagePath() + ", " + newDeviceType.hasLoginDetails());

        if(newDeviceType != null) {
            createDeviceType(newDeviceType);
        }

        //TODO Indlæs det nye devicetype på listen i drawing viewet
        handleBack();
    }

    private DeviceType createDeviceTypeFromFields() {
        if(validateInput()) {
            System.out.println("Valid input");
            String name = txtName.getText();

            DeviceType selectedIcon = cbDeviceIcons.getSelectionModel().getSelectedItem();
            String imagePath = selectedIcon.getImagePath();

            boolean hasLoginDetails = chbLoginDetails.isSelected();

            DeviceType newDeviceType = new DeviceType(name, imagePath, hasLoginDetails);

            return newDeviceType;
        }
        return null;
    }

    private void createDeviceType(DeviceType newDeviceType) {
        try {
            Task<Boolean> createDeviceTask = getModelsHandler()
                    .getDrawingModel().createDeviceType(newDeviceType);

            createDeviceTask.setOnFailed(event -> displayError(createDeviceTask.getException()));

            TaskExecutor.executeTask(createDeviceTask);
        } catch (Exception e) {
            displayError(e);
        }
    }

    private boolean validateInput() {
        return InputValidator.isName(txtName.getText()) && cbDeviceIcons.getSelectionModel().getSelectedItem() != null;
    }

    //TODO loadAllDeviceIcons + showFile skal rykkes til DAL
    private void loadAllDeviceIcons() {
        try {
            URL resource = AddDeviceController.class.getResource("/device_icons/");
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
            allDeviceIcons.add(new DeviceType(file.getName(), "/device_icons/" + file.getName(), false));
        }
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
            return new ListCellImage();
        }
    }
}
