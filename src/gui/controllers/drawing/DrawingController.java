package gui.controllers.drawing;

import be.Device;
import be.DeviceType;
import gui.controllers.BaseController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
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
import java.util.ResourceBundle;

public class DrawingController extends BaseController implements Initializable {


    public Button button;
    public ScrollPane contentArea;
    public Label lbl;
    public VBox background;
    public Pane pane;
    public VBox objectInfo;

    private DeviceController selectedDevice;


    public DataFormat dataFormat = new DataFormat("DragDropFormat1");

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
            Device d = new Device(deviceType, pane.getChildren().size());
            FXMLLoader loader1 = loadView("/gui/views/drawing/DeviceView.fxml");
            DeviceController controller1 = loader1.getController();
            controller1.setContent(d);

            ImageView imgview = controller1.getImgView();
            imgview.setOnMousePressed(event1 -> {
                selectedDevice = controller1;
                showDeviceInfo(controller1.getDevice());
                System.out.println(controller1.getDevice());
            });
            selectedDevice = controller1;

            d.setHeight(80);
            d.setWidth(80);
            selectedElementImg = imgview;
            selectedElementImg.setFitWidth(d.getHeight());
            selectedElementImg.setFitHeight(d.getWidth());
            Tooltip imgName = new Tooltip(deviceType.getName());
            imgName.setShowDelay(Duration.millis(200));
            Tooltip.install(selectedElementImg, imgName);

            selectedElementImg.setLayoutX(-100);
            selectedElementImg.setLayoutY(-100);

            pane.getChildren().add(selectedElementImg);
            problem(selectedElementImg, contentArea, pane, dataFormat, d, deviceElement);
            DeviceCard controller = loader.getController();
            try {
                //getModelsHandler().getDrawingModel().addDeviceToDrawing(controller.getDevice()); //todo run line when drawing is not null
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            problem(selectedElementImg, contentArea, pane, dataFormat, d, selectedElementImg);
        });
    }

    private void showDeviceInfo(Device device){
        objectInfo.getChildren().clear();

        Label name = new Label(device.getDeviceType().getName());
        objectInfo.getChildren().add(name);

        //creates the pos x filed
        Label label = new Label("PosX   ");
        TextField txtFiled = new TextField();
        txtFiled.setText(String.valueOf(device.getPosX()));
        txtFiled.textProperty().addListener((obs, oldVal, newVal) -> {
            device.setPosX(Double.parseDouble(newVal));
            System.out.println(device.getPosX());
        });
        HBox hbox = new HBox(label, txtFiled);
        hbox.setSpacing(10);
        objectInfo.getChildren().add(hbox);

        Label posY = new Label("PosY   ");
        TextField txtField = new TextField();
        txtField.setText(String.valueOf(device.getPosY()));
        HBox hboxPosY = new HBox(posY, txtField);
        hboxPosY.setSpacing(10);
        objectInfo.getChildren().add(hboxPosY);

        Label lblHeight = new Label("Height");
        TextField txtFieldHeight = new TextField();
        txtFieldHeight.setText(String.valueOf(device.getHeight()));
        HBox hboxHeight = new HBox(lblHeight, txtFieldHeight);
        hboxHeight.setSpacing(10);
        objectInfo.getChildren().add(hboxHeight);

        Label lblWidth = new Label("Width ");
        TextField txtFieldWidth = new TextField();
        txtFieldWidth.setText(String.valueOf(device.getWidth()));
        HBox hboxWidth = new HBox(lblWidth, txtFieldWidth);
        hboxWidth.setSpacing(10);
        objectInfo.getChildren().add(hboxWidth);
    }







    public void handleAddDevice() {
        FXMLLoader loader = openStage(ViewPaths.CREATE_DEVICE, "Create Device");
        AddDeviceController addDeviceController = loader.getController();
        addDeviceController.setDrawingController(this);
    }


    public void handleAddLine() {
        pane.setOnMousePressed(e -> {
            Node source = (Node)e.getPickResult().getIntersectedNode();
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
                            System.out.println(source);
                            pane.setOnMousePressed(null);
                        }
                    }
                });
                e.consume();
            }
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
                Node node1 = (Node) pane.getChildren().get(index);
                node1.setManaged(false);
                // this is the problematic part
                node1.setTranslateX(event.getX() - node1.getLayoutX() - (node1.getBoundsInParent().getHeight() / 2));
                node1.setTranslateY(event.getY() - node1.getLayoutY() - (node1.getBoundsInParent().getWidth() / 2));
                selectedDevice.getDevice().setPosX(node1.getTranslateX());
                selectedDevice.getDevice().setPosY(node1.getTranslateY());
                event.setDropCompleted(true);
                event.consume();
            }
        });

        scrollPane.setOnDragOver(event -> {
            event.acceptTransferModes(TransferMode.ANY);
            event.consume();
        });
    }

    public void save(ActionEvent actionEvent) {
        //todo create safe method that calls down the layer and sends devicesOnDrawing
    }
}

