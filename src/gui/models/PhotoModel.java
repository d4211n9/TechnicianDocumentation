package gui.models;

import be.Photo;
import bll.interfaces.IPhotoManager;
import bll.managers.PhotoManager;

import java.util.List;

public class PhotoModel {

    private IPhotoManager photoManager;

    public PhotoModel() throws Exception {
        this.photoManager = new PhotoManager();

    }

    public List<Photo> getPhotoFromInstallation(int installationID) throws Exception {
        return photoManager.getPhotoFromInstallation(installationID);

    }
}
