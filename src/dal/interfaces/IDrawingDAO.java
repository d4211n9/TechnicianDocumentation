package dal.interfaces;

import be.Drawing;

public interface IDrawingDAO {
    Drawing getDrawingFromInstallationId(int installationId) throws Exception;
}
