package dal.dao;

import be.Wire;
import dal.connectors.AbstractConnector;
import dal.connectors.SqlConnector;
import dal.interfaces.IWireDAO;
import exceptions.DALException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class WireDAO implements IWireDAO {

    private AbstractConnector connector;

    public WireDAO() throws Exception {
        connector = new SqlConnector();
    }

    public Wire createWire(Wire wire) throws DALException {
        //todo make create methode
        //return null;

        Wire newWire = null;
        String sql = "INSERT INTO Wire " +
                "(StartX, StartY, EndX, EndY, WireType) " +
                "VALUES (?, ?, ?, ?, ?);";

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, (int) wire.getStartX());
            statement.setInt(2, (int) wire.getStartY());
            statement.setInt(3, (int) wire.getEndX());
            statement.setInt(4, (int) wire.getEndY());
            statement.setString(5, wire.getWireType().getName());

            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();

            if(resultSet.next()) {
                int id = resultSet.getInt(1);

                newWire = new Wire(id, wire.getStartX(), wire.getStartY(), wire.getEndX(), wire.getEndY(), wire.getWireType());
            }

        } catch (Exception e) {
            DALException dalException = new DALException("Failed to create device", e);
            dalException.printStackTrace();
            throw dalException;
        }
        return newWire;
    }


}
