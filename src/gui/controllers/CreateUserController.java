package gui.controllers;

import be.Enum.SystemRole;
import be.SystemUser;
import gui.util.AutoCompleteBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateUserController extends BaseController implements Initializable {


    @FXML
    private VBox loginInformation;
    @FXML
    private ComboBox<SystemRole> cbRoles;
    @FXML
    private TextField txtfConfirmPassword, txtfName, txtfEmail, txtfPassword;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            cbRoles.setItems(getModelsHandler().getSystemUserModel().getAllRoles());
        } catch (Exception e) {
            displayError(e);
        }

        new AutoCompleteBox(cbRoles);
    }


    public void handleBack() {
        getMainController().mainBorderPane.setCenter(getMainController().getLastView());
    }
    public void handleCancel() {
        handleBack();
    }

    public void handleConfirm(ActionEvent actionEvent) {


    }

    private SystemUser createSystemUserFromFields() {

        String name = txtfName.getText();
        SystemRole role = cbRoles.getValue();
        String email = txtfEmail.getText();
        String password = txtfPassword.getText();

        return new SystemUser(password, name, role, email);
    }
}
