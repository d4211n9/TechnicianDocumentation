package gui.models;

import be.Photo;
import bll.interfaces.IPhotoManager;
import bll.managers.PhotoManager;
import javafx.concurrent.Task;

import java.util.List;

public class PhotoModel implements Runnable {

    private List<Photo> allPhotos;

    private IPhotoManager photoManager;

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

    @Override
    public void run() {
        //TODO
    }
}
