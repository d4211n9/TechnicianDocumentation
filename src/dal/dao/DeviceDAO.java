package dal.dao;

import be.Device;
import be.DeviceType;
import dal.connectors.AbstractConnector;
import dal.connectors.SqlConnector;
import dal.interfaces.IDeviceDAO;
import exceptions.DALException;

import java.sql.*;
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
    public Device createDevice(Device device) throws Exception {
        Device newDevice = null;
        String sql = "INSERT INTO Device " +
                "(DeviceType, PosX, PosY, Width, Height) " +
                "VALUES (?, ?, ?, ?, ?);";

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, device.getDeviceTypeName());
            statement.setDouble(2, device.getPosX());
            statement.setDouble(3, device.getPosY());
            statement.setDouble(4, device.getWidth());
            statement.setDouble(5, device.getHeight());

            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();

            if(resultSet.next()) {
                int id = resultSet.getInt(1);

                newDevice = new Device(id, device.getDeviceType(), device.getPosX(), device.getPosY(),
                        device.getWidth(), device.getHeight());
            }

        } catch (SQLException e) {
            DALException dalException = new DALException("Failed to create device", e);
            dalException.printStackTrace();
            throw dalException;
        }
        return newDevice;
    }
}
