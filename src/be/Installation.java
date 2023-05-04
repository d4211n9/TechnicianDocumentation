package be;

public class Installation {
    private String name, description;
    private int ID, isDoneInt;
    private byte[] drawingVarBinary;
    private boolean isDone;

    public Installation(String name, String description, byte[] drawingVarBinary, boolean isDone) {
        this.name = name;
        this.description = description;
        this.drawingVarBinary = drawingVarBinary;
        this.isDone = isDone;
        isDoneInt = isDone ? 1 : 0;
    }

    public Installation(int ID, String name, String description, byte[] drawingVarBinary, boolean isDone) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.drawingVarBinary = drawingVarBinary;
        this.isDone = isDone;
        isDoneInt = isDone ? 1 : 0;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    //TODO Check: usikker på om int eller boolean skal bruges i isDone getter...
    public boolean getIsDone() {
        return isDone;
    }

    public int getIsDoneInt() {
        isDoneInt = isDone ? 1 : 0;
        return isDoneInt;
    }
}
