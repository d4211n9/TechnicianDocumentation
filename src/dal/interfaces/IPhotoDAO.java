package dal.interfaces;

import be.Photo;
import exceptions.DALException;

public interface IPhotoDAO {

    public Photo uploadPhoto(Photo photo) throws Exception;

    public Photo deletePhoto(Photo photo) throws DALException;





}
