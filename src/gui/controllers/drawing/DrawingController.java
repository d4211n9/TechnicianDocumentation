package gui.controllers.drawing;

import be.*;
import com.jfoenix.controls.JFXButton;
import gui.controllers.BaseController;
import gui.util.TaskExecutor;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import util.ViewPaths;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class DrawingController extends BaseController implements Initializable {


    public VBox sideBarWires;
    @FXML
    private VBox background, objectInfo, sidebarDevice;
    @FXML
    private Button button;
    @FXML
    private ScrollPane contentArea;
    @FXML
    private Pane pane;
    @FXML
    private ImageView selectedElementImg;

    private DeviceController selectedDevice;
    private Node source;
    private Line currentLine;
    ArrayList<Device> devicesOnDrawing;

    ArrayList<Wire> wiresOnDrawing;
    private Installation installation;

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


    public void setInstallation(Installation installation) {
        this.installation = installation;

        try {
            devicesOnDrawing.clear();
            pane.getChildren().clear();

            Task<ObservableList<Device>> allDevicesTask = getModelsHandler()
                    .getDrawingModel()
                    .getDevicesFromInstallation(installation.getID());

            allDevicesTask.valueProperty().addListener((observable, oldValue, newValue) ->  {
                ObservableList<Device> allDevices = newValue;

                for(Device device : allDevices) {
                    devicesOnDrawing.add(device);
                    try {
                        loadDeviceInPane(device);
                    } catch (Exception e) {
                        displayError(e);
                    }
                }
            });
            TaskExecutor.executeTask(allDevicesTask);

            Task<ObservableList<Wire>> allWiresTask = getModelsHandler()
                    .getDrawingModel()
                    .getWiresFromInstallation(installation.getID());

            allWiresTask.valueProperty().addListener((observable, oldValue, newValue) ->  {
                ObservableList<Wire> allWires = newValue;
                System.out.println(allWires.size());

                for(Wire wire : allWires) {
                    wiresOnDrawing.add(wire);
                    try {
                        addWireToPane(wire);

                        //todo make method for creating wire
                    } catch (Exception e) {
                        displayError(e);
                    }
                }
            });
            TaskExecutor.executeTask(allWiresTask);


        } catch (Exception e) {
            displayError(e);
        }
    }

    private void addWireToPane(Wire wire) {
        currentLine = new Line(wire.getStartX(), wire.getStartY(), wire.getEndX(), wire.getEndY());
        System.out.println(wire.getStartX() + "dwdwdwdwdwdwdwdwdwdw");
        currentLine.setStrokeWidth(5.0);
        currentLine.setStroke(wire.getWireType().getColor());
        pane.getChildren().add(currentLine);
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
                addWireCardListener(loader, wireType);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void addWireCardListener(FXMLLoader loader, WireType wireType) {
        handleAddLineTest(loader, wireType);
    }


    public void loadDeviceInPane(Device device) throws Exception {
        FXMLLoader loader1 = loadView("/gui/views/drawing/DeviceView.fxml");
        DeviceController controller1 = loader1.getController();
        controller1.setContent(device);

        ImageView deviceImg = controller1.getImgView();

        deviceImg.setOnMousePressed(event1 -> {
            selectedDevice = controller1;
            showDeviceInfo(selectedDevice);
            source = (Node)event1.getPickResult().getIntersectedNode();
        });
        selectedDevice = controller1;

        deviceImg.setFitHeight(device.getHeight());
        deviceImg.setFitWidth(device.getWidth());

        deviceImg.setTranslateX(device.getPosX());
        deviceImg.setTranslateY(device.getPosY());


        Tooltip imgName = new Tooltip(device.getDeviceType().getName());
        imgName.setShowDelay(Duration.millis(200));
        Tooltip.install(deviceImg, imgName);
        device.setId(getIndex());

        problem(deviceImg, contentArea, pane, getModelsHandler().getDrawingModel().getDataFormat(), device, deviceImg);

        pane.getChildren().add(deviceImg);
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
                problem(selectedElementImg, contentArea, pane, getModelsHandler().getDrawingModel().getDataFormat(), d, deviceElement);

                devicesOnDrawing.add(d);
                getModelsHandler().getDrawingModel().addDeviceToInstallation(d, installation.getID());

                problem(selectedElementImg, contentArea, pane, getModelsHandler().getDrawingModel().getDataFormat(), d, selectedElementImg);
            } catch (Exception e) {
                displayError(e);
            }
        });
    }

    private void showDeviceInfo(DeviceController controller){
        Device device = controller.getDevice();
        objectInfo.getChildren().clear();

        Label name = new Label(device.getDeviceType().getName());
        objectInfo.getChildren().add(name);

        //creates the pos x filed
        Label label = new Label("PosX   ");
        TextField txtFiled = new TextField();
        txtFiled.setText(String.valueOf(device.getPosX()));
        txtFiled.textProperty().addListener((obs, oldVal, newVal) -> {
            if(!Objects.equals(newVal, "")){
                device.setPosX(Double.parseDouble(newVal));
                controller.imgView.setTranslateX(Double.parseDouble(newVal));
            }
        });
        HBox hbox = new HBox(label, txtFiled);
        hbox.setSpacing(10);
        objectInfo.getChildren().add(hbox);

        //creates the pos Y filed
        Label posY = new Label("PosY   ");
        TextField txtField = new TextField();
        txtField.setText(String.valueOf(device.getPosY()));
        txtField.textProperty().addListener((obs, oldVal, newVal) -> {
            if(!Objects.equals(newVal, "")){
                device.setPosY(Double.parseDouble(newVal));
                controller.imgView.setTranslateY(Double.parseDouble(newVal));
            }
        });
        HBox hboxPosY = new HBox(posY, txtField);
        hboxPosY.setSpacing(10);
        objectInfo.getChildren().add(hboxPosY);


        Label lblHeight = new Label("Height");
        TextField txtFieldHeight = new TextField();
        txtFieldHeight.setText(String.valueOf(device.getHeight()));
        txtFieldHeight.textProperty().addListener((obs, oldVal, newVal) -> {
            if(!Objects.equals(newVal, "")){
                device.setHeight(Double.parseDouble(newVal));
                controller.settingImgHeight(Double.valueOf(newVal));
                source.getParent().prefHeight(Double.parseDouble(newVal));
            }
        });
        HBox hboxHeight = new HBox(lblHeight, txtFieldHeight);
        hboxHeight.setSpacing(10);
        objectInfo.getChildren().add(hboxHeight);

        Label lblWidth = new Label("Width ");
        TextField txtFieldWidth = new TextField();
        txtFieldWidth.setText(String.valueOf(device.getWidth()));
        txtFieldWidth.textProperty().addListener((obs, oldVal, newVal) -> {
            if(!Objects.equals(newVal, "")){
                device.setWidth(Double.parseDouble(newVal));
                controller.settingImgHeight(Double.valueOf(newVal));
                source.getParent().prefWidth(Double.parseDouble(newVal));
            }
        });
        HBox hboxWidth = new HBox(lblWidth, txtFieldWidth);
        hboxWidth.setSpacing(10);
        objectInfo.getChildren().add(hboxWidth);

        JFXButton deleteBtn = createButton("delete");
        deleteBtn.setOnMouseClicked(event -> {
            pane.getChildren().remove(source);
            devicesOnDrawing.remove(controller.getDevice());
            try {
                save();
            } catch (Exception e) {
                displayError(e);
            }
            objectInfo.getChildren().clear();
        });
        objectInfo.getChildren().add(deleteBtn);
    }

    public void handleAddDevice() {
        FXMLLoader loader = openStage(ViewPaths.CREATE_DEVICE, "Create Device");
        AddDeviceController addDeviceController = loader.getController();
        addDeviceController.setDrawingController(this);
    }

    public void handleAddLine() {
        pane.setOnMousePressed(e -> {
            source = (Node)e.getPickResult().getIntersectedNode();
            if(!source.equals(pane)) {
                currentLine = new Line(e.getX(), e.getY(), e.getX(), e.getY());
                pane.getChildren().add(currentLine);

                pane.setOnMousePressed(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        Node source = (Node) event.getPickResult().getIntersectedNode();
                        if (!source.equals(pane)) {

                            currentLine.setEndX(event.getX());
                            currentLine.setEndY(event.getY());
                            pane.setOnMousePressed(null);
                        }
                    }
                });
                e.consume();
            }
        });
    }

    public void handleAddLineTest(FXMLLoader loader, WireType wireType) {
        Node wireCard = loader.getRoot();
        wireCard.setOnMousePressed(event -> {
            pane.setOnMousePressed(e -> {
                source = (Node)e.getPickResult().getIntersectedNode();
                if(!source.equals(pane)) {
                    currentLine = new Line(e.getX(), e.getY(), e.getX(), e.getY());
                    currentLine.setStrokeWidth(5.0);
                    currentLine.setStroke(wireType.getColor());
                    pane.getChildren().add(currentLine);

                    pane.setOnMousePressed(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            Node source = (Node) event.getPickResult().getIntersectedNode();
                            if (!source.equals(pane)) {
                                currentLine.setEndX(event.getX());
                                currentLine.setEndY(event.getY());

                                Wire newWire = new Wire(currentLine.getStartX(), currentLine.getStartY(), currentLine.getEndX(), currentLine.getEndY(), wireType);
                                wiresOnDrawing.add(newWire);//todo should also add them in model
                                pane.setOnMousePressed(null);

                                currentLine.setOnMousePressed(event1 -> {
                                    Line line = (Line) event1.getTarget();
                                    pane.getChildren().remove(line);
                                    wiresOnDrawing.remove(newWire);
                                    // todo  should remove wire in model
                                });
                            }
                        }
                    });
                }
            });
        });

    }


    public void problem(Node node, ScrollPane scrollPane, Pane pane, DataFormat dataFormat, Device d, Node card) {
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
                Node node1 = (Node) selectedDevice.getImgView();
                node1.setManaged(false);
                // this is the problematic part
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

        //todo maybe this should be done for wires also
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

    public int index = 0;

    private int getIndex(){
        index += 1;
        return index;
    }

    public void handleAddWire(ActionEvent actionEvent) {
        FXMLLoader loader = openStage(ViewPaths.WIRE_VIEW, "Create Wire");
        AddWireController addWireController = loader.getController();
        addWireController.setDrawingController(this);
    }
}

