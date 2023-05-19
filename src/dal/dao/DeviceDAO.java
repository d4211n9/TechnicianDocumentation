package dal.dao;

import be.Device;
import be.DeviceType;
import dal.connectors.AbstractConnector;
import dal.connectors.SqlConnector;
import dal.interfaces.IDeviceDAO;
import exceptions.DALException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeviceDAO implements IDeviceDAO {
    private AbstractConnector connector;

    public DeviceDAO() throws Exception {
        connector = new SqlConnector();
    }

    public DeviceDAO(AbstractConnector connector) {
        this.connector = connector;
    }

    @Override
    public List<Device> getAllDevicesFromDrawingId(int drawingId) throws Exception {
        String sql = "SELECT " +
                "Device.ID, Device.PosX, Device.PosY, Device.Width, Device.Height, " +
                "DeviceType.Name, DeviceType.ImagePath, DeviceType.HasLoginDetails " +
                "FROM Device " +
                "INNER JOIN DeviceType " +
                "ON Device.DeviceType = DeviceType.Name " +
                "WHERE Device.DrawingID = ?;";

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, drawingId);

            ResultSet resultSet = statement.executeQuery();

            List<Device> allDevicesFromDrawing = new ArrayList<>();

            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                double posX = resultSet.getDouble("PosX");
                double posY = resultSet.getDouble("PosY");
                double width = resultSet.getDouble("Width");
                double height = resultSet.getDouble("Height");

                String typeName = resultSet.getString("Name");
                String imagePath = resultSet.getString("ImagePath");
                boolean hasLoginDetails = resultSet.getBoolean("HasLoginDetails");

                DeviceType deviceType = new DeviceType(typeName, imagePath, hasLoginDetails);
                Device device = new Device(id, deviceType, posX, posY, width, height);

                allDevicesFromDrawing.add(device);
            }

            return allDevicesFromDrawing;
        }
        catch (SQLException e) {
            DALException dalException = new DALException("Failed to retrieve devices", e);
            dalException.printStackTrace();
            throw dalException;
        }
    }
}
