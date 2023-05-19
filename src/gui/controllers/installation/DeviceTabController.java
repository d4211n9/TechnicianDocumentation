package gui.controllers.installation;

import be.Installation;
import gui.controllers.TableViewController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class DeviceTabController extends TableViewController implements Initializable {

    @FXML
    private HBox deviceBtnArea;
    @FXML
    private TableColumn tcName, tcDescription;
    @FXML
    private TableColumn tcPrice;

    private Installation installation;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            installation = getModelsHandler().getInstallationModel().getSelectedInstallation();
        } catch (Exception e) {
            displayError(e);
        }
    }
}
