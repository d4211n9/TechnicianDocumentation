package be;


import be.Enum.SystemRole;

public class SystemUser {

    private String email;

    public SystemRole getRole() {
        return role;
    }

    public void setRole(SystemRole role) {
        this.role = role;
    }

    private SystemRole role;

    public SystemUser(String email, be.Enum.SystemRole role){
        this.email = email;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
