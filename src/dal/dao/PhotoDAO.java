package dal.dao;

import be.Photo;
import dal.connectors.AbstractConnector;
import dal.connectors.SqlConnector;
import dal.interfaces.IPhotoDAO;
import exceptions.DALException;

import java.sql.*;

public class PhotoDAO implements IPhotoDAO {

    private AbstractConnector connector;

    private PhotoDAO() throws Exception {
        connector = new SqlConnector();
    }


    @Override
    public Photo uploadPhoto(Photo photo) throws Exception {
        Photo newPhoto = null;

        String sql = "INSERT INTO Photo (InstallationID, Image, Description) VALUES (?, ?, ?)";

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, photo.getInstallationID());
            statement.setByte(2, photo.getPhoto());
            statement.setString(3, photo.getDescription());

            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();

            if(resultSet.next()) {
                int ID = resultSet.getInt(1);
                newPhoto = new Photo(ID, photo.getInstallationID(), photo.getPhoto(), photo.getDescription());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DALException("Failed to upload", e);
        }
        return newPhoto;
        }


    @Override
    public Photo deletePhoto(Photo photo) throws DALException {
        return null;
    }
}
