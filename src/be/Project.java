package be;

import util.Searchable;

import java.util.Date;

public class Project implements Searchable<Project> {
    private String location, name, clientName;
    private int ID;
    private Date created;
    private Client client;

    public Project(String name, Client client, String location, Date created) {
        this.client = client;
        this.name = name;
        this.clientName = client.getName();
        this.location = location;
        this.created = created;
    }

    public Project(int ID, String name, Client client, String location, Date created) {
        this.ID = ID;
        this.client = client;
        this.name = name;
        this.clientName = client.getName();
        this.location = location;
        this.created = created;
    }

    public Client getClient() {
        return client;
    }

    public String getClientName() {
        return clientName;
    }

    public int getID() {
        return ID;
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
        String searchableFields = (ID + name + clientName + location + created).toLowerCase();
        String lowerCaseQuery = query.toLowerCase();

        if (searchableFields.contains(lowerCaseQuery)) return this;

        return null;
    }

    @Override
    public String toString() {
        return ID + ": " + name + " " + clientName;
    }
}
