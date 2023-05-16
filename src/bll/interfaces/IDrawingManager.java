package bll.interfaces;

import be.DeviceType;

import java.util.List;

public interface IDrawingManager {
    List<DeviceType> getAllDeviceTypes() throws Exception;
}
