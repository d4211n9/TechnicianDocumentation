package dal.dao;

import be.WireType;
import dal.connectors.AbstractConnector;
import dal.connectors.SqlConnector;
import dal.interfaces.IWireTypeDAO;
import exceptions.DALException;
import javafx.scene.paint.Color;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WireTypeDAO implements IWireTypeDAO {

    private AbstractConnector connector;

    private List l;

    public WireTypeDAO() throws Exception {
        connector = new SqlConnector();
        l = new ArrayList<>();

        // todo test data should be deleted when real get method is implemented.
        WireType wireType = new WireType("hdmi", Color.color(0.5,0.5,0.5));
        WireType wireType1 = new WireType("power", Color.color(0,1.0,0.1));
        WireType wireType2 = new WireType("signal", Color.color(1.0,0.0,0.5));


        l.add(wireType);
        l.add(wireType1);
        l.add(wireType2);
    }

    public WireTypeDAO(AbstractConnector connector) {
        this.connector = connector;
    }

    @Override
    public List<WireType> getAllWireTypes() throws Exception {

        //return l; //todo get all method from wireType... look at devicetypeDAO

        String sql = "SELECT Name, Color FROM WireType";

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            List<WireType> allWireTypes = new ArrayList<>();

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString("Name");
                String color = resultSet.getString("Color");

                allWireTypes.add(new WireType(name, Color.web(color)));
            }

            return allWireTypes;
        }
        catch (SQLException e) {
            DALException dalException = new DALException("Failed to get all device types", e);
            e.printStackTrace();
            throw dalException;
        }
    }

    @Override
    public boolean createWireType(WireType wireType) throws Exception {
        String sql = "INSERT INTO WireType " +
                "(Name, Color) " +
                "VALUES (?, ?)";


        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, wireType.getName());
            statement.setString(2, String.valueOf(wireType.getColor()));

            int createdTuples = statement.executeUpdate();

            return createdTuples == 1;
        }
        catch (SQLException e) {
            DALException dalException = new DALException("Failed to create new wire type", e);
            dalException.printStackTrace();
            throw dalException;
        }
    }
}
