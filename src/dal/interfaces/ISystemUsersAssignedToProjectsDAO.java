package dal.interfaces;

import be.SystemUser;

import java.util.List;

public interface ISystemUsersAssignedToProjectsDAO {
    List<SystemUser> getSystemUsersAssignedToProject(int projectId) throws Exception;

    void deleteSystemUserAssignedToProject(int projectId, String systemUserEmail) throws Exception;
}
