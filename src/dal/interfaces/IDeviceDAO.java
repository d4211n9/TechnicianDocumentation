package dal.interfaces;

import be.Device;

import java.util.List;

public interface IDeviceDAO {
    List<Device> getAllDevicesFromDrawingId(int drawingId) throws Exception;
}
