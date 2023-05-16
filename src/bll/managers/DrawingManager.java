package bll.managers;

import be.DeviceType;
import bll.interfaces.IDrawingManager;
import dal.facades.DrawingFacade;

import java.util.List;

public class DrawingManager implements IDrawingManager {
    private DrawingFacade drawingFacade;

    public DrawingManager() throws Exception {
        drawingFacade = new DrawingFacade();
    }

    @Override
    public List<DeviceType> getAllDeviceTypes() throws Exception {
        return drawingFacade.getAllDeviceTypes();
    }
}
