package dal.interfaces;

import be.DeviceType;

import java.util.List;

public interface IDeviceTypeDAO {
    List<DeviceType> getAllDeviceTypes() throws Exception;

    /**
     * Creates a new type of device.
     * @param deviceType the device to create.
     * @return true if the device was successfully created, false otherwise.
     * @throws Exception if something goes wrong.
     */
    boolean createDeviceType(DeviceType deviceType) throws Exception;
}
