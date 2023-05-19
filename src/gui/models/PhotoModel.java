package gui.models;

import be.Photo;
import bll.interfaces.IPhotoManager;
import bll.managers.PhotoManager;
import javafx.concurrent.Task;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PhotoModel {

    private IPhotoManager photoManager;

    private List<Photo> allPhotos;

    //private Timestamp lastUpdatedTime;
    //private List<Photo> copyAllPhotos;

    public PhotoModel() throws Exception {
        this.photoManager = new PhotoManager();

    }

    public Photo uploadPhoto(Photo photo) throws Exception {
        return photoManager.uploadPhoto(photo);
    }

    public List<Photo> getPhotoFromInstallation(int installationID) throws Exception {
        return photoManager.getPhotoFromInstallation(installationID);
    }


    public Task<Boolean> updatePhoto(Photo updatedPhoto) {
        Task<Boolean> updatePhotoTask = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                boolean successfullyUpdatedPhoto = photoManager.updatePhoto(updatedPhoto) != null;

                if (successfullyUpdatedPhoto) {

                    allPhotos.removeIf(photo -> photo.getID() == updatedPhoto.getID());

                    allPhotos.add(updatedPhoto);

                }
                updateValue(successfullyUpdatedPhoto);

                return successfullyUpdatedPhoto;
            }
        };

        return updatePhotoTask;
    }





    public Task<Void> deletePhoto(Photo deletedPhoto) {
        Task<Void> deletePhoto = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                photoManager.deletePhoto(deletedPhoto);
                return null;
            }
        };

        return deletePhoto;
    }

    /*
    public List<Photo> retrieveAllPhotos() throws Exception {
        copyAllPhotos = new ArrayList<>(photoManager.getPhotoFromInstallation());
        return copyAllPhotos;
    }
     */

    /*
    @Override
    public void run() {
        System.out.println("photo update");

        List<Photo> updatedPhotos;
        try {
            updatedPhotos = photoManager.getAllModifiedPhotos(lastUpdatedTime);
            lastUpdatedTime.setTime(System.currentTimeMillis());

            if (updatedPhotos.size() > 0) {
                allPhotos = retrieveAllPhotos();
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
     */
}
