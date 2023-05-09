package gui.controllers.clientController;

import be.Client;
import be.Enum.SystemRole;
import gui.controllers.TableViewController;
import gui.util.NodeAccessLevel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import util.ViewPaths;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class ClientController extends TableViewController implements Initializable {

    @FXML
    private TableColumn<Client, String> tcName, tcLocation, tcEmail, tcPhone;
    @FXML
    private VBox clientView;
    @FXML
    private TextField txtfSearch;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadTableView();
        initializeButtonAccessLevels();
        addLoadedButtons();
        tvListener();
    }

    private void loadTableView() {
        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        tcEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tcPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        try {
            tableView.setItems(getModelsHandler().getClientModel().getAllClients());
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
        editButton = createButton("✏ Edit Client");
        buttonAccessLevel.addNodeAccessLevel(editButton,
                Arrays.asList(SystemRole.Administrator, SystemRole.ProjectManager));
        editButton.setDisable(true);

        editButton.setOnMouseClicked(event -> {
            FXMLLoader loader = loadView(ViewPaths.CREATE_CLIENTS_VIEW);
            CreateClientController controller = loader.getController();
            controller.setEditContent((Client) tableView.getSelectionModel().getSelectedItem());
            loadInMainView(loader.getRoot(), clientView);
        });
    }

    private void addDeleteBtn() {
        deleteButton = createButton("🗑 Delete User");
        buttonAccessLevel.addNodeAccessLevel(deleteButton,
                Arrays.asList(SystemRole.Administrator, SystemRole.ProjectManager));
        deleteButton.setDisable(true);

        deleteButton.setOnMouseClicked(event -> {
            Object user = tableView.getSelectionModel().getSelectedItem();
            if(showQuestionDialog(user.toString(), true)){
                System.out.println("delete " + user.toString());
                //TODO Delete ned i lagene
            }
        });
    }

    public void handleBack() {
        getMainController().mainBorderPane.setCenter(getMainController().getLastView());
    }

    public void handleSearch(KeyEvent keyEvent) {
        try {
            getModelsHandler().getClientModel().search(txtfSearch.getText());
        } catch (Exception e) {
            displayError(e);
        }
    }
}
