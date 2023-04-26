package be.Enum;

public enum SystemRole {
    Administrator("Administrator"),
    Technician("Technician"),
    ProjectManager("ProjectManager"),
    SalesPerson("SalesPerson");

    private final String role;

    SystemRole(String role) {
        this.role = role;
    }

    public String getRole() { return role; }
}
