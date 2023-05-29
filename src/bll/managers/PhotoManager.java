package bll.managers;

import be.Photo;
import bll.interfaces.IPhotoManager;
import dal.facades.ProjectFacade;

import java.util.List;

public class PhotoManager implements IPhotoManager {

    private ProjectFacade projectFacade;

    public PhotoManager() throws Exception {
        projectFacade = new ProjectFacade();
    }


    @Override
    public Photo uploadPhoto(Photo photo) throws Exception {
        return projectFacade.uploadPhoto(photo);
    }

    @Override
    public List<Photo> getPhotoFromInstallation(int installationID) throws Exception {
        return projectFacade.getPhotoFromInstallation(installationID);
    }

    @Override
    public Photo updatePhoto(Photo photo) throws Exception {
        return projectFacade.updatePhoto(photo);
    }

    @Override
    public void deletePhoto(Photo deletedPhoto) throws Exception {
        projectFacade.deletePhoto(deletedPhoto);
    }

    /*
    @Override
    public List<Photo> getAllModifiedPhotos(Timestamp lastCheck) throws Exception {
        return projectFacade.getAllModifiedPhotos(lastCheck);
    }
     */
}
