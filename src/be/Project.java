package be;

import java.util.Date;

public class Project {
    private String clientName, clientLocation, projectName;
    private Date created;

    public Project(Client client, String projectName, Date created) {
        clientName = client.getName();
        this.projectName = projectName;
        this.created = created;
    }

    public String getClient() {
        return clientName;
    }

    public String getLocation() {
        return clientLocation;
    }

    public String getProjectName() {
        return projectName;
    }

    public Date getCreated() {
        return created;
    }
}
