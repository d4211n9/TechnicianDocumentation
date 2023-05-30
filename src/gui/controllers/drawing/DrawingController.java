package gui.controllers.drawing;

import be.*;
import com.jfoenix.controls.JFXButton;
import gui.controllers.BaseController;
import gui.util.TaskExecutor;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.util.Duration;
import util.ViewPaths;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DrawingController extends BaseController implements Initializable {


    public VBox sideBarWires;
    @FXML
    private VBox background;
    @FXML
    VBox objectInfo;
    @FXML
    private VBox sidebarDevice;
    @FXML
    private Button button;
    @FXML
    private ScrollPane contentArea;
    @FXML
    Pane pane;
    @FXML
    private ImageView selectedElementImg;

    private DeviceController selectedDevice;
    private Node source;
    private Line currentLine;
    ArrayList<Device> devicesOnDrawing;

    ArrayList<Wire> wiresOnDrawing;
    private Installation installation;

    public int index = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadDeviceTypes();
        loadWireTypes();

        try {
            devicesOnDrawing = new ArrayList<>();
            wiresOnDrawing = new ArrayList<>();
        } catch (Exception e) {
            displayError(e);
        }
    }

    /**
     * loads all devices and wires into the pane
     */
    public void setInstallation(Installation installation) {
        this.installation = installation;
        try {
           addAllDevicesToPane();
           addAllWiresToPane();
        } catch (Exception e) {
            displayError(e);
        }
    }

    private void addAllWiresToPane() throws Exception {
        Task<ObservableList<Wire>> allWiresTask = getModelsHandler()
                .getDrawingModel()
                .getWiresFromInstallation(installation.getID());

        allWiresTask.valueProperty().addListener((observable, oldValue, newValue) ->  {
            for(Wire wire : newValue) {
                wiresOnDrawing.add(wire);
                try {
                    addWireToPane(wire);
                } catch (Exception e) {
                    displayError(e);
                }
            }
        });
        TaskExecutor.executeTask(allWiresTask);
    }

    private void addAllDevicesToPane() throws Exception {
        devicesOnDrawing.clear();
        pane.getChildren().clear();

        Task<ObservableList<Device>> allDevicesTask = getModelsHandler()
                .getDrawingModel()
                .getDevicesFromInstallation(installation.getID());

        allDevicesTask.valueProperty().addListener((observable, oldValue, newValue) ->  {

            for(Device device : newValue) {
                devicesOnDrawing.add(device);
                try {
                    loadDeviceInPane(device);
                } catch (Exception e) {
                    displayError(e);
                }
            }
        });
        TaskExecutor.executeTask(allDevicesTask);

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

    public void loadWireTypes() {
        sideBarWires.getChildren().clear();

        try {
            for(WireType wireType : getModelsHandler().getDrawingModel().getAllWireTypes()) {
                FXMLLoader loader = loadView(ViewPaths.WIRE_CARD_VIEW);
                WireCardController controller = loader.getController();
                controller.setContent(wireType);

                HBox wireCard = loader.getRoot();
                sideBarWires.getChildren().add(0, wireCard);
                handleAddLine(loader, wireType);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void loadDeviceInPane(Device device) throws Exception {
        FXMLLoader loader = loadView("/gui/views/drawing/DeviceView.fxml");
        DeviceController controller = loader.getController();
        controller.setContent(device);

        ImageView deviceImg = createDeviceImage(controller, device);

        Tooltip imgName = new Tooltip(device.getDeviceType().getName());
        imgName.setShowDelay(Duration.millis(200));
        Tooltip.install(deviceImg, imgName);
        device.setId(getIndex());

        makeDraggable(deviceImg, contentArea, pane, getModelsHandler().getDrawingModel().getDataFormat(), device, deviceImg);

        pane.getChildren().add(deviceImg);
    }

    private ImageView createDeviceImage(DeviceController controller, Device device) {
        ImageView deviceImg = controller.getImgView();

        deviceImg.setOnMousePressed(event -> {
            selectedDevice = controller;
            showDeviceInfo(selectedDevice);
            source = (Node)event.getPickResult().getIntersectedNode();
        });
        selectedDevice = controller;

        deviceImg.setFitHeight(device.getHeight());
        deviceImg.setFitWidth(device.getWidth());

        deviceImg.setTranslateX(device.getPosX());
        deviceImg.setTranslateY(device.getPosY());
        return deviceImg;
    }

    private void addWireToPane(Wire wire) {
        currentLine = new Line(wire.getStartX(), wire.getStartY(), wire.getEndX(), wire.getEndY());
        currentLine.setStrokeWidth(5.0);
        currentLine.setStroke(wire.getWireType().getColor());

        pane.getChildren().add(currentLine);
        selectLineListener(wire);
    }


    private void addDeviceCardListener(FXMLLoader loader, DeviceType deviceType) {
        Node deviceElement = loader.getRoot();

        deviceElement.setOnMousePressed(event -> {
            Device d = new Device(deviceType, getIndex());
            FXMLLoader loader1 = loadView(ViewPaths.DEVICE_VIEW);
            DeviceController controller1 = loader1.getController();
            controller1.setContent(d);

            ImageView imgview = controller1.getImgView();
            imgview.setOnMousePressed(event1 -> {
                selectedDevice = controller1;
                showDeviceInfo(controller1);
                source = (Node)event1.getPickResult().getIntersectedNode();
            });
            selectedDevice = controller1;

            d.setHeight(100);
            d.setWidth(100);
            selectedElementImg = imgview;
            selectedElementImg.setFitWidth(d.getHeight());
            selectedElementImg.setFitHeight(d.getWidth());
            Tooltip imgName = new Tooltip(deviceType.getName());
            imgName.setShowDelay(Duration.millis(200));
            Tooltip.install(selectedElementImg, imgName);

            selectedElementImg.setLayoutX(-100);
            selectedElementImg.setLayoutY(-100);

            pane.getChildren().add(selectedElementImg);
            try {
                makeDraggable(selectedElementImg, contentArea, pane, getModelsHandler().getDrawingModel().getDataFormat(), d, deviceElement);

                devicesOnDrawing.add(d);
                getModelsHandler().getDrawingModel().addDeviceToInstallation(d, installation.getID());

                makeDraggable(selectedElementImg, contentArea, pane, getModelsHandler().getDrawingModel().getDataFormat(), d, selectedElementImg);
            } catch (Exception e) {
                displayError(e);
            }
        });
    }

    private void showDeviceInfo(DeviceController controller){//todo maybe it could be moved to other controller
       List<Node> infoNodes = controller.getInfoNodes((ImageView) source, this);
        objectInfo.getChildren().clear();
        for (Node infoNode: infoNodes) {
            objectInfo.getChildren().add(infoNode);
        }
    }

    public void handleAddDevice() {
        FXMLLoader loader = openStage(ViewPaths.CREATE_DEVICE, "Create Device");
        AddDeviceController addDeviceController = loader.getController();
        addDeviceController.setDrawingController(this);
    }

    public void handleAddLine(FXMLLoader loader, WireType wireType) {
        Node wireCard = loader.getRoot();
        wireCard.setOnMousePressed(event -> {
           addLineListener(wireType);
        });

    }

    private void addLineListener(WireType wireType) {
        pane.setOnMousePressed(e -> {//sets the starting point of the wire
            source = (Node)e.getPickResult().getIntersectedNode();
            if(!source.equals(pane)) {
                currentLine = new Line(e.getX(), e.getY(), e.getX(), e.getY());
                currentLine.setStrokeWidth(5.0);
                currentLine.setStroke(wireType.getColor());
                pane.getChildren().add(currentLine);

                pane.setOnMousePressed(event1 -> {//sets the end point of the wire
                    Node source = (Node) event1.getPickResult().getIntersectedNode();
                    if (!source.equals(pane)) {
                        currentLine.setEndX(event1.getX());
                        currentLine.setEndY(event1.getY());

                        Wire newWire = new Wire(currentLine.getStartX(), currentLine.getStartY(), currentLine.getEndX(), currentLine.getEndY(), wireType);
                        wiresOnDrawing.add(newWire);
                        pane.setOnMousePressed(null);

                        selectLineListener(newWire);
                    }
                });
            }
        });
    }

    private void selectLineListener(Wire newWire) {
        currentLine.setOnMousePressed(event1 -> {
            objectInfo.getChildren().clear();

            Label label = new Label(newWire.getWireType().getName());
            objectInfo.getChildren().add(label);

            Label label1 = new Label();
            label1.setBackground(Background.fill(newWire.getWireType().getColor()));
            label.setFont(new Font("Arial", 16));
            label1.setMinWidth(120);
            label1.setMinHeight(30);
            objectInfo.getChildren().add(label1);

            JFXButton deleteBtn = createButton("delete");
            deleteBtn.setOnMouseClicked(event -> {
                Line line = (Line) event1.getTarget();
                pane.getChildren().remove(line);
                wiresOnDrawing.remove(newWire);
            });
            objectInfo.getChildren().add(deleteBtn);
        });
    }


    public void makeDraggable(Node node, ScrollPane scrollPane, Pane pane, DataFormat dataFormat, Device d, Node card) {
        card.setOnDragDetected(event -> {
            Dragboard db = node.startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.put(dataFormat, d.getId());
            content.put(dataFormat, d.getId());
            db.setContent(content);
            event.consume();
        });

        pane.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            if(db.hasContent(dataFormat) && db.getContent(dataFormat) instanceof Integer){
                Node node1 = (Node) selectedDevice.getImgView();
                node1.setManaged(false);
                node1.setTranslateX(event.getX() - node1.getLayoutX() - (node1.getBoundsInParent().getHeight() / 2));
                node1.setTranslateY(event.getY() - node1.getLayoutY() - (node1.getBoundsInParent().getWidth() / 2));
                selectedDevice.getDevice().setPosX(node1.getTranslateX());
                selectedDevice.getDevice().setPosY(node1.getTranslateY());
                event.setDropCompleted(true);
                showDeviceInfo(selectedDevice);
                event.consume();
            }
        });

        scrollPane.setOnDragOver(event -> {
            event.acceptTransferModes(TransferMode.ANY);
            event.consume();
        });
    }

    public void save() throws Exception {
        //delete all saved devices from the installation
        getModelsHandler().getDrawingModel().removeDevicesFromInstallation(installation.getID());

        getModelsHandler().getDrawingModel().removeWiresFromInstallation(installation.getID());

        for (Wire wire: wiresOnDrawing){
            getModelsHandler().getDrawingModel().addWireToInstallation(wire, installation.getID());
        }

        //save all the devices from current drawing
        for(Device device : devicesOnDrawing) {
            getModelsHandler().getDrawingModel().addDeviceToInstallation(device, installation.getID());
        }

        //Clear the list and make sure it now reflects the changes from DB
        devicesOnDrawing.clear();

        Task<ObservableList<Device>> allDevicesTask = getModelsHandler()
                .getDrawingModel()
                .getDevicesFromInstallation(installation.getID());

        allDevicesTask.valueProperty().addListener((observable, oldValue, newValue) -> {
            devicesOnDrawing.addAll(newValue);
        });

        allDevicesTask.setOnFailed(event -> displayError(allDevicesTask.getException()));

        TaskExecutor.executeTask(allDevicesTask);
    }

    private int getIndex(){
        index += 1;
        return index;
    }

    public void handleAddWire() {
        FXMLLoader loader = openStage(ViewPaths.WIRE_VIEW, "Create Wire");
        AddWireController addWireController = loader.getController();
        addWireController.setDrawingController(this);
    }
}

