package gui.models;

import be.Photo;
import bll.interfaces.IPhotoManager;
import bll.managers.PhotoManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class PhotoModel {

    private IPhotoManager photoManager;

    public PhotoModel() throws Exception {
        this.photoManager = new PhotoManager();

    }

    public Photo uploadPhoto(Photo photo) throws Exception {
        return photoManager.uploadPhoto(photo);
    }

    public Task<ObservableList<Photo>> getPhotoFromInstallation(int installationID) {
        Task<ObservableList<Photo>> allPhotosTask = new Task<ObservableList<Photo>>() {
            @Override
            protected ObservableList<Photo> call() throws Exception {
                ObservableList<Photo> allPhotos = FXCollections.observableList
                        (photoManager.getPhotoFromInstallation(installationID));

                updateValue(allPhotos);

                return allPhotos;
            }
        };

        return allPhotosTask;
    }

    public Task<Boolean> updatePhoto(Photo updatedPhoto) {
        Task<Boolean> updatePhotoTask = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                boolean successfullyUpdatedPhoto = photoManager.updatePhoto(updatedPhoto) != null;

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

}
