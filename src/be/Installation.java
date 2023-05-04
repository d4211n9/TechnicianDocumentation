package be;

public class Installation {
    private String name, description;
    private int ID, projectID, isDoneInt;
    private byte[] drawingBytes;
    private boolean isDone;

    public Installation(int projectID, String name, String description, byte[] drawingBytes, boolean isDone) {
        this.projectID = projectID;
        this.name = name;
        this.description = description;
        this.drawingBytes = drawingBytes;
        this.isDone = isDone;
        isDoneInt = isDone ? 1 : 0;
    }

    public Installation(int ID, int projectID, String name, String description, byte[] drawingBytes, boolean isDone) {
        this.ID = ID;
        this.projectID = projectID;
        this.name = name;
        this.description = description;
        this.drawingBytes = drawingBytes;
        this.isDone = isDone;
        isDoneInt = isDone ? 1 : 0;
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

    //TODO Check: usikker på om int eller boolean skal bruges i isDone getter...
    public boolean getIsDone() {
        return isDone;
    }

    public int getIsDoneInt() {
        isDoneInt = isDone ? 1 : 0;
        return isDoneInt;
    }

    @Override
    public String toString() {
        return name + ", isDone=" + isDone;
    }
}
