package dal.connectors;

public class SqlConnector extends AbstractConnector {
    public SqlConnector() throws Exception {
        super("resources/config/sql_dev_config", "Failed to connect the the server");
    }
}
