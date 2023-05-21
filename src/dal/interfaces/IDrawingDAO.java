package dal.interfaces;

import be.Drawing;

public interface IDrawingDAO {

    Drawing createDrawing(Drawing drawing) throws Exception;
    Drawing getDrawingFromInstallationId(int installationId) throws Exception;

    void deleteDrawing(Drawing drawing) throws Exception;
}
