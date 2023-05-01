package gui.controllers;

import be.Client;
import be.Enum.SystemRole;
import be.Project;
import gui.models.ClientModel;
import gui.util.NodeAccessLevel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import util.ViewPaths;

import java.util.ArrayList;
import java.util.Arrays;

public class ClientController extends BaseController {
    private ClientModel clientModel;

    private NodeAccessLevel buttonAccessLevel;
    private ObservableList<Client> allClients = FXCollections.observableList(new ArrayList<>()); //TODO Slet, testing

    public ClientController() {
        try {
            clientModel = getModelsHandler().getClientModel();
        } catch (Exception e) {
            displayError(e);
        }
        allClients = clientModel.getAllClients();
    }

    private void initializeButtonAccessLevels() {
        buttonAccessLevel = new NodeAccessLevel();

        buttonAccessLevel.addNodeAccessLevel(
                loadButton("➕📄 Add Client", ViewPaths.CLIENTS_VIEW, (Node) allClients),
                Arrays.asList(SystemRole.Administrator, SystemRole.ProjectManager));

    }
        public void handleBack() {
            getMainController().mainBorderPane.setCenter(getMainController().getLastView());
        }

}
