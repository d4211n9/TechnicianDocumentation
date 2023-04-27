package be;

import java.util.Date;

public class Project {
    private String client, projectName, location;
    private Date created;

    public Project(String client, String projectName, String location, Date created) {
        this.client = client;
        this.projectName = projectName;
        this.location = location;
        this.created = created;
    }

    public String getClient() {
        return client;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getLocation() {
        return location;
    }

    public Date getCreated() {
        return created;
    }
}
