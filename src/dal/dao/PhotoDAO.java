package dal.dao;

import be.Photo;
import dal.connectors.AbstractConnector;
import dal.connectors.SqlConnector;
import dal.interfaces.IPhotoDAO;
import exceptions.DALException;
import gui.util.ImageConverter;
import javafx.scene.image.Image;
import jdk.jfr.Description;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
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

                ByteArrayInputStream bais = new ByteArrayInputStream(photo.getPhotoBytes());
                Image imgPhoto = convertToFxImage(ImageIO.read(bais));
                newPhoto = new Photo(ID, photo.getInstallationID(), imgPhoto, photo.getDescription());
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
                int ID = resultSet.getInt("ID");
                byte[] photoBytes = resultSet.getBytes("Image");
                String description = resultSet.getString("Description");

                ByteArrayInputStream bais = new ByteArrayInputStream(photoBytes);
                Image imgPhoto = convertToFxImage(ImageIO.read(bais));
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
        public void deletePhoto (Photo photo) throws Exception {

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
    }



   /* public byte[] convertToBytes(Photo photo) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutput out = new ObjectOutputStream(bos)) {
            out.writeObject(photo);
            return bos.toByteArray();
        }
    }*/


