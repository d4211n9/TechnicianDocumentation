package bll.interfaces;

import be.Photo;

import java.util.List;

public interface IPhotoManager {

    Photo uploadPhoto(Photo photo) throws Exception;

    List<Photo> getPhotoFromInstallation(int installationID) throws Exception;

    public Photo deletePhoto(Photo photo) throws Exception;
}
