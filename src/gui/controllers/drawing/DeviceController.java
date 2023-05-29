package gui.controllers.drawing;

import be.Device;
import com.jfoenix.controls.JFXButton;
import gui.controllers.BaseController;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DeviceController extends BaseController {

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

    public List<Node> getInfoNodes(ImageView source, DrawingController drawingController){
        List<Node> infoNodes;
        infoNodes = new ArrayList<>();

        Label name = new Label(device.getDeviceType().getName());
        infoNodes.add(name);

        //creates the pos x filed
        Label label = new Label("PosX   ");
        TextField txtFiled = new TextField();
        txtFiled.setText(String.valueOf(device.getPosX()));
        txtFiled.textProperty().addListener((obs, oldVal, newVal) -> {
            if(!Objects.equals(newVal, "")){
                device.setPosX(Double.parseDouble(newVal));
                imgView.setTranslateX(Double.parseDouble(newVal));
            }
        });
        HBox hbox = new HBox(label, txtFiled);
        hbox.setSpacing(10);
        infoNodes.add(hbox);

        //creates the pos Y filed
        Label posY = new Label("PosY   ");
        TextField txtField = new TextField();
        txtField.setText(String.valueOf(device.getPosY()));
        txtField.textProperty().addListener((obs, oldVal, newVal) -> {
            if(!Objects.equals(newVal, "")){
                device.setPosY(Double.parseDouble(newVal));
                imgView.setTranslateY(Double.parseDouble(newVal));
            }
        });
        HBox hboxPosY = new HBox(posY, txtField);
        hboxPosY.setSpacing(10);
        infoNodes.add(hboxPosY);


        Label lblHeight = new Label("Height");
        TextField txtFieldHeight = new TextField();
        txtFieldHeight.setText(String.valueOf(device.getHeight()));
        txtFieldHeight.textProperty().addListener((obs, oldVal, newVal) -> {
            if(!Objects.equals(newVal, "")){
                device.setHeight(Double.parseDouble(newVal));
                settingImgHeight(Double.valueOf(newVal));
                //source.getParent().prefHeight(Double.parseDouble(newVal));
            }
        });
        HBox hboxHeight = new HBox(lblHeight, txtFieldHeight);
        hboxHeight.setSpacing(10);
        infoNodes.add(hboxHeight);

        Label lblWidth = new Label("Width ");
        TextField txtFieldWidth = new TextField();
        txtFieldWidth.setText(String.valueOf(device.getWidth()));
        txtFieldWidth.textProperty().addListener((obs, oldVal, newVal) -> {
            if(!Objects.equals(newVal, "")){
                device.setWidth(Double.parseDouble(newVal));
                settingImgHeight(Double.valueOf(newVal));
                source.getParent().prefWidth(Double.parseDouble(newVal));
            }
        });
        HBox hboxWidth = new HBox(lblWidth, txtFieldWidth);
        hboxWidth.setSpacing(10);
        infoNodes.add(hboxWidth);

        JFXButton deleteBtn = drawingController.createButton("delete");
        deleteBtn.setOnMouseClicked(event -> {
            drawingController.pane.getChildren().remove(source);
            drawingController.devicesOnDrawing.remove(getDevice());
            try {
                drawingController.save();
            } catch (Exception e) {
                displayError(e);
            }
            drawingController.objectInfo.getChildren().clear();
        });
        infoNodes.add(deleteBtn);
        return infoNodes;
    }
}
