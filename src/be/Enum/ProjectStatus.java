package be.Enum;

public enum ProjectStatus {
    Done("Done"),
    InProgress("InProgress"),
    ToDo("ToDo");

    private final String status;
    ProjectStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static ProjectStatus getProjectStatus(String status) {
        if (status.equalsIgnoreCase(Done.getStatus())) return Done;

        if (status.equalsIgnoreCase(InProgress.getStatus())) return InProgress;

        if(status.equalsIgnoreCase(ToDo.getStatus())) return ToDo;

        return null;
    }
}
