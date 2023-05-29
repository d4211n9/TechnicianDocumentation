package gui.controllers.installation;

import be.Device;
import be.Installation;
import gui.controllers.TableViewController;
import gui.util.TaskExecutor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class DeviceTabController extends TableViewController implements Initializable {

    @FXML
    private HBox deviceBtnArea;
    @FXML
    private TableColumn<Device, String> tcName;
    @FXML
    private TableColumn<Device, Integer> tcID;

    private Installation installation;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void loadTableView() {
        tcName.setCellValueFactory(new PropertyValueFactory<>("deviceTypeName"));
        tcID.setCellValueFactory(new PropertyValueFactory<>("id"));

        try {
            Task<ObservableList<Device>> allDevicesTask = getModelsHandler()
                    .getDrawingModel()
                    .getDevicesFromInstallation(installation.getID());

            allDevicesTask.valueProperty().addListener((observable, oldValue, newValue) -> {
                tableView.setItems(FXCollections.observableArrayList(newValue));
            });

            allDevicesTask.setOnFailed(event -> displayError(allDevicesTask.getException()));

            TaskExecutor.executeTask(allDevicesTask);

        } catch (Exception e) {
            displayError(e);
        }
    }

    public void setInstallation(Installation installation) {
        this.installation = installation;
    }
}
