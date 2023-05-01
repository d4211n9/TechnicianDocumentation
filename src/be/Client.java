package be;

import util.Searchable;

public class Client implements Searchable {
    private String name, location, email, phone, type;
    private int ID;

    public Client(String name, String location, String email, String phone, String type) {
        this.name = name;
        this.location = location;
        this.email = email;
        this.phone = phone;
        this.type = type;
    }

    public Client(int ID, String name, String location, String email, String phone, String type) {
        this.ID = ID;
        this.name = name;
        this.location = location;
        this.email = email;
        this.phone = phone;
        this.type = type;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "#" + ID + ": " + name + " (" + type + ")";
    }

    @Override
    public Object search(String query) {
        String searchableFields = ("#" + ID + ": " + name + "(" + type + ")").toLowerCase();
        String lowerCaseQuery = query.toLowerCase();

        if (searchableFields.contains(lowerCaseQuery)) return this;

        return null;
    }
}
