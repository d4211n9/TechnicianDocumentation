package dal.dao;

import be.Enum.SystemRole;
import be.SystemUser;
import dal.connectors.AbstractConnector;
import dal.connectors.SqlConnector;
import dal.interfaces.ISystemUserDAO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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


    @Override
    public SystemUser createSystemUser(SystemUser systemUser) throws Exception {
        SystemUser user = null;
        String sql = "INSERT INTO SystemUser " +
                "(Email, Password, RoleName, UserName)" +
                "VALUES (?, ?, ?, ?)";

        try (Connection conn = connector.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, systemUser.getEmail());
            System.out.println(systemUser.getEmail());
            statement.setString(2, systemUser.getPassword());

            statement.setString(3, systemUser.getRole().getRole());
            statement.setString(4, systemUser.getName());
            statement.executeUpdate();
            user = systemUser;
        }
        catch (Exception e) {
            throw new Exception("Failed create system user", e);
        }
        return user;
    }
}
