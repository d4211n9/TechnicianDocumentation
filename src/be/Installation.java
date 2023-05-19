package be;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Installation {
    private String name, description;
    private int ID, projectID, isDoneInt;
    private boolean isDone;
    private List<SystemUser> assignedSystemUsers;
    private Drawing drawing;
    private Timestamp deleted;


    public Installation(int projectID, String name, String description, Drawing drawing, boolean isDone) {
        this.projectID = projectID;
        this.name = name;
        this.description = description;
        this.drawing = drawing;
        this.isDone = isDone;
        isDoneInt = isDone ? 1 : 0;
        assignedSystemUsers = new ArrayList<>();
    }

    public Installation(int ID, int projectID, String name, String description, Drawing drawing, boolean isDone) {
        this.ID = ID;
        this.projectID = projectID;
        this.name = name;
        this.description = description;
        this.drawing = drawing;
        this.isDone = isDone;
        isDoneInt = isDone ? 1 : 0;
        assignedSystemUsers = new ArrayList<>();
    }

    public int getID() {
        return ID;
    }

    public int getProjectID() {
        return projectID;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Drawing getDrawing() {
        return drawing;
    }

    public void setDrawing(Drawing drawing) {
        this.drawing = drawing;
    }

    //TODO Check: usikker på om int eller boolean skal bruges i isDone getter...
    public boolean getIsDone() {
        return isDone;
    }

    public int getIsDoneInt() {
        isDoneInt = isDone ? 1 : 0;
        return isDoneInt;
    }

    public List<SystemUser> getAssignedSystemUsers() {
        return assignedSystemUsers;
    }

    public Timestamp getDeleted() {
        return deleted;
    }

    public void setDeleted(Timestamp deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return name + ", isDone=" + isDone;
    }
}
