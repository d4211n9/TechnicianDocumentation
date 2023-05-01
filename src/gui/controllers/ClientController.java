package gui.controllers;

import be.Client;
import be.Enum.SystemRole;
import be.Project;
import gui.util.NodeAccessLevel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import util.ViewPaths;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClientController extends BaseController {

    private NodeAccessLevel buttonAccessLevel;
    private ObservableList<Client> allClients = FXCollections.observableList(new ArrayList<>()); //TODO Slet, testing
    @FXML
    private HBox buttonArea;
    

    private void initializeButtonAccessLevels() {
        buttonAccessLevel = new NodeAccessLevel();

        buttonAccessLevel.addNodeAccessLevel(
                loadButton("➕📄 Add Client", ViewPaths.CLIENTS_VIEW, (Node) allClients),
                Arrays.asList(SystemRole.Administrator, SystemRole.ProjectManager));
    }

    public void handleBack() {
        getMainController().mainBorderPane.setCenter(getMainController().getLastView());
    }

    private void addButton(Button button) {
        buttonArea.getChildren().add(0, button);}

    private void addLoadedButtons() {
        initializeButtonAccessLevels();

        try {
            SystemRole loggedInUserRole = getLoggedInUser();

            // Loops through the buttons and adds them to the sidebar if the user has the right access level
            for (Node button : buttonAccessLevel.getNodes()) {

                List<SystemRole> accessLevel = buttonAccessLevel.getAccessLevelsForNode(button);
                if(accessLevel.contains(loggedInUserRole)) addButton((Button) button);
            }
        } catch (Exception e) {
            displayError(e);
        }
    }

}
