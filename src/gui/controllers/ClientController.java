package gui.controllers;

import be.Client;
import be.Enum.SystemRole;
import com.jfoenix.controls.JFXButton;
import gui.controllers.userController.CreateUserController;
import gui.models.ClientModel;
import gui.util.NodeAccessLevel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import util.ViewPaths;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ClientController extends BaseController implements Initializable {
    public TextField txtfSearch;
    @FXML
    private TableView<Client> tvClients;
    @FXML
    private TableColumn<Client, String> tcName, tcLocation, tcEmail, tcPhone;

    @FXML
    private VBox clientView;
    private ClientModel clientModel;

    private JFXButton editButton, deleteButton;

    private NodeAccessLevel buttonAccessLevel;
    private ObservableList<Client> allClients = FXCollections.observableList(new ArrayList<>()); //TODO Slet, testing

    @FXML
    private HBox buttonArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addLoadedButtons();
        tvListener();
        loadTableView();
    }

    private void tvListener() {
        tvClients.setOnMouseClicked(event -> {
            if(isTvSelected()){
                deleteButton.setDisable(false);
                editButton.setDisable(false);
            }else {
                deleteButton.setDisable(true);
                editButton.setDisable(true);
            }
        });
    }

    private boolean isTvSelected() {
        return tvClients.getSelectionModel().getSelectedItem() != null;
    }


    private void loadTableView() {
        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        tcEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tcPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        try {
            tvClients.setItems(getModelsHandler().getClientModel().getAllClients());
        } catch (Exception e) {
            displayError(e);
        }
    }


    private void initializeButtonAccessLevels() {
        buttonAccessLevel = new NodeAccessLevel();

        addDeleteBtn();
        addEditBtn();

        buttonAccessLevel.addNodeAccessLevel(
                loadButton("➕💰 Add Client", ViewPaths.CREATE_CLIENTS_VIEW, clientView),
                Arrays.asList(SystemRole.Administrator, SystemRole.ProjectManager));
    }

    private void addEditBtn() {
        editButton = createButton("Edit User");
        buttonAccessLevel.addNodeAccessLevel(editButton,
                Arrays.asList(SystemRole.Administrator, SystemRole.ProjectManager));
        editButton.setDisable(true);

        editButton.setOnMouseClicked(event -> {
            FXMLLoader loader = loadView(ViewPaths.CREATE_CLIENTS_VIEW);
            CreateClientController controller = loader.getController();
            controller.setEditContent(tvClients.getSelectionModel().getSelectedItem());
            loadInMainView(loader.getRoot(), clientView);
        });
    }

    private void addDeleteBtn() {
        deleteButton = createButton("Delete User");
        buttonAccessLevel.addNodeAccessLevel(deleteButton,
                Arrays.asList(SystemRole.Administrator, SystemRole.ProjectManager));
        deleteButton.setDisable(true);

        deleteButton.setOnMouseClicked(event -> {
            Object user = tvClients.getSelectionModel().getSelectedItem();
            if(showQuestionDialog(user.toString(), true)){
                System.out.println("delete " + user.toString());
            }
        });
    }

    public void handleBack() {
        getMainController().mainBorderPane.setCenter(getMainController().getLastView());
    }

    private void addButton(Button button) {buttonArea.getChildren().add(0, button);}

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

    public void handleSearch(KeyEvent keyEvent) {
        try {
            getModelsHandler().getClientModel().search(txtfSearch.getText());
        } catch (Exception e) {
            displayError(e);
        }
    }
}
