package be;

import javafx.scene.image.Image;

public class Photo {

    private int ID, installationID;
    private Image photo;
    private String description;
    private byte[] photoBytes;



    public Photo (int ID, int installationID, Image photo, String description){
        this.ID = ID;
        this.installationID = installationID;
        this.photo = photo;
        this.description = description;
    }

    public Photo (int installationID, byte[] photoBytes, String description) {
        this.installationID = installationID;
        this.photoBytes = photoBytes;
        this.description = description;
    }


    public byte[] getPhotoBytes() {
        return photoBytes;
    }

    public void setPhotoBytes(byte[] photoBytes) {
        this.photoBytes = photoBytes;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getInstallationID() {
        return installationID;
    }

    public void setInstallationID(int installationID) {
        this.installationID = installationID;
    }

    public Image getPhoto() {
        return photo;
    }

    public void setPhoto(Image photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
