package gui.controllers.drawing;

import be.Device;
import be.DeviceType;
import gui.controllers.BaseController;
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
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
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
    private Line currentLine;
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

    public void loadDeviceInPane(Device device){

        ImageView deviceImg = new ImageView(new Image(device.getDeviceType().getImagePath()));

        deviceImg.setFitHeight(device.getHeight());
        deviceImg.setFitWidth(device.getWidth());

        deviceImg.setLayoutX(device.getPosX());
        deviceImg.setLayoutY(device.getWidth());

        Tooltip imgName = new Tooltip(device.getDeviceType().getName());
        imgName.setShowDelay(Duration.millis(200));
        Tooltip.install(deviceImg, imgName);

        pane.getChildren().add(deviceImg);
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

    public void handleAddLine() {
        pane.setOnMousePressed(e -> {
            currentLine = new Line(e.getX(), e.getY(), e.getX(), e.getY());
            pane.getChildren().add(currentLine);
        });

        pane.setOnMouseDragged(e -> {
            currentLine.setEndX(e.getX());
            currentLine.setEndY(e.getY());
        });
    }

    public static void problem(Node node, ScrollPane scrollPane, Pane pane, DataFormat dataFormat, Device d, Node card) {
        card.setOnDragDetected(event -> {
            Dragboard db = node.startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.put(dataFormat, d.getId()); // normally, ID of node
            db.setContent(content);
            event.consume();
        });

        pane.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            if(db.hasContent(dataFormat) && db.getContent(dataFormat) instanceof Integer){
                int index = (Integer) db.getContent(dataFormat);
                Node node1 = (Node) pane.getChildren().get(index);
                node1.setManaged(false);
                // this is the problematic part
                node1.setTranslateX(event.getX() - node1.getLayoutX() - (node1.getBoundsInParent().getHeight() / 2));
                node1.setTranslateY(event.getY() - node1.getLayoutY() - (node1.getBoundsInParent().getWidth() / 2));

                event.setDropCompleted(true);
                event.consume();
            }
        });

        scrollPane.setOnDragOver(event -> {
            event.acceptTransferModes(TransferMode.ANY);
            event.consume();
        });
    }
}

