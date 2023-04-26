package dal.connectors;

public class DatabaseConfig {
    private final String server;
    private final String name;
    private final String user;
    private final String password;

    public DatabaseConfig(String server, String name, String user, String password) {
        this.server = server;
        this.name = name;
        this.user = user;
        this.password = password;
    }

    public String getServer() {
        return server;
    }

    public String getName() {
        return name;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
