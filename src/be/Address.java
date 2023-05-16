package be;

public class Address {
    private String street, postalCode, city;
    private int ID;

    public Address(String street, String postalCode, String city) {
        this.street = street;
        this.postalCode = postalCode;
        this.city = city;
    }

    public Address(int ID, String street, String postalCode, String city) {
        this.ID = ID;
        this.street = street;
        this.postalCode = postalCode;
        this.city = city;
    }

    public int getID() {
        return ID;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocation() {
        return this.toString();
    }

    @Override
    public String toString() {
        return street + ", " + postalCode + " " + city;
    }
}
