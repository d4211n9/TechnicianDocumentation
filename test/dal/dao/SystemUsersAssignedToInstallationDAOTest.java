package dal.dao;

import be.SystemUser;
import dal.connectors.TestSqlConnector;
import dal.interfaces.ISystemUserAssignedToInstallationDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

class SystemUsersAssignedToInstallationDAOTest {

    @DisplayName("Retrieve System Users Assigned to Installation")
    @Test
    void getSystemUsersAssignedToInstallation() {
        try {
            ISystemUserAssignedToInstallationDAO systemUsersAssignedToInstallationDAO = new SystemUserAssignedToInstallationDAO(new TestSqlConnector());

            List<SystemUser> assignedSystemUsers = systemUsersAssignedToInstallationDAO.getSystemUsersAssignedToInstallation(1);

            Assertions.assertTrue(assignedSystemUsers.size() > 0);
        }
        catch (Exception e) {
            Assertions.fail("Failed to retrieve system users assigned to installation", e);
        }
    }

    @DisplayName("Retrieve System Users not Assigned to Installation")
    @Test
    void getSystemUsersNotAssignedToInstallation() {
        try {
            ISystemUserAssignedToInstallationDAO systemUsersAssignedToInstallationDAO = new SystemUserAssignedToInstallationDAO(new TestSqlConnector());

            List<SystemUser> notAssignedSystemUsers = systemUsersAssignedToInstallationDAO.getSystemUsersAssignedToInstallation(1);

            Assertions.assertTrue(notAssignedSystemUsers.size() > 0);
        }
        catch (Exception e) {
            Assertions.fail("Failed to retrieve system users not assigned to installation", e);
        }
    }

    @DisplayName("Assign System User to Installation")
    @Test
    void assignSystemUserToInstallation() {
        try {
            ISystemUserAssignedToInstallationDAO systemUsersAssignedToInstallationDAO = new SystemUserAssignedToInstallationDAO(new TestSqlConnector());

            boolean isInsertedSuccessfully = systemUsersAssignedToInstallationDAO.assignSystemUserToInstallation(1, "stefewffan@gmail.com");

            Assertions.assertTrue(isInsertedSuccessfully);
        }
        catch (Exception e) {
            Assertions.fail("Failed to assign system user to installation", e);
        }
    }

    @DisplayName("Delete Assigned System User From Installation")
    @Test
    void deleteSystemUserAssignedToInstallation() {
        try {
            ISystemUserAssignedToInstallationDAO systemUsersAssignedToInstallationDAO = new SystemUserAssignedToInstallationDAO(new TestSqlConnector());

            boolean isDeletedSuccessfully = systemUsersAssignedToInstallationDAO.deleteSystemUserAssignedToInstallation(1, "micdra@easv365.dk");

            Assertions.assertTrue(isDeletedSuccessfully);
        }
        catch (Exception e) {
            Assertions.fail("Failed to delete system user assigned to installation", e);
        }
    }
}