package gui.controllers.drawing;

import be.Device;
import gui.controllers.BaseController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import util.DraggableMaker;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

public class DrawingController extends BaseController implements Initializable {


    public Button button;
    public Canvas canvas;
    public ScrollPane contentArea;
    public Label lbl;
    public VBox background;
    public Pane pane;
    public VBox sidebar;

    public ImageView selectedElementImg;
    private double mouseAnchorX;
    private double mouseAnchorY;

    DraggableMaker draggableMaker = new DraggableMaker();

    Node selectedelement;

    ArrayList<Device> devicesesOnDrawing;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        devicesesOnDrawing = new ArrayList<>();

                                                //todo test tænker vi skal køre det her igennem for hver element der skal tilføjes ovre i siden.
        Device device = new Device();           //todo make devices be. højde, brede, img, navn,
        FXMLLoader loader = loadView("/gui/views/drawing/deviceCard.fxml");

        Node deviceElement = loader.getRoot();
        sidebar.getChildren().add(deviceElement);

        DeviceCard controller = loader.getController();
        controller.setContent(device);


       addDeviceListener(loader);//checks and creates new devices if dragged into pane
}

    private void addDeviceListener(FXMLLoader loader) {
        Node deviceElement = loader.getRoot();
        deviceElement.setOnMousePressed(event -> {
            System.out.println("first step done");
            mouseAnchorX = event.getX();
            mouseAnchorY = event.getY();
            //todo lav listener om så den finder img fra det valgte og størelsen fra valgte device
            Image img = new Image("images/WUAV_logo.jpg");//todo skal komme fra devise i stedet
            ImageView imgview = new ImageView(img);
            selectedElementImg = imgview;
            selectedElementImg.setFitWidth(80);
            selectedElementImg.setFitHeight(80);

            deviceElement.setOnDragDetected(mouseEvent -> {
                pane.getChildren().add(selectedElementImg);
                System.out.println("du får revet i det du");
            });


            deviceElement.setOnMouseDragged(mouseEvent -> {
                selectedElementImg.setLayoutX(mouseEvent.getSceneX() - mouseAnchorX - background.getLayoutX());//todo skal også tage højde for hvor på pane man er
                selectedElementImg.setLayoutY(mouseEvent.getSceneY() - mouseAnchorY - background.getLayoutY());

                System.out.println(contentArea.getHvalue());
                //todo burde blive tilføjet en liste over devices hvis den bliver tilføjet til vores pane, så vi kan bruge det senere til device tappen

            });



            deviceElement.setOnMouseReleased(event1 -> {
                //todo tjek om musen er indenfor selve content pane, hvis ikke skal elementet slettes ellers skal resten af koden køres
                //todo lav elementerne draggable når man er færdig med at dragg
                DeviceCard controller = loader.getController();

                devicesesOnDrawing.add(controller.getDevice());
                DraggableMaker d = new DraggableMaker();
                d.makeDraggAble(selectedElementImg, pane);

                System.out.println("drag done you placed the item");
            });
        });
    }
}

