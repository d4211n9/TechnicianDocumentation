package dal.dao;

import be.Device;
import be.DeviceLogin;
import dal.connectors.AbstractConnector;
import dal.connectors.SqlConnector;
import dal.interfaces.IDeviceLoginDAO;
import exceptions.DALException;

import java.sql.*;

public class DeviceLoginDAO implements IDeviceLoginDAO {
    private AbstractConnector connector;

    public DeviceLoginDAO() throws Exception {
        connector = new SqlConnector();
    }

    @Override
    public DeviceLogin createDeviceLogin(DeviceLogin deviceLogin) throws Exception {
        DeviceLogin newDeviceLogin = null;
        String sql = "INSERT INTO DeviceLogin (DeviceID, Username, Password) VALUES (?, ?, ?)";

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, deviceLogin.getDevice().getId());
            statement.setString(2, deviceLogin.getUsername());
            statement.setString(3, deviceLogin.getPassword());

            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();

            if(resultSet.next()) {
                int ID = resultSet.getInt(1);

                newDeviceLogin = new DeviceLogin(ID, deviceLogin.getDevice(),
                        deviceLogin.getUsername(), deviceLogin.getPassword());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DALException("Failed to create device login", e);
        }

        return newDeviceLogin;
    }

    @Override
    public DeviceLogin getDeviceLogin(Device device) throws Exception {
        DeviceLogin deviceLogin = null;

        String sql = "SELECT * FROM DeviceLogin WHERE DeviceID = ? AND SoftDelete IS NULL;";

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, device.getId());

            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {
                int ID = resultSet.getInt(1);
                String username = resultSet.getString(3);
                String password = resultSet.getString(4);

                deviceLogin = new DeviceLogin(ID, device, username, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DALException("Failed to read device login", e);
        }
        return deviceLogin;
    }

    @Override
    public DeviceLogin updateDeviceLogin(DeviceLogin deviceLogin) throws Exception {
        DeviceLogin updatedDeviceLogin = null;
        String sql = "UPDATE DeviceLogin SET Username=?, Password=?, SoftDelete=? WHERE ID=?;";

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, deviceLogin.getUsername());
            statement.setString(2, deviceLogin.getPassword());
            statement.setTimestamp(3, deviceLogin.getDeleted());
            statement.setInt(4, deviceLogin.getID());

            statement.executeUpdate();

            updatedDeviceLogin = deviceLogin;

        } catch (SQLException e) {
            throw new Exception("Failed to update device login", e);
        }
        return updatedDeviceLogin;
    }
}
