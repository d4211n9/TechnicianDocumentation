package be;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Installation {
    private String name, description;
    private int ID, projectID, isDoneInt;
    private byte[] drawingBytes;
    private boolean isDone;
    private List<SystemUser> assignedSystemUsers;
    private Timestamp deleted;


    public Installation(int projectID, String name, String description, byte[] drawingBytes, boolean isDone) {
        this.projectID = projectID;
        this.name = name;
        this.description = description;
        this.drawingBytes = drawingBytes;
        this.isDone = isDone;
        isDoneInt = isDone ? 1 : 0;
        assignedSystemUsers = new ArrayList<>();
    }

    public Installation(int ID, int projectID, String name, String description, byte[] drawingBytes, boolean isDone) {
        this.ID = ID;
        this.projectID = projectID;
        this.name = name;
        this.description = description;
        this.drawingBytes = drawingBytes;
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

    public byte[] getDrawingBytes() {
        return drawingBytes;
    }

    public void setDrawingBytes(byte[] drawingBytes) {
        this.drawingBytes = drawingBytes;
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
