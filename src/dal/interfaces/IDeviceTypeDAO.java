package dal.interfaces;

import be.DeviceType;

import java.util.List;

public interface IDeviceTypeDAO {
    List<DeviceType> getAllDeviceTypes() throws Exception;
}
