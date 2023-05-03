package dal.connectors;

public class TestSqlConnector extends AbstractConnector {
    public TestSqlConnector() throws Exception {
        super("resources/config/sql_test_config", "Failed to connect to test database");
    }
}
