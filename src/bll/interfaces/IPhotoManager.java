package bll.interfaces;

import be.Photo;

public interface IPhotoManager {

    Photo uploadPhoto(Photo photo) throws Exception;

    public Photo deletePhoto(Photo photo) throws Exception;
}
