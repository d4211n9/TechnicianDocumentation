package gui.models;

import be.Device;
import be.DeviceType;
import be.Drawing;
import bll.interfaces.IDrawingManager;
import bll.managers.DrawingManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.input.DataFormat;

import java.util.List;

public class DrawingModel {
    private IDrawingManager drawingManager;

    private ObservableList<DeviceType> allDeviceTypes;
    private Drawing selectedDrawing;

    private DataFormat dataFormat = new DataFormat("DragDropFormat1");

    public DrawingModel() throws Exception {
        drawingManager = new DrawingManager();

        updateAllDeviceTypes();
    }

    public void addDeviceToDrawing(Device device){
        selectedDrawing.getDevices().add(device);
    }

    public Task<Boolean> createDeviceType(DeviceType deviceTypeToCreate) {
        Task<Boolean> createDeviceTypeTask = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                boolean successfullyCreatedDeviceType = drawingManager.createDeviceType(deviceTypeToCreate);

                updateValue(successfullyCreatedDeviceType);

                return successfullyCreatedDeviceType;
            }
        };

        return createDeviceTypeTask;
    }

    /**
     * Gets this instance of the observable list "allDeviceTypes".
     * It does not re-retrieve all device types, but simply returns the ones already stored.
     * @return the observable list of all device types.
     */
    public ObservableList<DeviceType> getAllDeviceTypes() {
        return allDeviceTypes;
    }

    public void updateAllDeviceTypes() throws Exception {
        List<DeviceType> allDeviceTypes = drawingManager.getAllDeviceTypes();

        if (this.allDeviceTypes == null) {
            this.allDeviceTypes = FXCollections.observableList(allDeviceTypes);
        }
        else {
            this.allDeviceTypes.clear();
            this.allDeviceTypes.addAll(allDeviceTypes);
        }
    }

    public Drawing getSelectedDrawing() {
        return selectedDrawing;
    }

    public void setSelectedDrawing(int installationId) throws Exception {
        selectedDrawing = drawingManager.getDrawingFromInstallationId(installationId);
    }

    public void saveAllDevicesOnDrawing(Drawing drawing){
        //todo should delete the drawing
        //todo create a new drawing
        //todo should create a device for each item in the list linking to the drawing
    }

    public DataFormat getDataFormat() {
        return dataFormat;
    }

    public void setDataFormat(DataFormat dataFormat) {
        this.dataFormat = dataFormat;

    public void deleteDrawing(Drawing drawing) throws Exception {
        drawingManager.deleteDrawing(drawing);
    }
}
