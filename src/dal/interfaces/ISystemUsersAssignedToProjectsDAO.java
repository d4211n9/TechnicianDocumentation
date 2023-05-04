package dal.interfaces;

import be.SystemUser;

import java.util.List;

public interface ISystemUsersAssignedToProjectsDAO {
    List<SystemUser> getSystemUsersAssignedToProject(int projectId) throws Exception;

    boolean deleteSystemUserAssignedToProject(int projectId, String systemUserEmail) throws Exception;
    boolean assignSystemUserToProject(int projectId, String systemUserEmail) throws Exception;
    List<SystemUser> getSystemUsersNotAssignedToProject(int projectId) throws Exception;
}
