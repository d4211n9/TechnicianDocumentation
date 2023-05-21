package gui.controllers.drawing;

import be.Device;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class DeviceController {

    public ImageView imgView;
    public VBox vBox;
    private int index;

    private Device device;

    public void setContent(Device device){
        this.device = device;
        imgView.setImage(new Image(device.getDeviceType().getImagePath()));
    }

    public ImageView getImgView(){
        return imgView;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public void settingImgHeight(Double height){
        imgView.setFitHeight(height);
        vBox.setMinHeight(height);
    }
}
