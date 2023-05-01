package gui.controllers;

import gui.util.MainControllerHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class AddProjectController extends BaseController {
    @FXML
    private VBox addProjectView;

    public void handleCancel() {
        handleBack();
    }

    public void handleBack() {
        getMainController().mainBorderPane.setCenter(getMainController().getLastView());
    }

    public void handleConfirm(ActionEvent actionEvent) {
    }
}
