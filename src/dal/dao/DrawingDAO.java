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

        String sql = "INSERT INTO Drawing (Image) VALUES (?);";

        try (Connection conn = connector.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setBytes(1, drawing.getImage());

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

        String sqlUpdateInstallation = "UPDATE Installation SET DrawingID = null WHERE DrawingID=?;";
        String sqlDeleteDrawing = "DELETE FROM Drawing WHERE ID=?;";

        Connection connection = connector.getConnection();
        connection.setAutoCommit(false);

        try (PreparedStatement stmtUpdateInstallation = connection.prepareStatement(sqlUpdateInstallation);
             PreparedStatement stmtDeleteDrawing = connection.prepareStatement(sqlDeleteDrawing)) {

            stmtUpdateInstallation.setInt(1, drawing.getId());
            stmtUpdateInstallation.executeUpdate();

            stmtDeleteDrawing.setInt(1, drawing.getId());
            stmtDeleteDrawing.executeUpdate();

            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Transaction is being rolled back");
            connection.rollback();
            throw new DALException("Failed to delete drawing", e);
        }
        connection.setAutoCommit(true);
    }



}
