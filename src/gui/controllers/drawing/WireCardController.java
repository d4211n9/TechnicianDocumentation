package gui.controllers.drawing;

import be.WireType;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;

public class WireCardController {
    public Label color;
    public Label name;


    public void setContent(WireType wireType){
        color.setBackground(Background.fill(wireType.getColor()));
        name.setText(wireType.getName());
    }
}
