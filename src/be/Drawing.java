package be;

import java.util.ArrayList;
import java.util.List;

public class Drawing {
    private int id;
    private byte[] image;
    private List<Device> devices;
    private List<Wire> wires;

    public Drawing(byte[] image) {
        this.image = image;
        initializeLists();
    }

    public Drawing(int id, byte[] image) {
        this.id = id;
        this.image = image;
        initializeLists();
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public List<Wire> getWires() {
        return wires;
    }

    private void initializeLists() {
        devices = new ArrayList<>();
        wires = new ArrayList<>();
    }
}
