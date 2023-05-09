package gui.controllers;

import be.Enum.SystemRole;
import com.jfoenix.controls.JFXButton;
import gui.util.NodeAccessLevel;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;

import java.util.List;

public class TableViewController extends BaseController {
    @FXML
    public TableView tableView;
    @FXML
    public HBox buttonArea;
    @FXML
    public JFXButton editButton, deleteButton;
    public NodeAccessLevel buttonAccessLevel;


    public void addLoadedButtons() {
        try {
            //Gets the logged-in user's role
            SystemRole loggedInUserRole = getLoggedInUser();

            // Loops through the buttons and adds them to the sidebar if the user has the right access level
            for (Node button : buttonAccessLevel.getNodes()) {

                List<SystemRole> accessLevel = buttonAccessLevel.getAccessLevelsForNode(button);

                if(accessLevel.contains(loggedInUserRole))
                    addButton((Button) button);
            }
        } catch (Exception e) {
            displayError(e);
        }
    }

    public void addButton(Button button) {
        buttonArea.getChildren().add(0, button);
    }

    public void tvListener() {
        tableView.setOnMouseClicked(event -> {
            if(isTvSelected()){
                deleteButton.setDisable(false);
                editButton.setDisable(false);
            }else {
                deleteButton.setDisable(true);
                editButton.setDisable(true);
            }
        });
    }

    public boolean isTvSelected() {
        return tableView.getSelectionModel().getSelectedItem() != null;
    }
}
