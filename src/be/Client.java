package be;

public class Client {
    private String name, email, phone, type;
    private int ID;

    public Client(String name, String email, String phone, String type) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.type = type;
    }

    public Client(int ID, String name, String email, String phone, String type) {
        this.ID = ID;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.type = type;
    }

    public String getName() {
        return name;
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

    public int getID() {
        return ID;
    }
}
