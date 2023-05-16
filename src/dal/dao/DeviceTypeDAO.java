package dal.dao;

import be.DeviceType;
import dal.connectors.AbstractConnector;
import dal.connectors.SqlConnector;
import dal.interfaces.IDeviceTypeDAO;
import exceptions.DALException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeviceTypeDAO implements IDeviceTypeDAO {
    private AbstractConnector connector;

    public DeviceTypeDAO() throws Exception {
        connector = new SqlConnector();
    }

    public DeviceTypeDAO(AbstractConnector connector) {
        this.connector = connector;
    }

    @Override
    public List<DeviceType> getAllDeviceTypes() throws Exception {
        String sql = "SELECT Name, ImagePath, HasLoginDetails FROM DeviceType";

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            List<DeviceType> allDeviceTypes = new ArrayList<>();

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString("Name");
                String imagePath = resultSet.getString("ImagePath");
                boolean hasLoginDetails = resultSet.getBoolean("HasLoginDetails");

                allDeviceTypes.add(new DeviceType(name, imagePath, hasLoginDetails));
            }

            return allDeviceTypes;
        }
        catch (SQLException e) {
            DALException dalException = new DALException("Failed to get all device types", e);
            e.printStackTrace();
            throw dalException;
        }
    }
}
