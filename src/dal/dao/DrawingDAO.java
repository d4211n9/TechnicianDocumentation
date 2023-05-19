package dal.dao;

import be.Drawing;
import dal.connectors.AbstractConnector;
import dal.connectors.SqlConnector;
import dal.interfaces.IDrawingDAO;
import exceptions.DALException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DrawingDAO implements IDrawingDAO {
    private AbstractConnector connector;

    public DrawingDAO() throws Exception {
        connector = new SqlConnector();
    }

    public DrawingDAO(AbstractConnector connector) {
        this.connector = connector;
    }

    @Override
    public Drawing getDrawingFromInstallationId(int installationId) throws Exception {
        String sql = "SELECT " +
                "Drawing.ID AS 'DrawingID', Drawing.Image AS 'DrawingImage' " +
                "FROM Drawing " +
                "WHERE " +
                "Drawing.ID = (SELECT Installation.DrawingID FROM Installation WHERE Installation.ID = ?)";

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, installationId);

            ResultSet resultSet = statement.executeQuery();

            Drawing drawing = null;

            if (resultSet.next()) {
                int id = resultSet.getInt("DrawingID");
                byte[] image = resultSet.getBytes("DrawingImage");

                drawing = new Drawing(id, image);
            }

            return drawing;
        }
        catch (SQLException e) {
            DALException dalException = new DALException("Failed to retrieve drawing", e);
            dalException.printStackTrace();
            throw dalException;
        }
    }
}
