package be;


import be.Enum.SystemRole;
import util.Searchable;

public class SystemUser implements Searchable<SystemUser> {

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


    @Override
    public SystemUser search(String query) {
        String searchableFields = (email + name + role.getRole()).toLowerCase();
        String lowerCaseQuery = query.toLowerCase();

        if (searchableFields.contains(lowerCaseQuery)) return this;

        return null;
    }

    public String getPassword() {
        return password;
    }

}
