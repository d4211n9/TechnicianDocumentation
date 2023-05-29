package gui.controllers.clientController;

import be.Client;
import be.Enum.SystemRole;
import gui.controllers.TableViewController;
import gui.util.NodeAccessLevel;
import gui.util.TaskExecutor;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import util.ViewPaths;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ClientController extends TableViewController implements Initializable {

    @FXML
    private TableColumn<Client, String> tcName, tcEmail, tcPhone, tcStreet, tcPostalCode, tcCity, tcType;
    @FXML
    private VBox clientView;
    @FXML
    private TextField txtfSearch;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadTableView();
        initializeButtonAccessLevels();
        clientView.getChildren().add(addButtons());
        tvListener();

        clientBackgroundUpdate();//should be last in initialize
    }

    private void clientBackgroundUpdate() {
        try {
            List<Runnable> backgroundUpdateList = new ArrayList<>();
            backgroundUpdateList.add(getModelsHandler().getClientModel());
            backgroundUpdate(backgroundUpdateList);
        } catch (Exception e) {
            displayError(e);
        }
    }

    private void loadTableView() {
        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcStreet.setCellValueFactory(new PropertyValueFactory<>("street"));
        tcPostalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        tcCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        tcEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tcPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        tcType.setCellValueFactory(new PropertyValueFactory<>("type"));

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
                loadButton("➕👨‍💼 Add", ViewPaths.CREATE_CLIENTS_VIEW, clientView),
                Arrays.asList(SystemRole.Administrator, SystemRole.ProjectManager));
    }

    private void addEditBtn() {
        editButton = createButton("✏ Edit");
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
        deleteButton = createButton("🗑 Delete");
        buttonAccessLevel.addNodeAccessLevel(deleteButton,
                Arrays.asList(SystemRole.Administrator, SystemRole.ProjectManager));
        deleteButton.setDisable(true);

        deleteButton.setOnMouseClicked(event -> {
            Client client = (Client) tableView.getSelectionModel().getSelectedItem();
            if(showQuestionDialog(client.toString(), true)){//todo burde lige advarer hvis der stadigt er projekter der er i gang værende på projektet
                handleDelete(client);
            }
        });
    }

    private void handleDelete(Client client) {
        try {
            Task<Void> deleteClientTask = getModelsHandler().getClientModel().deleteClient(client);

            deleteClientTask.setOnSucceeded(event -> handleSearch());
            deleteClientTask.setOnFailed(event -> displayError(deleteClientTask.getException()));

            TaskExecutor.executeTask(deleteClientTask);
        } catch (Exception e) {
            displayError(e);
        }
    }

    public void handleBack() {
        getMainController().mainBorderPane.setCenter(getMainController().getLastView());
    }

    public void handleSearch() {
        try {
            getModelsHandler().getClientModel().search(txtfSearch.getText());
        } catch (Exception e) {
            displayError(e);
        }
    }
}
