package gui.models;

import be.DeviceType;
import bll.interfaces.IDrawingManager;
import bll.managers.DrawingManager;
import gui.util.TaskExecutor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.util.List;

public class DrawingModel {
    private IDrawingManager drawingManager;

    private ObservableList<DeviceType> allDeviceTypes;

    public DrawingModel() throws Exception {
        drawingManager = new DrawingManager();

        updateAllDeviceTypes();
    }

    /**
     * Gets this instance of the observable list "allDeviceTypes".
     * It does not re-retrieve all device types, but simply returns the ones already stored.
     * @return the observable list of all device types.
     */
    public ObservableList<DeviceType> getAllDeviceTypes() {
        return allDeviceTypes;
    }

    private void updateAllDeviceTypes() throws Exception {
        List<DeviceType> allDeviceTypes = drawingManager.getAllDeviceTypes();

        if (this.allDeviceTypes == null) {
            this.allDeviceTypes = FXCollections.observableList(allDeviceTypes);
        }
        else {
            this.allDeviceTypes.clear();
            this.allDeviceTypes.addAll(allDeviceTypes);
        }
    }
}
