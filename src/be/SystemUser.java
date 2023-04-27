package be;


import be.Enum.SystemRole;

public class SystemUser {

    private String email;
    private SystemRole role;

    private String password;

    public SystemUser(String email, SystemRole role){
        this.email = email;
        this.role = role;
    }

    public SystemUser(String email, String password, SystemRole role){
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }
    public SystemRole getRole() {
        return role;
    }


}
