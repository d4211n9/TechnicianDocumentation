package gui.controllers.installation;

import be.Device;
import be.DeviceLogin;
import be.Enum.SystemRole;
import be.Installation;
import com.jfoenix.controls.JFXButton;
import gui.controllers.BaseController;
import gui.util.NodeAccessLevel;
import gui.util.TaskExecutor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class LoginTabController extends BaseController implements Initializable {

    @FXML
    private VBox loginTab;
    @FXML
    public TableView<DeviceLogin> tableView;
    @FXML
    private TableColumn<DeviceLogin, String> tcDevice, tcUsername, tcPassword;
    @FXML
    private TableColumn<DeviceLogin, Integer> tcID;
    @FXML
    private HBox loginBtnArea, hbEditArea;
    @FXML
    public JFXButton editButton, saveButton;
    @FXML
    private Label lblSelectedDevice;
    @FXML
    private TextField txtUsername, txtPassword;
    private Installation installation;
    private ObservableList<DeviceLogin> allDevicesWithLogin;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeButtonAccessLevels();
        loginBtnArea.getChildren().add(addButtons());
        tvListener();

    }

    private void initializeButtonAccessLevels() {
        buttonAccessLevel = new NodeAccessLevel();

        addSaveBtn();
        addEditBtn();
    }

    private void addEditBtn() {
        editButton = createButton("✏ Edit");
        buttonAccessLevel.addNodeAccessLevel(editButton,
                Arrays.asList(SystemRole.Administrator, SystemRole.ProjectManager, SystemRole.Technician));
        editButton.setDisable(true);

        editButton.setOnMouseClicked(event -> {
            hbEditArea.setDisable(false);

            DeviceLogin selectedDevice = tableView.getSelectionModel().getSelectedItem();

            lblSelectedDevice.setText(selectedDevice.getDeviceTypeName());
            txtUsername.setText(selectedDevice.getUsername());
            txtPassword.setText(selectedDevice.getPassword());
        });
    }

    private void addSaveBtn() {
        saveButton = createButton("💾 Save"); //Variable name is misleading as it's held in Parent controller
        buttonAccessLevel.addNodeAccessLevel(saveButton,
                Arrays.asList(SystemRole.Administrator, SystemRole.ProjectManager));
        saveButton.setDisable(true);

        saveButton.setOnMouseClicked(event -> {
            hbEditArea.setDisable(true);
            DeviceLogin deviceLogin = tableView.getSelectionModel().getSelectedItem();
            deviceLogin.setUsername(txtUsername.getText());
            deviceLogin.setPassword(txtPassword.getText());

            try {
                deviceLogin = getModelsHandler().getDrawingModel().updateDeviceLogin(deviceLogin);
                loadTableView();
            } catch (Exception e) {
                displayError(e);
            }
        });
    }

    public void loadTableView() {
        tcDevice.setCellValueFactory(new PropertyValueFactory<>("deviceTypeName"));
        tcUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        tcPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        tcID.setCellValueFactory(new PropertyValueFactory<>("ID"));

        try {
            List<DeviceLogin> allDevicesFromDrawingWithLogin = new ArrayList<>();

            Task<ObservableList<Device>> allDevicesTask = getModelsHandler()
                    .getDrawingModel()
                    .getDevicesFromInstallation(installation.getID());

            allDevicesTask.valueProperty().addListener((observable, oldValue, newValue) -> {
                for(Device device : newValue) {
                    if (device.getDeviceType().hasLoginDetails()) {
                        DeviceLogin deviceLogin = null;
                        try {
                            deviceLogin = getModelsHandler().getDrawingModel().getDeviceLogin(device);
                        } catch (Exception e) {
                            displayError(e);
                        }

                        if(deviceLogin == null) {
                            try {
                                deviceLogin = getModelsHandler().getDrawingModel().createDeviceLogin(new DeviceLogin(device, "", ""));
                            } catch (Exception e) {
                                displayError(e);
                            }
                        }
                        allDevicesFromDrawingWithLogin.add(deviceLogin);
                    }
                }

                allDevicesWithLogin = FXCollections.observableArrayList(allDevicesFromDrawingWithLogin);
                tableView.setItems(allDevicesWithLogin);
            });

            allDevicesTask.setOnFailed(event -> displayError(allDevicesTask.getException()));

            TaskExecutor.executeTask(allDevicesTask);

        } catch (Exception e) {
            displayError(e);
        }
    }

    public void tvListener() {
        tableView.setOnMouseClicked(event -> {
            if(isTvSelected()){
                saveButton.setDisable(false);
                editButton.setDisable(false);

                DeviceLogin selected = tableView.getSelectionModel().getSelectedItem();
                lblSelectedDevice.setText(selected.getDeviceTypeName());
                txtUsername.setText(selected.getUsername());
                txtPassword.setText(selected.getPassword());
            }else {
                saveButton.setDisable(true);
                editButton.setDisable(true);
                lblSelectedDevice.setText("Select a device...");
                txtUsername.setText("");
                txtPassword.setText("");
                hbEditArea.setDisable(true);
            }
        });
    }

    public boolean isTvSelected() {
        return tableView.getSelectionModel().getSelectedItem() != null;
    }

    public void setInstallation(Installation installation) {
        this.installation = installation;
    }
}
