package dal.dao;

import be.Photo;
import dal.connectors.AbstractConnector;
import dal.connectors.SqlConnector;
import dal.interfaces.IPhotoDAO;
import exceptions.DALException;
import javafx.scene.image.Image;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static gui.util.ImageConverter.convertToFxImage;

public class PhotoDAO implements IPhotoDAO {

    private AbstractConnector connector;

    public PhotoDAO() throws Exception {
        connector = new SqlConnector();
    }


    @Override
    public Photo uploadPhoto(Photo photo) throws Exception {
        Photo newPhoto = null;

        String sql = "INSERT INTO Photo (InstallationID, Image, Description) VALUES (?, ?, ?)";
        System.out.println("Installation ID: " + photo.getInstallationID());

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, photo.getInstallationID());
            statement.setBytes(2, photo.getPhotoBytes());
            statement.setString(3, photo.getDescription());


            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();

            if (resultSet.next()) {
                int ID = resultSet.getInt(1);

                Image imgPhoto = convertToFxImage(photo.getPhotoBytes());

                newPhoto = new Photo(ID, photo.getInstallationID(), imgPhoto, photo.getDescription());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DALException("Failed to upload", e);

        }
        return newPhoto;
    }

    @Override
    public Photo updatePhoto(Photo photo) throws Exception {

        Photo updatedPhoto = null;

        String sql = "UPDATE Photo SET InstallationID=?, Image=?, Description=? WHERE ID=?;";

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, photo.getInstallationID());
            statement.setBytes(2, photo.getPhotoBytes());
            statement.setString(3, photo.getDescription());
            statement.setInt(4, photo.getID());


            statement.executeUpdate();

            updatedPhoto = photo;

        } catch (SQLException e) {
            DALException dalException = new DALException("Failed to update photo", e);
            dalException.printStackTrace(); //TODO replace with log to the database.

            throw dalException;
        }
        return updatedPhoto;
    }

    @Override
    public List<Photo> getPhotoFromInstallation(int installationID) throws Exception {

        List<Photo> allPhotos = new ArrayList<>();

        String sql = "SELECT * FROM Photo WHERE InstallationID =?;";

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, installationID);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int ID = resultSet.getInt("ID");
                byte[] photoBytes = resultSet.getBytes("Image");
                String description = resultSet.getString("Description");

                Image imgPhoto = convertToFxImage(photoBytes);

                Photo photo = new Photo(ID, installationID, imgPhoto, description);

                allPhotos.add(photo);
            }

        } catch (SQLException e) {
        e.printStackTrace();
        throw new DALException("Failed to read photo from installation", e);
    }
        return allPhotos;
    }




    @Override
    public void deletePhoto(Photo photo) throws Exception {

        String sql = "DELETE FROM Photo WHERE ID=?;";

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, photo.getID());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DALException("Failed to delete photo", e);
        }
    }

     /*
    public List<Photo> getAllModifiedPhotos(Timestamp lastCheck) throws Exception {
        List<Photo> allPhotos = new ArrayList<>();

        String sql = "SELECT * FROM Photo WHERE InstallationID =? AND LastModified>?;";

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setTimestamp(1, lastCheck);

            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {

                int ID = resultSet.getInt("ID");
                byte[] photoBytes = resultSet.getBytes("Image");
                String description = resultSet.getString("Description");

                Image imgPhoto = convertToFxImage(photoBytes);

                Photo photo = new Photo(ID, installationID, imgPhoto, description);

                allPhotos.add(photo);

            }
        }

        return allPhotos;
    }

      */
}



