package gui.controllers;

import gui.util.AutoCompleteBox;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateUserController extends BaseController implements Initializable {


    public VBox loginInformation;
    public ComboBox cbRoles;


    public void handleBack(ActionEvent actionEvent) {
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {//todo test content 
            cbRoles.setItems(getModelsHandler().getSystemUserModel().getAllUsers());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        AutoCompleteBox a = new AutoCompleteBox(cbRoles);
    }
}
