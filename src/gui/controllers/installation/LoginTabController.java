package gui.controllers.installation;

import be.Installation;
import gui.controllers.TableViewController;
import gui.util.NodeAccessLevel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginTabController extends TableViewController implements Initializable {

    @FXML
    private VBox vbLoginTab, installationInfo;
    @FXML
    private TableColumn tcDevice, tcUsername, tcPassword;
    @FXML
    private HBox loginBtnArea;

    private Installation installation;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            installation = getModelsHandler().getInstallationModel().getSelectedInstallation();
        } catch (Exception e) {
            displayError(e);
        }
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
