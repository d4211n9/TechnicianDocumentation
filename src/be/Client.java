package be;

import util.Searchable;

import java.sql.Timestamp;

public class Client implements Searchable {
    private String name, email, phone, type;
    private int ID;
    private Timestamp deleted;
    private Address clientAddress;

    public Client(String name, Address clientAddress, String email, String phone, String type) {
        this.name = name;
        this.clientAddress = clientAddress;
        this.email = email;
        this.phone = phone;
        this.type = type;
    }

    public Client(int ID, String name, Address clientAddress, String email, String phone, String type) {
        this.ID = ID;
        this.name = name;
        this.clientAddress = clientAddress;
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

    public Address getAddress() {
        return clientAddress;
    }
    public String getStreet() {
        return clientAddress.getStreet();
    }

    public String getPostalCode() {
        return clientAddress.getPostalCode();
    }

    public String getCity() {
        return clientAddress.getCity();
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

    public Timestamp getDeleted() {
        return deleted;
    }

    public void setDeleted(Timestamp deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "#" + ID + ": " + name + " (" + type + ")";
    }

    @Override
    public Object search(String query) {
        String searchableFields = ("#" + ID + ": " + name + "(" + type + ") " + clientAddress).toLowerCase();
        String lowerCaseQuery = query.toLowerCase();

        if (searchableFields.contains(lowerCaseQuery)) return this;

        return null;
    }
}
