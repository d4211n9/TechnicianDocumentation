package gui.controllers.installation;

import gui.controllers.TableViewController;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.HBox;

public class DeviceTabController extends TableViewController {

    @FXML
    private HBox deviceBtnArea;
    @FXML
    private TableColumn tcName, tcDescription;
    @FXML
    private TableColumn tcPrice;

}
