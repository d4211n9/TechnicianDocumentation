package gui.controllers.projectControllers;

import be.Client;
import be.Project;
import com.jfoenix.controls.JFXTextArea;
import gui.controllers.BaseController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import util.ViewPaths;

import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

public class AddProjectController extends BaseController implements Initializable {
    @FXML
    private TextField txtfName, txtfStreet, txtfPostalCode, txtfCity, txtfSearch;
    @FXML
    private ComboBox cbClients;
    @FXML
    private JFXTextArea jfxTxtADescription;

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
            String name = txtfName.getText();
            Client client = (Client) cbClients.getSelectionModel().getSelectedItem();
            String street = txtfStreet.getText();
            String postalCode = txtfPostalCode.getText();
            String city = txtfCity.getText();
            String location = street + ", " + postalCode + " " + city;
            Date created = Calendar.getInstance().getTime();
            String description = jfxTxtADescription.getText();
            Project project = new Project(name, client, location, created, description);

            try {
                getModelsHandler().getProjectModel().createProject(project);
                getMainController().mainBorderPane.setCenter(loadView(ViewPaths.PROJECTS_VIEW).getRoot());
                //TODO Åben det nye projekt der er oprettet, når vi har view til det...
                getMainController().saveLastView(null);
            } catch (Exception e) {
                displayError(e);
            }
        }
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
}
