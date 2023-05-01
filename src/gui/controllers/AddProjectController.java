package gui.controllers;

import be.Client;
import be.Project;
import com.jfoenix.controls.JFXButton;
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
    private TextField txtfName, txtfStreet, txtfPostalCode, txtfCity;
    @FXML
    private ComboBox cbRoles;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createComboBoxContent();
    }

    private void createComboBoxContent() {
        try {
            cbRoles.setItems(getModelsHandler().getClientModel().getAllClients());
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
            Client client = (Client) cbRoles.getSelectionModel().getSelectedItem();
            String street = txtfStreet.getText();
            String postalCode = txtfPostalCode.getText();
            String city = txtfCity.getText();
            String location = street + ", " + postalCode + " " + city;
            Date created = Calendar.getInstance().getTime();
            Project project = new Project(name, client, location, created);

            try {
                getModelsHandler().getProjectModel().createProject(project);
                getMainController().mainBorderPane.setCenter(loadView(ViewPaths.PROJECTS_VIEW).getRoot());
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
}
