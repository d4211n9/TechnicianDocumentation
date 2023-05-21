package gui.controllers.installation;

import be.Device;
import be.DeviceLogin;
import be.Drawing;
import gui.controllers.TableViewController;
import gui.util.NodeAccessLevel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class LoginTabController extends TableViewController implements Initializable {

    @FXML
    private VBox loginTab;
    @FXML
    private TableColumn<DeviceLogin, String> tcDevice, tcUsername, tcPassword;
    @FXML
    private TableColumn<DeviceLogin, Integer> tcID;
    @FXML
    private HBox loginBtnArea;

    private ObservableList<DeviceLogin> allDevicesWithLogin;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeButtonAccessLevels();
        loginBtnArea.getChildren().add(addButtons());
    }

    private void initializeButtonAccessLevels() {
        buttonAccessLevel = new NodeAccessLevel();

        /**
        buttonAccessLevel.addNodeAccessLevel(
                loadButton("➕🔑 Add Login", ViewPaths.CREATE_CLIENTS_VIEW, getMainController().mainBorderPane.getCenter()),
                Arrays.asList(SystemRole.Administrator, SystemRole.ProjectManager, SystemRole.Technician));
         */
    }

    public void loadTableView() {
        tcDevice.setCellValueFactory(new PropertyValueFactory<>("deviceTypeName"));
        tcUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        tcPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        tcID.setCellValueFactory(new PropertyValueFactory<>("ID"));

        try {
            Drawing selectedDrawing = getModelsHandler().getDrawingModel().getSelectedDrawing();

            if (selectedDrawing != null) {
                List<DeviceLogin> allDevicesFromDrawingWithLogin = new ArrayList<>();

                for(Device device : selectedDrawing.getDevices()) {
                    if (device.getDeviceType().hasLoginDetails()) {
                        DeviceLogin deviceLogin = getModelsHandler().getDrawingModel().getDeviceLogin(device);
                        allDevicesFromDrawingWithLogin.add(deviceLogin);
                    }
                }

                allDevicesWithLogin = FXCollections.observableArrayList(allDevicesFromDrawingWithLogin);
                tableView.setItems(allDevicesWithLogin);
            }
        } catch (Exception e) {
            displayError(e);
        }

        tableView.setItems(allDevicesWithLogin);
    }
}
