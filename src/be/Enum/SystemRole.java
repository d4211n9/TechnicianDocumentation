package be.Enum;

import javax.lang.model.element.Name;
import java.util.jar.Attributes;

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

    public static SystemRole getRole(String roleName) {
        if (roleName.equalsIgnoreCase(Administrator.getRole())) return Administrator;

        if (roleName.equalsIgnoreCase(Technician.getRole())) return Technician;

        if (roleName.equalsIgnoreCase(ProjectManager.getRole())) return ProjectManager;

        if (roleName.equalsIgnoreCase(SalesPerson.getRole())) return SalesPerson;

        return null;
    }
}
