package gui.models;

import be.Device;
import be.DeviceLogin;
import be.DeviceType;
import be.WireType;
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

    private ObservableList<WireType> allWireTypes;
    private List<Device> devices;
    private DataFormat dataFormat = new DataFormat("DragDropFormat1");

    public DrawingModel() throws Exception {
        drawingManager = new DrawingManager();
        devices = new ArrayList<>();

        updateAllDeviceTypes();
        updateAllWireTypes();
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

    public void updateAllWireTypes() throws Exception {
        List<WireType> allWireTypes1 = drawingManager.getAllWireTypes();

        if (this.allWireTypes == null) {
            this.allWireTypes = FXCollections.observableList(allWireTypes1);
        }
        else {
            this.allWireTypes.clear();
            this.allWireTypes.addAll(allWireTypes1);
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

    public Task<ObservableList<Device>> getDevicesFromInstallation(int installationID) {
        Task<ObservableList<Device>> allDevicesTask = new Task<ObservableList<Device>>() {
            @Override
            protected ObservableList<Device> call() throws Exception {
                ObservableList<Device> allDevices = FXCollections.observableList
                        (drawingManager.getDevicesFromInstallation(installationID));

                updateValue(allDevices);

                return allDevices;
            }
        };

        return allDevicesTask;
    }

    public boolean addDeviceToInstallation(Device device, int installationID) throws Exception {
        return drawingManager.addDeviceToInstallation(device, installationID);
    }

    public boolean removeDevicesFromInstallation(int installationID) throws Exception {
        return drawingManager.removeDevicesFromInstallation(installationID);
    }

    public Task<Boolean> createWireType(WireType wireType) {
        Task<Boolean> createDeviceTypeTask = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                boolean successfullyCreatedDeviceType = drawingManager.createWireType(wireType);

                updateValue(successfullyCreatedDeviceType);
                return successfullyCreatedDeviceType;
            }
        };

        allWireTypes.add(wireType);

        return createDeviceTypeTask;
    }

    public ObservableList<WireType> getAllWireTypes() throws Exception {
        return allWireTypes;
    }
}
