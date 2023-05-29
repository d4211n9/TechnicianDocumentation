package gui.controllers.clientController;

import be.Address;
import be.Client;
import be.Enum.ClientType;
import be.Enum.SystemRole;
import be.SystemUser;
import com.jfoenix.controls.JFXButton;
import gui.controllers.BaseController;
import gui.util.TaskExecutor;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import util.InputValidator;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CreateClientController extends BaseController implements Initializable {
    @FXML
    private TextField txtfName, txtfPhone, txtfEmail, txtfAddress, txtfCity, txtfPostalCode;
    @FXML
    private Label lblCreateUser;
    @FXML
    private HBox buttonArea;
    @FXML
    private JFXButton btnConfirm;
    @FXML
    private ComboBox<ClientType> cbType;

    Client selectedClient;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createComboBoxContent();
    }

    private void createComboBoxContent() {
        List<ClientType> clientTypes = new ArrayList<>();
        clientTypes.add(ClientType.Consumer);
        clientTypes.add(ClientType.Business);
        clientTypes.add(ClientType.Government);
        cbType.setItems(FXCollections.observableArrayList(clientTypes));
    }

    public void handleConfirm() {
        if(isTextFieldInfoValid()){ //TODO: fix input validation og brug isTextFieldInfoValid()

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
        System.out.println("navn "+InputValidator.isName(txtfName.getText()));
        System.out.println("email " + InputValidator.isEmail(txtfEmail.getText()));
        System.out.println("tlf " + InputValidator.isPhone(txtfPhone.getText()));
        System.out.println("adresse " + InputValidator.isStreet(txtfAddress.getText()));
        System.out.println("postnr " + InputValidator.isPostalCode(Integer.parseInt(txtfPostalCode.getText())));
        System.out.println("by " + InputValidator.isCity(txtfCity.getText()));

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
        cbType.getSelectionModel().select(selectedItem.getClientType());

        lblCreateUser.setText("Edit Customer");

        addEditBtn();
        buttonArea.getChildren().remove(btnConfirm);
        selectedClient = selectedItem;
    }

    private void addEditBtn() {
        JFXButton button = createButton("✔ Confirm Edit");
        buttonArea.getChildren().add(0, button);

        button.setOnMouseClicked(event -> {
            if(isTextFieldInfoValid()) { //TODO: fix input validation og brug isTextFieldInfoValid()
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

        ClientType clientType = cbType.getSelectionModel().getSelectedItem();

        Address address = null;
        try {
            address = getModelsHandler().getAddressModel().createAddress(new Address(street, postalCode, city));
        } catch (Exception e) {
            displayError(e);
        }

        return new Client(name, address, email, phone, clientType);
    }

    private Client bindClientID(Client client) {
        return new Client(selectedClient.getID(), client.getName(), client.getAddress(),
                client.getEmail(), client.getPhone(), client.getClientType());
    }
}
