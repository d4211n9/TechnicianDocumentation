package gui.controllers.clientController;

import be.Client;
import be.Enum.SystemRole;
import be.SystemUser;
import com.jfoenix.controls.JFXButton;
import gui.controllers.BaseController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import util.InputValidator;

public class CreateClientController extends BaseController {
    @FXML
    private TextField txtfName, txtfPhone, txtfEmail, txtfAddress, txtfCity, txtfPostalCode;
    @FXML
    private Label lblCreateUser;
    @FXML
    private HBox buttonArea;
    @FXML
    private JFXButton btnConfirm;

    Client selectedClient;

    public void handleConfirm(ActionEvent actionEvent) {
        if(isTextFieldInfoValid()){

            Client client = bindClientInfo();
            try {
                getModelsHandler().getClientModel().createClient(client);
                handleCancel();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean isTextFieldInfoValid() {
        return InputValidator.isName(txtfName.getText()) &&
                InputValidator.isEmail(txtfEmail.getText()) &&
                InputValidator.isPhone(txtfPhone.getText()) &&
                InputValidator.isStreet(txtfAddress.getText()) &&
                InputValidator.isPostalCode(Integer.parseInt(txtfPostalCode.getText())) &&
                InputValidator.isCity(txtfCity.getText());
    }

    public void handleCancel() {
        getMainController().mainBorderPane.setCenter(getMainController().getLastView());
    }

    public void handleBack(ActionEvent actionEvent) {
        getMainController().mainBorderPane.setCenter(getMainController().getLastView());
    }

    public void setEditContent(Client selectedItem) {

        txtfName.setText(selectedItem.getName());
        txtfEmail.setText(selectedItem.getEmail());
        txtfPhone.setText(selectedItem.getPhone());

        lblCreateUser.setText("Edit Client");

        addEditBtn();
        buttonArea.getChildren().remove(btnConfirm);
        selectedClient = selectedItem;
        //todo vi skal have lavet en location tabel for at kunne sætte det sidste information
    }

    private void addEditBtn() {
        JFXButton button = createButton("Confirm Edit");
        buttonArea.getChildren().add(0, button);

        button.setOnMouseClicked(event -> {
            if(isTextFieldInfoValid()) {
                Client client = bindClientInfo();
                client = bindClientID(client);
                try {
                    getModelsHandler().getClientModel().updateClient(client, selectedClient);
                    handleCancel();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    private Client bindClientInfo(){
        String name = txtfName.getText();
        String email = txtfEmail.getText();
        String phone = txtfPhone.getText();

        String city = txtfCity.getText();
        String street = txtfAddress.getText();
        String postalCode = txtfPostalCode.getText();
        String location = street + " " +city + " " + postalCode;

        return new Client(name, location,email, phone, "b2b");
        //TODO type burde være en enum...
        //TODO Type skal også tilføjes til .fxml så man kan vælge ved create og edit
    }

    private Client bindClientID(Client client) {
        return new Client(selectedClient.getID(), client.getName(), client.getLocation(),
                client.getEmail(), client.getPhone(), "b2b");
    }
}
