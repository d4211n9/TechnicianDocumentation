package bll.managers;

import be.Photo;
import bll.interfaces.IPhotoManager;
import dal.facades.ProjectFacade;

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
    public Photo deletePhoto(Photo photo) throws Exception {
        return projectFacade.deletePhoto(photo);
    }
}
