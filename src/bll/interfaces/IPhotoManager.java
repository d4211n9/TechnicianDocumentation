package bll.interfaces;

import be.Photo;
import javafx.concurrent.Task;

import java.sql.Timestamp;
import java.util.List;

public interface IPhotoManager {

    Photo uploadPhoto(Photo photo) throws Exception;

    List<Photo> getPhotoFromInstallation(int installationID) throws Exception;

    Photo updatePhoto(Photo photo) throws Exception;

    void deletePhoto(Photo deletedPhoto) throws Exception;

    /*
    List<Photo> getAllModifiedPhotos(Timestamp lastCheck) throws Exception;
     */
}
