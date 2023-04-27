package be;

import java.util.ArrayList;
import java.util.List;

public class Client {
    private String name, location, email, phone;

    public Client(String name, String location, String email, String phone) {
        this.name = name;
        this.location = location;
        this.email = email;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

}
