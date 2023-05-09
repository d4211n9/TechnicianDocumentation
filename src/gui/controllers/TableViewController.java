package gui.controllers;

import be.Enum.SystemRole;
import com.jfoenix.controls.JFXButton;
import gui.util.NodeAccessLevel;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;

public class TableViewController extends BaseController {
    @FXML
    public TableView tableView;
    @FXML
    public JFXButton editButton, deleteButton;

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
