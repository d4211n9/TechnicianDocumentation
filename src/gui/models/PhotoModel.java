package gui.models;

import be.Photo;
import bll.interfaces.IPhotoManager;
import bll.managers.PhotoManager;
import java.util.List;

public class PhotoModel implements Runnable {

    private IPhotoManager photoManager;

    public PhotoModel() throws Exception {
        this.photoManager = new PhotoManager();
    }

    public Photo uploadPhoto(Photo photo) throws Exception {
        return photoManager.uploadPhoto(photo);
    }

    public Photo uploadPhoto(Photo photo) throws Exception {
        return photoManager.uploadPhoto(photo);
    }

    public List<Photo> getPhotoFromInstallation(int installationID) throws Exception {
        return photoManager.getPhotoFromInstallation(installationID);
    }

    public Photo deletePhoto(Photo photo) throws Exception {
        return photoManager.deletePhoto(photo);
    }

    public void deletePhoto(Photo photo) throws Exception {
        photoManager.deletePhoto(photo);
    }

    @Override
    public void run() {
        //TODO
    }
}
