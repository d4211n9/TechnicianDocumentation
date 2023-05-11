package gui.controllers.clientController;

import be.Client;
import be.Enum.SystemRole;
import be.Installation;
import be.Project;
import gui.controllers.TableViewController;
import gui.util.NodeAccessLevel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import util.ViewPaths;

import java.net.URL;
import java.sql.Timestamp;
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
        clientView.getChildren().add(addButtons());
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
        deleteButton = createButton("🗑 Delete Client");
        buttonAccessLevel.addNodeAccessLevel(deleteButton,
                Arrays.asList(SystemRole.Administrator, SystemRole.ProjectManager));
        deleteButton.setDisable(true);

        deleteButton.setOnMouseClicked(event -> {
            Client client = (Client) tableView.getSelectionModel().getSelectedItem();
            if(showQuestionDialog(client.toString(), true)){
                handleDelete(client);
            }
        });
    }

    private void handleDelete(Client client) {
        try {
            //Loop through each project from that client ...
            for(Project project : getModelsHandler().getProjectModel().getAllProjects()) {
                if(project.getClient().getID() == client.getID()) {

                    //.. to Soft Delete all installations from all projects
                    for(Installation installation : getModelsHandler().getInstallationModel().getAllInstallations(project.getID())) {
                        System.out.println(installation.getID() + " [INSTALLATION DELETED]");
                        System.out.println(getModelsHandler().getInstallationModel().getAllInstallations(project.getID()).size());
                        getModelsHandler().getInstallationModel().deleteInstallation(installation);
                    }

                    //... and then each project
                    System.out.println(project.getName() + " [PROJECT DELETED]");
                    getModelsHandler().getProjectModel().deleteProject(project);
                }
            }

            //Soft Delete the client
            getModelsHandler().getClientModel().deleteClient(client);

            handleSearch();
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
