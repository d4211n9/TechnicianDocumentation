package dal.dao;

import be.Device;
import be.DeviceType;
import dal.connectors.AbstractConnector;
import dal.connectors.SqlConnector;
import dal.interfaces.IDeviceOnInstallationDAO;
import exceptions.DALException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeviceOnInstallationDAO implements IDeviceOnInstallationDAO {
    private AbstractConnector connector;

    public DeviceOnInstallationDAO() throws Exception {
        connector = new SqlConnector();
    }

    @Override
    public boolean addDeviceToInstallation(Device device, int installationID) throws Exception {
        String sql = "INSERT INTO DeviceOnDrawing (InstallationID, DeviceID) VALUES (?, ?);";

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, installationID);
            statement.setInt(2, device.getId());

            int insertedTuples = statement.executeUpdate();

            return insertedTuples == 1;
        } catch (SQLException e) {
            DALException dalException = new DALException("Failed to add device to installation", e);
            dalException.printStackTrace();

            throw dalException;
        }
    }

    @Override
    public List<Device> getDevicesFromInstallation(int installationID) throws Exception {
        String sql = "SELECT * " +
                "FROM DeviceOnDrawing " +
                "INNER JOIN Device " +
                "ON DeviceOnDrawing.DeviceID = Device.ID " +
                "INNER JOIN DeviceType " +
                "ON DeviceType.Name = Device.DeviceType " +
                "WHERE InstallationID = ?;";

        try (Connection conn = connector.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            List<Device> devicesFromInstallation = new ArrayList<>();

            statement.setInt(1, installationID);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                //Map devicetype
                String name = resultSet.getString(10);
                String imagePath = resultSet.getString(11);
                boolean loginDetails = resultSet.getBoolean(12);
                DeviceType deviceType = new DeviceType(name, imagePath, loginDetails);

                //then map device
                int id = resultSet.getInt(4);
                double posX = resultSet.getDouble(6);
                double posY = resultSet.getDouble(7);
                double width = resultSet.getDouble(8);
                double height = resultSet.getDouble(9);
                Device device = new Device(id, deviceType, posX, posY, width, height);

                devicesFromInstallation.add(device);
            }

            return devicesFromInstallation;
        } catch (SQLException e) {
            DALException dalException = new DALException("Failed to retrieve devices from installation", e);
            dalException.printStackTrace();
            throw dalException;
        }
    }

    @Override
    public boolean removeDevicesFromInstallation(int installationID) throws Exception {
        String sql = "DELETE FROM DeviceOnDrawing WHERE InstallationID = ?;";

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, installationID);

            int insertedTuples = statement.executeUpdate();

            return insertedTuples == 1;
        } catch (SQLException e) {
            DALException dalException = new DALException("Failed to remove devices from installation", e);
            dalException.printStackTrace();

            throw dalException;
        }
    }
}
