package be;

import be.Enum.ProjectStatus;
import util.Searchable;

import java.sql.Timestamp;
import java.util.Date;

public class Project implements Searchable<Project> {
    private String name, clientName;
    private int ID;
    private Date created;
    private Client client;
    private String description;
    private Timestamp deleted;
    private Address projectAddress;
    private ProjectStatus status;

    public Project(String name, Client client, Address projectAddress, Date created, String description, ProjectStatus status) {
        this.client = client;
        this.name = name;
        this.clientName = client.getName();
        this.projectAddress = projectAddress;
        this.created = created;
        this.description = description;
        this.status = status;
    }

    public Project(int ID, String name, Client client, Address projectAddress, Date created, String description, ProjectStatus status) {
        this.ID = ID;
        this.client = client;
        this.name = name;
        this.clientName = client.getName();
        this.projectAddress = projectAddress;
        this.created = created;
        this.description = description;
        this.status = status;
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

    public Address getAddress() {
        return projectAddress;
    }
    public String getStreet() {
        return projectAddress.getStreet();
    }

    public String getPostalCode() {
        return projectAddress.getPostalCode();
    }

    public String getCity() {
        return projectAddress.getCity();
    }

    public String getName() {
        return name;
    }

    public Date getCreated() {
        return created;
    }

    @Override
    public Project search(String query) {
        String searchableFields = (ID + name + clientName + projectAddress + created).toLowerCase(); //TODO Re-add address after normalizing
        String lowerCaseQuery = query.toLowerCase();

        if (searchableFields.contains(lowerCaseQuery)) return this;

        return null;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) { this.description = description; }

    public Timestamp getDeleted() {
        return deleted;
    }

    public void setDeleted(Timestamp deleted) {
        this.deleted = deleted;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return ID + ": " + name + " " + clientName;
    }
}
