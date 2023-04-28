package dal.dao;

import be.Enum.SystemRole;
import be.SystemUser;
import dal.connectors.AbstractConnector;
import dal.connectors.SqlConnector;
import dal.interfaces.ISystemUserDAO;
import exceptions.DALException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SystemUserDAO implements ISystemUserDAO {

    private AbstractConnector connector;

    public SystemUserDAO() throws Exception {
        connector = new SqlConnector();
    }

    public SystemUserDAO(AbstractConnector connector) {
        this.connector = connector;
    }

    @Override
    public SystemUser systemUserValidLogin(SystemUser user) throws Exception {
        SystemUser systemUser = null;

        String sql = "SELECT * FROM SystemUser WHERE Email = ?";

        try (Connection conn = connector.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, user.getEmail());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String email = resultSet.getString("Email");
                String password = resultSet.getString("Password");
                String role = resultSet.getString("RoleName");
                String userName = resultSet.getString("UserName");

                SystemRole systemRole = SystemRole.valueOf(role);
                systemUser = new SystemUser(email, password, systemRole, userName);
            }

            return systemUser;
        }
        catch (SQLException e) {
            DALException dalException = new DALException("Failed to validate login", e);
            dalException.printStackTrace(); //TODO Log error in database
            throw dalException;
        }
    }
}
