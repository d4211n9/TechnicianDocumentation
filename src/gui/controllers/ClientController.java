package gui.controllers;

import be.Client;
import be.Enum.SystemRole;
import gui.models.ClientModel;
import gui.util.NodeAccessLevel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import util.ViewPaths;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ClientController extends BaseController implements Initializable {
    @FXML
    private TableView tvProjects;
    @FXML
    private TableColumn<Client, String> tcName, tcLocation, tcEmail, tcPhone;

    @FXML
    private VBox clientView;
    private ClientModel clientModel;

    private NodeAccessLevel buttonAccessLevel;
    private ObservableList<Client> allClients = FXCollections.observableList(new ArrayList<>()); //TODO Slet, testing

    @FXML
    private HBox buttonArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addLoadedButtons();
        loadTableView();
    }

    private void loadTableView() {
        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        //tcLocation.setCellValueFactory(new PropertyValueFactory<>("location")); //TODO Add location til BE.Client og i Client tabel i DB
        tcEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tcPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        try {
            tvProjects.setItems(getModelsHandler().getClientModel().getAllClients());
        } catch (Exception e) {
            displayError(e);
        }
    }


    private void initializeButtonAccessLevels() {
        buttonAccessLevel = new NodeAccessLevel();

        buttonAccessLevel.addNodeAccessLevel(
                loadButton("➕📄 Add Client", ViewPaths.CLIENTS_VIEW, clientView),
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
