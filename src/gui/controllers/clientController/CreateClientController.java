package gui.controllers.clientController;

import be.Client;
import gui.controllers.BaseController;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class CreateClientController extends BaseController {
    public TextField txtfName, txtfPhone, txtfEmail, txtfAddress, txtfCity, txtfPostalCode;

    public void handleConfirm(ActionEvent actionEvent) {
        if(isTextFieldInfoValid()){
            String name = txtfName.getText();
            String email = txtfEmail.getText();
            String phone = txtfPhone.getText();

            String city = txtfCity.getText();
            String street = txtfAddress.getText();
            String postalCode = txtfPostalCode.getText();
            String location = street + " " +city + " " + postalCode;

            Client client = new Client(name, location,email, phone, "b2b");
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

    }
}
