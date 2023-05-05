package gui.controllers.installation;

import be.Enum.SystemRole;
import be.Installation;
import gui.controllers.BaseController;
import gui.util.NodeAccessLevel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class InstallationInfoController extends BaseController implements Initializable {
    @FXML
    private VBox installationInfo;
    @FXML
    private HBox infoBtnArea, photosBtnArea, drawingBtnArea, deviceBtnArea;
    @FXML
    private Label lblName;

    private NodeAccessLevel buttonAccessLevel;
    private Installation installation;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeButtonAccessLevels();
    }


    public void setContent(Installation installation) {
        this.installation = installation;
        lblName.setText(installation.getName());

    }

    /**
     * Shows buttons relevant for each tab.
     *
     * Add new by specifying:
     *      Button [btnName] = createButton("[Button text]");
     *      buttonAccessLevel.addNodeAccessLevel(
     *      [btnName],
     *      Arrays.asList([SystemRoles with access]);
     *      add[Tab]Button([btnName]);
     */
    private void initializeButtonAccessLevels() {
        buttonAccessLevel = new NodeAccessLevel();

        Button btnEditInfo = createButton("✏ Edit info");
        buttonAccessLevel.addNodeAccessLevel(
                btnEditInfo,
                Arrays.asList(SystemRole.Administrator, SystemRole.ProjectManager, SystemRole.Technician));
        addInfoButton(btnEditInfo);
    }

    private void addInfoButton(Button button) {
        infoBtnArea.getChildren().add(button);
    }
    private void addPhotosButton(Button button) {
        infoBtnArea.getChildren().add(button);
    }
    private void addDrawingButton(Button button) {
        infoBtnArea.getChildren().add(button);
    }
    private void addDeviceButton(Button button) {
        infoBtnArea.getChildren().add(button);
    }

    public void handleBack() {
        getMainController().mainBorderPane.setCenter(getMainController().getLastView());
    }
}
