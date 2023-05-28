package dal.dao;

import be.Wire;
import be.WireType;
import dal.connectors.AbstractConnector;
import dal.connectors.SqlConnector;
import dal.interfaces.IWiresOnInstallationDAO;
import exceptions.DALException;
import javafx.scene.paint.Color;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WiresOnInstallation implements IWiresOnInstallationDAO {

    private AbstractConnector connector;

    public WiresOnInstallation() throws Exception {
        connector = new SqlConnector();
    }

    @Override
    public boolean addWireToInstallation(Wire wire, int id) throws DALException {
        String sql = "INSERT INTO WireOnDrawing (InstallationID, WireID) VALUES (?, ?);";

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.setInt(2, wire.getId());

            int insertedTuples = statement.executeUpdate();

            return insertedTuples == 1;
        } catch (Exception e) {
            DALException dalException = new DALException("Failed to add wire to installation", e);
            dalException.printStackTrace();

            throw dalException;
        }
    }

    @Override
    public boolean removeWiresFromInstallation(int id) throws DALException {
        String sql = "DELETE FROM WireOnDrawing WHERE InstallationID = ?;";

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            int insertedTuples = statement.executeUpdate();

            return insertedTuples == 1;
        } catch (Exception e) {
            DALException dalException = new DALException("Failed to remove wire from installation", e);
            dalException.printStackTrace();
            throw dalException;
        }
    }

    @Override
    public List<Wire> getWiresFromInstallation(int id) throws DALException {
        String sql = "SELECT * " +
                "FROM WireOnDrawing " +
                "INNER JOIN Wire " +
                "ON WireOnDrawing.WireID = Wire.ID " +
                "INNER JOIN WireType " +
                "ON WireType.Name = Wire.WireType " +
                "WHERE InstallationID = ?;";

        try (Connection conn = connector.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            List<Wire> wiresFromInstallation = new ArrayList<>();

            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                //Map devicetype


                String wireType = resultSet.getString(9);
                Color color = Color.web(resultSet.getString(11));
                WireType wireType1 = new WireType(wireType, color);

                //user for mapping wire
                //todo map wire

                int wireId = resultSet.getInt(3);
                /***
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
                 */

            }

            return wiresFromInstallation;
        } catch (SQLException e) {
            DALException dalException = new DALException("Failed to retrieve devices from installation", e);
            dalException.printStackTrace();
            throw dalException;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // return null;
    }
}
