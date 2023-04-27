package be;


import be.Enum.SystemRole;

public class SystemUser {

    private String email;
    private SystemRole role;

    private String password;

    private String name;

    public SystemUser(String email, String password){
        this.email = email;
        this.password = password;
    }

    public SystemUser(String email, String password, SystemRole role, String name){
        this.email = email;
        this.password = password;
        this.role = role;
        this.name = name;
    }

    public SystemUser(String email, SystemRole role, String name){
        this.email = email;
        this.role = role;
        this.name = name;

    }

    public String getEmail() {
        return email;
    }
    public SystemRole getRole() {
        return role;
    }

    public String getName() {return name;}


}
