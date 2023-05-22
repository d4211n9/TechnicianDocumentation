package dal.interfaces;

import be.Device;

import java.util.List;

public interface IDeviceDAO {
    Device createDevice(Device device) throws Exception;
}
