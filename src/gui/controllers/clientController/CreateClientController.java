package gui.controllers.clientController;

import be.Address;
import be.Client;
import be.Enum.SystemRole;
import be.SystemUser;
import com.jfoenix.controls.JFXButton;
import gui.controllers.BaseController;
import gui.util.TaskExecutor;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
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

    public void handleConfirm() {
        if(isTextFieldInfoValid()){

            Client client = bindClientInfo();

            createClient(client);
        }
    }

    private void createClient(Client client) {
        try {
            Task<Client> createClientTask = getModelsHandler()
                    .getClientModel()
                    .createClient(client);

            createClientTask.setOnFailed(event -> displayError(createClientTask.getException()));

            TaskExecutor.executeTask(createClientTask);

            handleCancel();
        } catch (Exception e) {
            displayError(e);
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
        txtfAddress.setText(selectedItem.getAddress().getStreet());
        txtfPostalCode.setText(selectedItem.getAddress().getPostalCode());
        txtfCity.setText(selectedItem.getAddress().getCity());

        lblCreateUser.setText("Edit Customer");

        addEditBtn();
        buttonArea.getChildren().remove(btnConfirm);
        selectedClient = selectedItem;
    }

    private void addEditBtn() {
        JFXButton button = createButton("✔ Confirm Edit");
        buttonArea.getChildren().add(0, button);

        button.setOnMouseClicked(event -> {
            if(isTextFieldInfoValid()) {
                Client client = bindClientInfo();
                client = bindClientID(client);

                updateClient(client);
            }
        });
    }

    private void updateClient(Client client) {
        try {
            Task<Boolean> updateClientTask = getModelsHandler()
                    .getClientModel()
                    .updateClient(client, selectedClient);

            updateClientTask
                    .setOnFailed(failedEvent -> displayError(updateClientTask.getException()));

            TaskExecutor.executeTask(updateClientTask);

            handleCancel();
        } catch (Exception e) {
            displayError(e);
        }
    }

    private Client bindClientInfo(){
        String name = txtfName.getText();
        String email = txtfEmail.getText();
        String phone = txtfPhone.getText();

        String city = txtfCity.getText();
        String street = txtfAddress.getText();
        String postalCode = txtfPostalCode.getText();

        Address address = null;
        try {
            address = getModelsHandler().getAddressModel().createAddress(new Address(street, postalCode, city));
        } catch (Exception e) {
            displayError(e);
        }

        return new Client(name, address, email, phone, "b2b");
        //TODO type burde være en enum...
        //TODO Type skal også tilføjes til .fxml så man kan vælge ved create og edit
    }

    private Client bindClientID(Client client) {
        return new Client(selectedClient.getID(), client.getName(), client.getAddress(),
                client.getEmail(), client.getPhone(), "b2b");
    }
}
