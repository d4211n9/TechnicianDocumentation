package be;

import util.Searchable;

import java.util.Date;

public class Project implements Searchable<Project> {
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

    @Override
    public Project search(String query) {
        String searchableFields = (clientID + name + location + created).toLowerCase();
        String lowerCaseQuery = query.toLowerCase();

        if (searchableFields.contains(lowerCaseQuery)) return this;

        return null;
    }
}
