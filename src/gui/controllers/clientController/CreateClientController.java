package gui.controllers.clientController;

import be.Client;
import be.Enum.SystemRole;
import be.SystemUser;
import com.jfoenix.controls.JFXButton;
import gui.controllers.BaseController;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CreateClientController extends BaseController {
    public TextField txtfName, txtfPhone, txtfEmail, txtfAddress, txtfCity, txtfPostalCode;
    public Label lblCreateUser;
    public HBox buttonArea;

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
        return true;
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
        selectedClient = selectedItem;
        //todo vi skal have lavet en location tabel for at kunne sætte det sidste information
    }

    private void addEditBtn() {
        JFXButton button = createButton("Confirm Edit");
        buttonArea.getChildren().add(0, button);

        button.setOnMouseClicked(event -> {
            if(isTextFieldInfoValid()) {
                Client client = bindClientInfo();//todo lav en update i dal osv..
                System.out.println("edit bro");
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

        return new Client(name, location,email, phone, "b2b");//todo type burde være en enum...
    }
}
