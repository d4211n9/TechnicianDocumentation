package dal.dao;

import be.Photo;
import dal.connectors.AbstractConnector;
import dal.connectors.SqlConnector;
import dal.interfaces.IPhotoDAO;
import exceptions.DALException;
import javafx.scene.image.Image;
import jdk.jfr.Description;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhotoDAO implements IPhotoDAO {

    private AbstractConnector connector;

    public PhotoDAO() throws Exception {
        connector = new SqlConnector();
    }


    @Override
    public Photo uploadPhoto(Photo photo) throws Exception {
        Photo newPhoto = null;

        InputStream is = new FileInputStream(new File("resources/images/WUAV_logo.jpg"));

        //InputStream is = new FileInputStream(new Image(photo.getPhoto()));

        String sql = "INSERT INTO Photo (InstallationID, Image, Description) VALUES (?, ?, ?)";

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, photo.getInstallationID());
            statement.setBinaryStream(2, is);
            statement.setString(3, photo.getDescription());


            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();

            if (resultSet.next()) {
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
    public List<Photo> getPhotoFromInstallation(int installationID) throws Exception {

        List<Photo> allPhotos = new ArrayList<>();

        String sql = "SELECT * FROM Photo WHERE InstallationID =?;";

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, installationID);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int ID = resultSet.getInt(1);
                byte[] photoBytes = resultSet.getBytes(3);
                String description = resultSet.getString(4);

                Photo photo = new Photo(ID, installationID, photoBytes, description);

                allPhotos.add(photo);
            }

        } catch (SQLException e) {
        e.printStackTrace();
        throw new DALException("Failed to read photo from installation", e);
    }
        return allPhotos;
    }


        @Override
    public Photo deletePhoto (Photo photo) throws Exception {

            Photo deletedPhoto = null;

            String sql = "DELETE FROM Photo WHERE ID=?;";

            try (Connection connection = connector.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {

                statement.setInt(1, photo.getID());
                statement.executeUpdate();

                deletedPhoto = photo;

            } catch (SQLException e) {
                e.printStackTrace();
                throw new DALException("Failed to upload", e);
            }
            return deletedPhoto;
        }
    }



   /* public byte[] convertToBytes(Photo photo) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutput out = new ObjectOutputStream(bos)) {
            out.writeObject(photo);
            return bos.toByteArray();
        }
    }*/


