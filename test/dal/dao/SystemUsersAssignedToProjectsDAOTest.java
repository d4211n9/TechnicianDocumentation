package dal.dao;

import be.SystemUser;
import dal.connectors.TestSqlConnector;
import dal.interfaces.ISystemUsersAssignedToProjectsDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

class SystemUsersAssignedToProjectsDAOTest {

    @DisplayName("Retrieve System Users Assigned to Project")
    @Test
    void getSystemUsersAssignedToProject() {
        try {
            ISystemUsersAssignedToProjectsDAO systemUsersAssignedToProjectsDAO = new SystemUsersAssignedToProjectsDAO(new TestSqlConnector());

            List<SystemUser> assignedSystemUsers = systemUsersAssignedToProjectsDAO.getSystemUsersAssignedToProject(1);

            Assertions.assertEquals(2, assignedSystemUsers.size());
        } catch (Exception e) {
            Assertions.fail(e.getMessage(), e);
        }
    }

    @DisplayName("Delete System User Assigned to Project")
    @Test
    void deleteSystemUserAssignedToProject() {
        try {
            ISystemUsersAssignedToProjectsDAO systemUsersAssignedToProjectsDAO = new SystemUsersAssignedToProjectsDAO(new TestSqlConnector());

            boolean isSuccess = systemUsersAssignedToProjectsDAO.deleteSystemUserAssignedToProject(1, "steffan@gmail.com");

            Assertions.assertTrue(isSuccess);
        }
        catch (Exception e) {
            Assertions.fail(e.getMessage(), e);
        }
    }

    @DisplayName("Assign System User to Project")
    @Test
    void assignSystemUserToProject() {
        try {
            ISystemUsersAssignedToProjectsDAO systemUsersAssignedToProjectsDAO = new SystemUsersAssignedToProjectsDAO(new TestSqlConnector());

            boolean isSuccess = systemUsersAssignedToProjectsDAO.assignSystemUserToProject(1, "patand01twin@easv365.dk");

            Assertions.assertTrue(isSuccess);
        }
        catch (Exception e) {
            Assertions.fail(e.getMessage(), e);
        }
    }

    @DisplayName("Retrieve System Users Not Assigned to Project")
    @Test
    void getSystemUsersNotAssignedToProject() {
        try {
            ISystemUsersAssignedToProjectsDAO systemUsersAssignedToProjectsDAO = new SystemUsersAssignedToProjectsDAO(new TestSqlConnector());

            List<SystemUser> systemUsersNotAssigned = systemUsersAssignedToProjectsDAO.getSystemUsersNotAssignedToProject(1);

            boolean isCorrectUser = false;

            for (SystemUser systemUser : systemUsersNotAssigned) {
                if (systemUser.getEmail().equals("patand01@easv365.dk")) {
                    isCorrectUser = true;
                    break;
                }
            }

            Assertions.assertTrue(isCorrectUser);
        }
        catch (Exception e) {
            Assertions.fail(e.getMessage(), e);
        }
    }
}