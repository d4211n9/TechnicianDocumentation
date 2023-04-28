package dal.dao;

import be.Enum.SystemRole;
import be.SystemUser;
import dal.connectors.AbstractConnector;
import dal.connectors.SqlConnector;
import dal.interfaces.ISystemUserDAO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SystemUserDAO implements ISystemUserDAO {

    private AbstractConnector connector;

    public SystemUserDAO() throws Exception {
        connector = new SqlConnector();
    }


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
                SystemRole systemRole = SystemRole.valueOf(role);

                String name = resultSet.getString("UserName");

                systemUser = new SystemUser(email, password, systemRole, name);
            }

            return systemUser;
        }
        catch (SQLException e) {
            e.printStackTrace();//todo wait for patricks error catcher
            throw new Exception("Failed to validate login", e);
        }
    }

    public List<SystemUser> getAllSystemUsers() throws Exception {

        ArrayList<SystemUser> allUsers = new ArrayList<>();

        try (Connection connection = connector.getConnection();
             Statement statement = connection.createStatement()) {

            String sql = "SELECT * FROM SystemUser;";

            ResultSet rs = statement.executeQuery(sql);

            while(rs.next()) {
                String email = rs.getString("Email");
                String role = rs.getString("RoleName");
                SystemRole systemRole = SystemRole.valueOf(role);

                String name= rs.getString("UserName");
                SystemUser systemUser = new SystemUser(email, systemRole, name);
                allUsers.add(systemUser);
            }
        } catch (Exception e){
            throw new Exception("Failed to retrieve all Users", e);
        }
        return allUsers;
    }

}
