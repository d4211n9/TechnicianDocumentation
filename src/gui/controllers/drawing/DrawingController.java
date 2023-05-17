package gui.controllers.drawing;

import be.Device;
import be.DeviceType;
import gui.controllers.BaseController;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import util.ViewPaths;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DrawingController extends BaseController implements Initializable {


    public Button button;
    public Canvas canvas;
    public ScrollPane contentArea;
    public Label lbl;
    public VBox background;
    public Pane pane;

    DataFormat dataFormat = new DataFormat("DragDropFormat");

    public ImageView selectedElementImg;
    public VBox sidebarDevice;
    ArrayList<Device> devicesesOnDrawing;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadDeviceTypes();
        devicesesOnDrawing = new ArrayList<>();
    }

    public void loadDeviceTypes() {
        sidebarDevice.getChildren().clear();
        try {
            for(DeviceType deviceType : getModelsHandler().getDrawingModel().getAllDeviceTypes()) {
                FXMLLoader loader = loadView(ViewPaths.DEVICE_CARD);
                DeviceCard controller = loader.getController();
                controller.setTypeContent(deviceType);

                HBox deviceCard = loader.getRoot();
                sidebarDevice.getChildren().add(0, deviceCard);
                addDeviceCardListener(loader, deviceType);
            }
        } catch (Exception e) {
            displayError(e);
        }
    }

    private void addDeviceCardListener(FXMLLoader loader, DeviceType deviceType) {
        Node deviceElement = loader.getRoot();

        deviceElement.setOnMousePressed(event -> {
            Device d = new Device(deviceType, devicesesOnDrawing.size());

            ImageView imgview = new ImageView(new Image(d.getDeviceType().getImagePath()));


            selectedElementImg = imgview;
            selectedElementImg.setFitWidth(80);
            selectedElementImg.setFitHeight(80);

            Tooltip imgName = new Tooltip(deviceType.getName());
            imgName.setShowDelay(Duration.millis(200));
            Tooltip.install(selectedElementImg, imgName);

            selectedElementImg.setLayoutX(-100);
            selectedElementImg.setLayoutY(-100);

            pane.getChildren().add(selectedElementImg);
            problem(selectedElementImg, contentArea, pane, dataFormat, d, deviceElement);
            DeviceCard controller = loader.getController();
            problem(selectedElementImg, contentArea, pane, dataFormat, d, selectedElementImg);
            devicesesOnDrawing.add(controller.getDevice());
        });
    }

    public void handleAddDevice() {
        FXMLLoader loader = openStage(ViewPaths.CREATE_DEVICE, "Create Device");
        AddDeviceController addDeviceController = loader.getController();
        addDeviceController.setDrawingController(this);
    }

    public static void problem(Node node, ScrollPane scrollPane, Pane pane, DataFormat dataFormat, Device d, Node card){
        card.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Dragboard db = node.startDragAndDrop(TransferMode.ANY);
                ClipboardContent content = new ClipboardContent();
                content.put(dataFormat, d.getId()); // normally, ID of node
                db.setContent(content);
                event.consume();
            }
        });

        pane.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                if(db.hasContent(dataFormat) && db.getContent(dataFormat) instanceof Integer){
                    int index = (Integer) db.getContent(dataFormat);
                    Node node = (Node) pane.getChildren().get(index);
                    node.setManaged(false);
                    // this is the problematic part
                    node.setTranslateX(event.getX() - node.getLayoutX() - (node.getBoundsInParent().getHeight() / 2));
                    node.setTranslateY(event.getY() - node.getLayoutY() - (node.getBoundsInParent().getWidth() / 2));

                    event.setDropCompleted(true);
                    event.consume();
                }
            }
        });

        scrollPane.setOnDragOver(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                event.acceptTransferModes(TransferMode.ANY);
                event.consume();
            }
        });
    }


}

