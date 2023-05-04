package dal.dao;

import be.Enum.SystemRole;
import be.SystemUser;
import dal.connectors.AbstractConnector;
import dal.connectors.SqlConnector;
import dal.interfaces.ISystemUsersAssignedToProjectsDAO;
import exceptions.DALException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SystemUsersAssignedToProjectsDAO implements ISystemUsersAssignedToProjectsDAO {
    private AbstractConnector connector;

    public SystemUsersAssignedToProjectsDAO() throws Exception {
        connector = new SqlConnector();
    }

    public SystemUsersAssignedToProjectsDAO(AbstractConnector connector) {
        this.connector = connector;
    }

    @Override
    public List<SystemUser> getSystemUsersAssignedToProject(int projectId) throws Exception {
        String sql = "SELECT " +
                "SystemUser.Email, SystemUser.RoleName, SystemUser.UserName " +
                "FROM SystemUser " +
                "WHERE SystemUser.Email IN " +
                "(SELECT SystemUsersAssignedToProjects.SystemUserEmail " +
                "FROM SystemUsersAssignedToProjects " +
                "WHERE SystemUsersAssignedToProjects.ProjectID = ?);";

        try (Connection conn = connector.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            List<SystemUser> assignedSystemUsers = new ArrayList<>();

            statement.setInt(1, projectId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String email = resultSet.getString("Email");
                SystemRole role = SystemRole.getRole(resultSet.getString("RoleName"));
                String name = resultSet.getString("UserName");

                SystemUser assignedSystemUser = new SystemUser(email, role, name);

                assignedSystemUsers.add(assignedSystemUser);
            }

            return assignedSystemUsers;
        }
        catch (SQLException e) {
            e.printStackTrace(); //TODO Replace with logging to database table
            throw new DALException("Failed to retrieve users assigned to this project", e);
        }
    }

    @Override
    public boolean deleteSystemUserAssignedToProject(int projectId, String systemUserEmail) throws Exception {
        String sql = "DELETE FROM SystemUsersAssignedToProjects WHERE ProjectID = ? AND SystemUserEmail = ?";

        try (Connection conn = connector.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, projectId);
            statement.setString(2, systemUserEmail);

            int deletedTuples = statement.executeUpdate();

            return deletedTuples > 0;
        }
        catch (SQLException e) {
            DALException dalException = new DALException("Failed to delete user assigned to this project", e);
            dalException.printStackTrace(); //TODO replace with log to the database.

            throw dalException;
        }
    }

    @Override
    public boolean assignSystemUserToProject(int projectId, String systemUserEmail) throws Exception {
        String sql = "INSERT INTO SystemUsersAssignedToProjects (ProjectID, SystemUserEmail) VALUES (?, ?)";

        try (Connection conn = connector.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, projectId);
            statement.setString(2, systemUserEmail);

            int insertedTuples = statement.executeUpdate();

            return insertedTuples == 1;
        }
        catch (SQLException e) {
            DALException dalException = new DALException("Failed to assign user to this project", e);
            dalException.printStackTrace(); //TODO replace with log to the database.

            throw dalException;
        }
    }

    @Override
    public List<SystemUser> getSystemUsersNotAssignedToProject(int projectId) throws Exception {
        String sql = "SELECT " +
                "SystemUser.Email, SystemUser.RoleName, SystemUser.UserName " +
                "FROM SystemUser " +
                "WHERE SystemUser.Email NOT IN " +
                "(SELECT SystemUsersAssignedToProjects.SystemUserEmail " +
                "FROM SystemUsersAssignedToProjects " +
                "WHERE SystemUsersAssignedToProjects.ProjectID = ?);";

        try (Connection conn = connector.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql)) {

            List<SystemUser> systemUsersNotAssigned = new ArrayList<>();

            statement.setInt(1, projectId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String email = resultSet.getString("Email");
                String roleName = resultSet.getString("RoleName");
                String username = resultSet.getString("UserName");

                SystemUser systemUserNotAssigned = new SystemUser(email, SystemRole.getRole(roleName), username);

                systemUsersNotAssigned.add(systemUserNotAssigned);
            }

            return systemUsersNotAssigned;
        }
        catch (SQLException e) {
            DALException dalException = new DALException("Failed to retrieve users not assigned to this project", e);
            dalException.printStackTrace(); //TODO replace with log to the database.

            throw dalException;
        }
    }
}
