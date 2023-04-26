package dal.connectors;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

/**
 * Instructions:
 * Create a new config file in
 * a "db_config" package in the project root directory.
 *
 * The config file should look like this:
 * Server=insert IP
 * Name=insert database name
 * User=insert username
 * Password=insert password
 */
public abstract class AbstractConnector {
    private final String CONFIG;
    private final String errorMessage;
    private SQLServerDataSource dataSource;

    public AbstractConnector(String configPath, String errorMessage) throws Exception {
        this.CONFIG = configPath;
        this.errorMessage = errorMessage;

        try {
            connect(getConfig());
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new Exception(errorMessage, e);
        }
    }

    private void connect(DatabaseConfig config) {
        dataSource = new SQLServerDataSource();
        dataSource.setServerName(config.getServer());
        dataSource.setDatabaseName(config.getName());
        dataSource.setUser(config.getUser());
        dataSource.setPassword(config.getPassword());
        dataSource.setTrustServerCertificate(true);
    }

    private DatabaseConfig getConfig() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(CONFIG));

        String server = properties.getProperty("Server");
        String name = properties.getProperty("Name");
        String user = properties.getProperty("User");
        String password = properties.getProperty("Password");
        return new DatabaseConfig(server, name, user, password);
    }

    public Connection getConnection() throws Exception {
        try {
            return dataSource.getConnection();
        }
        catch (SQLServerException e) {
            e.printStackTrace();
            throw new Exception(errorMessage, e);
        }
    }

    public static void main(String[] args) throws Exception {

    }
}
