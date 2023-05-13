package dal.interfaces;

import be.Photo;
import java.util.List;

public interface IPhotoDAO {

    public Photo uploadPhoto(Photo photo) throws Exception;

    public List<Photo> getPhotoFromInstallation(int installationID) throws Exception;

    public Photo deletePhoto(Photo photo) throws Exception;

}
