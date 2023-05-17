package gui.controllers.drawing;

import be.Device;
import be.DeviceType;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class DeviceCard implements Initializable {

    public ImageView imgView;
    public Label lbl;

    public Device getDevice() {
        return device;
    }

    private Device device;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setContent(Device device){
        //todo skal sætte finde billede og tekst fra device..
        Image img = new Image("images/WUAV_logo.jpg");
        imgView.setImage(img);

        lbl.setText("højtaler");

        this.device = device;
    }

    public void setTypeContent(DeviceType deviceType) {
        lbl.setText(deviceType.getName());
        imgView.setImage(new Image(deviceType.getImagePath()));
    }
}
