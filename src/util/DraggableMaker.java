package util;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class DraggableMaker {
    private double mouseAnchorX;
    private double mouseAnchorY;

    public void makeDraggAble(Node node, Pane area) {

        node.setOnMousePressed(mouseEvent -> {
            mouseAnchorX = mouseEvent.getX();
            mouseAnchorY = mouseEvent.getY();
        });

        node.setOnMouseDragged(mouseEvent -> {
            double x = mouseEvent.getSceneX() - mouseAnchorX - area.getParent().getParent().getParent().getParent().getLayoutX();
            node.setLayoutX(x);
            node.setLayoutY(mouseEvent.getSceneY() - mouseAnchorY - area.getParent().getLayoutY());
        });
    }
}
