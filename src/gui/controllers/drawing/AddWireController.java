package gui.controllers.drawing;

import be.WireType;
import gui.controllers.BaseController;
import gui.util.TaskExecutor;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class AddWireController extends BaseController {

    public VBox wireColorDisplay;
    public ColorPicker colorPicker;
    public Label selectedColorView;
    public TextField txtName;

    DrawingController drawingController;

    public void handleConfirm() throws Exception {
        WireType newWireType = createWireTypeFromFields();

        if(newWireType != null) {
            Task<Boolean> createWireTask = getModelsHandler()
                    .getDrawingModel().createWireType(newWireType);

            drawingController.loadWireTypes();

            createWireTask.setOnFailed(event -> displayError(createWireTask.getException()));

            TaskExecutor.executeTask(createWireTask);
            handleBack();
        }
    }


    private WireType createWireTypeFromFields() {
        String name = txtName.getText();
        Color color = colorPicker.getValue();
        return new WireType(name, color);
    }


    public void handleBack() {
        Stage stage = (Stage) txtName.getParent().getScene().getWindow();
        stage.close();
    }

    public void setDrawingController(DrawingController drawingController) {
        this.drawingController = drawingController;
    }

    public void handleColorPicker(ActionEvent actionEvent) {
        Color chosenColor = colorPicker.getValue();
        selectedColorView.setBackground(Background.fill(chosenColor));
    }
}
