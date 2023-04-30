package be;

import java.util.Date;

public class Project {
    private String location, name;
    private int ID, clientID;
    private Date created;

    public Project(String name, int clientID, String location, Date created) {
        this.name = name;
        this.clientID = clientID;
        this.location = location;
        this.created = created;
    }

    public Project(int ID, String name, int clientID, String location, Date created) {
        this.ID = ID;
        this.name = name;
        this.clientID = clientID;
        this.location = location;
        this.created = created;
    }

    public int getID() {
        return ID;
    }

    public int getClientID() {
        return clientID;
    }

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public Date getCreated() {
        return created;
    }
}
