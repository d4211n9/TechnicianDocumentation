package gui.controllers.projectControllers;

import be.Client;
import be.Project;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXButton;
import gui.controllers.BaseController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import util.ViewPaths;

import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

public class AddProjectController extends BaseController implements Initializable {
    @FXML
    private Label lblCreateProject;
    @FXML
    private HBox buttonArea;
    @FXML
    private JFXButton btnConfirm;
    @FXML
    private TextField txtfName, txtfStreet, txtfPostalCode, txtfCity, txtfSearch;
    @FXML
    private ComboBox<Client> cbClients;
    @FXML
    private JFXTextArea jfxTxtADescription;

    private int projectToEditId = -1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createComboBoxContent();
    }

    private void createComboBoxContent() {
        try {
            cbClients.setItems(getModelsHandler().getClientModel().getAllClients());
        } catch (Exception e) {
            displayError(e);
        }
    }

    public void handleCancel() {
        handleBack();
    }

    public void handleBack() {
        getMainController().mainBorderPane.setCenter(getMainController().getLastView());
    }

    public void handleConfirm() {
        if(validateInput()) {

            Project project = createProject();

            try {
                project = getModelsHandler().getProjectModel().createProject(project);
                getModelsHandler().getProjectModel().assignSystemUserToProject(project.getID(),
                        getModelsHandler().getSystemUserModel().getLoggedInSystemUser().getValue().getEmail());
                
                FXMLLoader loader = loadView(ViewPaths.PROJECT_INFO_VIEW);
                VBox projectInfoView = loader.getRoot();
                ProjectInfoController projectInfoController = loader.getController();
                projectInfoController.setContent(project);
                getMainController().mainBorderPane.setCenter(projectInfoView);
            } catch (Exception e) {
                displayError(e);
            }
        }
    }

    private Project createProject() {
        String name = txtfName.getText();
        Client client = (Client) cbClients.getSelectionModel().getSelectedItem();
        String street = txtfStreet.getText();
        String postalCode = txtfPostalCode.getText();
        String city = txtfCity.getText();
        String location = street + ", " + postalCode + " " + city;
        Date created = Calendar.getInstance().getTime();
        String description = jfxTxtADescription.getText();

        if (projectToEditId != -1) {
            return new Project(projectToEditId, name, client, location, created, description);
        }

        return new Project(name, client, location, created, description);
    }

    //TODO, valider input
    private boolean validateInput() {
        return true;
    }

    public void handleAddClient() {
    }

    public void handleSearch() {
        try {
            getModelsHandler().getClientModel().search(txtfSearch.getText());
            cbClients.setItems(getModelsHandler().getClientModel().getAllClients());
            if(cbClients.getItems().size() == 1) {
                cbClients.getSelectionModel().select(0);
            }
        } catch (Exception e) {
            displayError(e);
        }
    }

    public void setEditContent(Project selectedItem) {
        projectToEditId = selectedItem.getID();

        lblCreateProject.setText("Edit Project");
        buttonArea.getChildren().remove(btnConfirm);

        txtfName.setText(selectedItem.getName());
        txtfSearch.setText(selectedItem.getClient().getName());
        cbClients.getSelectionModel().select(selectedItem.getClient());
        //todo lav nu de locations

        addEditBtn();
    }

    private void addEditBtn() {
        JFXButton button = createButton("✔ Confirm Edit");
        buttonArea.getChildren().add(0, button);

        button.setOnMouseClicked(event -> {
            if(validateInput()) {
                Project project = createProject();

                try {
                    getModelsHandler().getProjectModel().updateProject(project);

                    handleCancel();
                }
                catch (Exception e) {
                    displayError(e);
                }
            }
        });
    }
}
