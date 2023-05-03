package dal.dao;

import be.SystemUser;
import dal.connectors.TestSqlConnector;
import dal.interfaces.ISystemUsersAssignedToProjectsDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

class SystemUsersAssignedToProjectsDAOTest {

    @DisplayName("Retrieve Assigned System Users")
    @Test
    void getSystemUsersAssignedToProject() {
        try {
            ISystemUsersAssignedToProjectsDAO systemUsersAssignedToProjectsDAO = new SystemUsersAssignedToProjectsDAO(new TestSqlConnector());

            List<SystemUser> assignedSystemUsers = systemUsersAssignedToProjectsDAO.getSystemUsersAssignedToProject(1);

            Assertions.assertEquals(2, assignedSystemUsers.size());
        } catch (Exception e) {
            Assertions.fail("Failed to retrieve assigned system users", e);
        }
    }
}