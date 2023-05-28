package gui.controllers.drawing;

import be.WireType;
import com.jfoenix.controls.JFXButton;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;

public class WireCardController {
    public Label color;
    public JFXButton name;


    public void setContent(WireType wireType){
        color.setBackground(Background.fill(wireType.getColor()));
        name.setText(wireType.getName());
    }


    public Label getColor() {
        return color;
    }

    public void setColor(Label color) {
        this.color = color;
    }

    public JFXButton getName() {
        return name;
    }

    public void setName(JFXButton name) {
        this.name = name;
    }
}
