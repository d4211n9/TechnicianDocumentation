package dal.dao;

import be.Enum.SystemRole;
import be.SystemUser;
import dal.connectors.AbstractConnector;
import dal.connectors.SqlConnector;
import dal.interfaces.ISystemUserAssignedToInstallationDAO;
import exceptions.DALException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SystemUserAssignedToInstallationDAO implements ISystemUserAssignedToInstallationDAO {
    private AbstractConnector connector;

    public SystemUserAssignedToInstallationDAO() throws Exception {
        this.connector = new SqlConnector();
    }

    public SystemUserAssignedToInstallationDAO(AbstractConnector connector) {
        this.connector = connector;
    }

    @Override
    public List<SystemUser> getSystemUsersAssignedToInstallation(int installationId) throws Exception {
        String sql = "SELECT " +
                "SystemUser.Email, SystemUser.RoleName, SystemUser.UserName " +
                "FROM SystemUser " +
                "WHERE SystemUser.Email IN " +
                "(SELECT SystemUserAssignedToInstallation.SystemUserEmail " +
                "FROM SystemUserAssignedToInstallation " +
                "WHERE SystemUserAssignedToInstallation.InstallationID = ?);";

        try (Connection conn = connector.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            List<SystemUser> assignedSystemUsers = new ArrayList<>();

            statement.setInt(1, installationId);

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
            DALException dalException = new DALException("Failed to retrieve users assigned to this installation", e);
            dalException.printStackTrace(); //TODO replace with log to the database.

            throw dalException;
        }
    }

    @Override
    public List<SystemUser> getSystemUsersNotAssignedToInstallation(int installationId) throws Exception {
        String sql = "SELECT " +
                "SystemUser.Email, SystemUser.RoleName, SystemUser.UserName " +
                "FROM SystemUser " +
                "WHERE SystemUser.Email NOT IN " +
                "(SELECT SystemUserAssignedToInstallation.SystemUserEmail " +
                "FROM SystemUserAssignedToInstallation " +
                "WHERE SystemUserAssignedToInstallation.InstallationID = ?);";

        try (Connection conn = connector.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            List<SystemUser> assignedSystemUsers = new ArrayList<>();

            statement.setInt(1, installationId);

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
            DALException dalException = new DALException("Failed to retrieve users not assigned to this installation", e);
            dalException.printStackTrace(); //TODO replace with log to the database.

            throw dalException;
        }
    }

    @Override
    public boolean assignSystemUserToInstallation(int installationId, String systemUserEmailToAssign) throws Exception {
        String sql = "INSERT INTO SystemUserAssignedToInstallation (InstallationID, SystemUserEmail) VALUES (?, ?)";

        try (Connection conn = connector.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, installationId);
            statement.setString(2, systemUserEmailToAssign);

            int insertedTuples = statement.executeUpdate();

            return insertedTuples == 1;
        }
        catch (SQLException e) {
            DALException dalException = new DALException("Failed to assign user to installation", e);
            dalException.printStackTrace(); //TODO replace with log to the database.

            throw dalException;
        }
    }

    @Override
    public boolean deleteSystemUserAssignedToInstallation(int installationId, String systemUserEmailToDelete) throws Exception {
        String sql = "DELETE FROM SystemUserAssignedToInstallation WHERE InstallationID = ? AND SystemUserEmail = ?";

        try (Connection conn = connector.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, installationId);
            statement.setString(2, systemUserEmailToDelete);

            int deletedTuples = statement.executeUpdate();

            return deletedTuples == 1;
        }
        catch (SQLException e) {
            DALException dalException = new DALException("Failed to delete user assigned to this installation", e);
            dalException.printStackTrace(); //TODO replace with log to the database.

            throw dalException;
        }
    }
}
