package gui.models;

import be.Device;
import be.DeviceLogin;
import be.DeviceType;
import bll.interfaces.IDrawingManager;
import bll.managers.DrawingManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.input.DataFormat;

import java.util.ArrayList;
import java.util.List;

public class DrawingModel {
    private IDrawingManager drawingManager;
    private ObservableList<DeviceType> allDeviceTypes;
    private List<Device> devices;
    private DataFormat dataFormat = new DataFormat("DragDropFormat1");

    public DrawingModel() throws Exception {
        drawingManager = new DrawingManager();
        devices = new ArrayList<>();

        updateAllDeviceTypes();
    }

    public void addDeviceToDrawing(Device device){
        devices.add(device);
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

    public DataFormat getDataFormat() {
        return dataFormat;
    }

    public void setDataFormat(DataFormat dataFormat) {
        this.dataFormat = dataFormat;
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

    public List<Device> getDevicesFromInstallation(int installationID) throws Exception {
        return drawingManager.getDevicesFromInstallation(installationID);
    }

    public boolean addDeviceToInstallation(Device device, int installationID) throws Exception {
        return drawingManager.addDeviceToInstallation(device, installationID);
    }

    public boolean removeDevicesFromInstallation(int installationID) throws Exception {
        return drawingManager.removeDevicesFromInstallation(installationID);
    }
}
