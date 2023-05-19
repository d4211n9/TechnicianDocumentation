package gui.controllers.installation;

import be.Enum.SystemRole;
import gui.controllers.TableViewController;
import gui.util.NodeAccessLevel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import util.ViewPaths;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class LoginTabController extends TableViewController implements Initializable {

    @FXML
    private VBox vbLoginTab, installationInfo;
    @FXML
    private TableColumn tcDevice, tcUsername, tcPassword;
    @FXML
    private HBox loginBtnArea;

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
}
