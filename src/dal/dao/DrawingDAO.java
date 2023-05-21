package dal.dao;

import be.Drawing;
import dal.connectors.AbstractConnector;
import dal.connectors.SqlConnector;
import dal.interfaces.IDrawingDAO;
import exceptions.DALException;

import java.sql.*;

public class DrawingDAO implements IDrawingDAO {
    private AbstractConnector connector;

    public DrawingDAO() throws Exception {
        connector = new SqlConnector();
    }

    public DrawingDAO(AbstractConnector connector) {
        this.connector = connector;
    }

    @Override
    public Drawing createDrawing(Drawing drawing) throws Exception {
        Drawing newDrawing = null;

        String sql = "INSERT INTO Drawing (ID, Image) VALUES (?, ?);";

        try (Connection conn = connector.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, drawing.getId());
            statement.setBytes(2, drawing.getImage());

            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();

            if (resultSet.next()) {
                int ID = resultSet.getInt(1);

                newDrawing = new Drawing(ID, drawing.getImage());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DALException("Failed to create drawing", e);
        }
        
        return newDrawing;
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

    @Override
    public void deleteDrawing(Drawing drawing) throws Exception {

        String sql = "DELETE FROM Drawing WHERE ID=?;";

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, drawing.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DALException("Failed to delete drawing", e);
        }

    }
}
