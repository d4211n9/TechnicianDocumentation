package gui.models;

import be.Device;
import be.DeviceLogin;
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

    public Drawing createDrawing(Drawing drawing) throws Exception {
        return drawingManager.createDrawing(drawing);
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

    public void saveAllDevicesOnDrawing() throws Exception {
        deleteDrawing(selectedDrawing);
        System.out.println(selectedDrawing.getDevices().size());//todo
        selectedDrawing = createDrawing(selectedDrawing);

        //todo create a new drawing
        drawingManager.createDevices(selectedDrawing.getDevices(), selectedDrawing.getId());
    }

    public DataFormat getDataFormat() {
        return dataFormat;
    }

    public void setDataFormat(DataFormat dataFormat) {
        this.dataFormat = dataFormat;
    }

    public void deleteDrawing(Drawing drawing) throws Exception {
        drawingManager.deleteDrawing(drawing);
    }

    //TODO Lav til tasks ?
    public DeviceLogin createDeviceLogin(DeviceLogin deviceLogin) throws Exception {
        return drawingManager.createDeviceLogin(deviceLogin);
    }

    public DeviceLogin getDeviceLogin(Device device) throws Exception {
        return drawingManager.getDeviceLogin(device);
    }

    public DeviceLogin updateDeviceLogin(DeviceLogin deviceLogin) throws Exception {
        return drawingManager.updateDeviceLogin(deviceLogin);
    }
}
