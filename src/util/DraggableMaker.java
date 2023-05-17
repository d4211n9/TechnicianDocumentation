package util;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class DraggableMaker {

    private double mouseAnchorX;
    private double mouseAnchorY;

    public void makeDraggAble(Node node, Pane area){


        node.setOnMousePressed(mouseEvent -> {
            mouseAnchorX = mouseEvent.getX();
            mouseAnchorY = mouseEvent.getY();
        });

        //todo should check if the node is inside the content area before setting placement
        node.setOnMouseDragged(mouseEvent -> {
            double x = mouseEvent.getSceneX() - mouseAnchorX - area.getParent().getParent().getParent().getParent().getLayoutX();
            node.setLayoutX(x);
            node.setLayoutY(mouseEvent.getSceneY() - mouseAnchorY - area.getParent().getLayoutY());
        });
    }


}
