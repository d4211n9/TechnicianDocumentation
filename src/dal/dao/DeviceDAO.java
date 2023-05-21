package dal.dao;

import be.Device;
import be.DeviceType;
import dal.connectors.AbstractConnector;
import dal.connectors.SqlConnector;
import dal.interfaces.IDeviceDAO;
import exceptions.DALException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
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

    @Override
    public List<Device> createDevices(List<Device> devicesToCreate, int drawingId) throws Exception {
        String sql = "INSERT INTO Device " +
                "(DrawingID, DeviceType, PosX, PosY, Width, Height) " +
                "VALUES (?, ?, ?, ?, ?, ?);";

        PreparedStatement statement = null;

        try (Connection connection = connector.getConnection()) {

            List<Device> createdDevices = new ArrayList<>();

            for (Device deviceToCreate : devicesToCreate) {
                statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                statement.setInt(1, drawingId);
                statement.setString(2, deviceToCreate.getDeviceType().getName());
                statement.setDouble(3, deviceToCreate.getPosX());
                statement.setDouble(4, deviceToCreate.getPosY());
                statement.setDouble(5, deviceToCreate.getWidth());
                statement.setDouble(6, deviceToCreate.getHeight());

                statement.executeUpdate();

                int id = statement.getGeneratedKeys().getInt(1);
                createdDevices.add(new Device(id, deviceToCreate));
            }

            return createdDevices;
        } catch (SQLException e) {
            DALException dalException = new DALException("Failed to create devices", e);
            dalException.printStackTrace();
            throw dalException;
        }
        finally {
            if (statement != null) statement.close();
        }
    }
}
