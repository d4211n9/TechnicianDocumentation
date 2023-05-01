package gui.controllers;

import be.Enum.SystemRole;
import be.SystemUser;
import gui.util.AutoCompleteBox;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import util.InputValidator;

import java.io.ObjectInputValidation;
import java.net.URL;
import java.util.ResourceBundle;

public class CreateUserController extends BaseController implements Initializable {


    public VBox loginInformation;
    public ComboBox<SystemRole> cbRoles;
    public TextField txtfConfirmPassword, txtfName, txtfEmail, txtfPassword;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            cbRoles.setItems(getModelsHandler().getSystemUserModel().getAllRoles());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        AutoCompleteBox a = new AutoCompleteBox(cbRoles);
    }


    public void handleBack() {
        getMainController().mainBorderPane.setCenter(getMainController().getLastView());
    }
    public void handleCancel() {
        handleBack();
    }

    public void handleConfirm(ActionEvent actionEvent) {
        SystemUser user = createSystemUserFromFields();
        if(user != null){
            try {
                SystemUser s = getModelsHandler().getSystemUserModel().createSystemUser(createSystemUserFromFields());

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    private SystemUser createSystemUserFromFields() {
        if(validateInput()){
            String name = txtfName.getText();
            int role = cbRoles.getSelectionModel().getSelectedIndex();
            String email = txtfEmail.getText();
            String password = txtfPassword.getText();

            return new SystemUser(email, password, SystemRole.getRole(String.valueOf(cbRoles.getItems().get(role))), name);
        }
        return null;
    }

    private boolean validateInput() {
        return InputValidator.isEmail(txtfEmail.getText()) &&
                InputValidator.isName(txtfName.getText()) &&
                InputValidator.isPassword(txtfPassword.getText()) &&
                isPasswordSame();
    }

    private boolean isPasswordSame(){

        return txtfPassword.getText().equals(txtfConfirmPassword.getText());
    }
}
