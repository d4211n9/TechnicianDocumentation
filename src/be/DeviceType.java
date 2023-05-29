package be;

public class DeviceType {
    private String name;
    private String imagePath;
    private boolean hasLoginDetails;

    public DeviceType(String name, String imagePath, boolean hasLoginDetails) {
        this.name = name;
        this.imagePath = imagePath;
        this.hasLoginDetails = hasLoginDetails;
    }

    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public boolean hasLoginDetails() {
        return hasLoginDetails;
    }
}
