package dal.dao;

import be.SystemUser;
import dal.connectors.AbstractConnector;
import dal.connectors.SqlConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SystemUserDAO {

    private AbstractConnector connector;

    public SystemUserDAO() throws Exception {
        connector = new SqlConnector();
    }


    public SystemUser systemUserValidLogin(SystemUser user) throws Exception {
        SystemUser systemUser = null;

        String sql = "SELECT * FROM SystemUsers WHERE Email = ?";

        try (Connection conn = connector.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, user.getEmail());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String firstName = resultSet.getString("FirstName");
                String lastName = resultSet.getString("LastName");
                String password = resultSet.getString("Password");
                //todo create systemuser

               // List<Role> systemUsersRoles = retrieveRolesForSystemUser(conn, systemUser);

                //systemUser.getRoles().addAll(systemUsersRoles);
            }

            return systemUser;
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Failed to validate login", e);
        }
    }
}
