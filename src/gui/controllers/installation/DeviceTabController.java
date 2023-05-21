package gui.controllers.installation;

import be.Device;
import be.DeviceType;
import be.Drawing;
import gui.controllers.TableViewController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    private ObservableList<Device> allDevicesFromDrawing;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void loadTableView() {
        tcName.setCellValueFactory(new PropertyValueFactory<>("deviceTypeName"));
        tcID.setCellValueFactory(new PropertyValueFactory<>("id"));

        try {
            Drawing selectedDrawing = getModelsHandler().getDrawingModel().getSelectedDrawing();

            if (selectedDrawing != null) {
                allDevicesFromDrawing = FXCollections.observableArrayList(selectedDrawing.getDevices());
                tableView.setItems(allDevicesFromDrawing);
            }
        } catch (Exception e) {
            displayError(e);
        }
    }
}
