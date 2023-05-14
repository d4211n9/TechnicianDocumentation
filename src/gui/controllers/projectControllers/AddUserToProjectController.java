package gui.controllers.projectControllers;

import gui.controllers.TableViewController;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class AddUserToProjectController extends TableViewController {
    public void handleDoubleClick(MouseEvent mouseEvent) {
    }

    public void handleBack(ActionEvent actionEvent) {
        getMainController().mainBorderPane.setCenter(getMainController().getLastView());
    }

    public void handleSearch(KeyEvent keyEvent) {
    }
}
