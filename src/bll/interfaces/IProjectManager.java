package bll.interfaces;

import be.Project;
import be.SystemUser;
import exceptions.DALException;

import java.sql.Timestamp;
import java.util.List;

public interface IProjectManager {

    Project createProject(Project project) throws Exception;

    List<Project> getAllProjects() throws Exception;

    Project updateProject(Project project) throws Exception;

    void deleteProject(Project deletedProject) throws Exception;

    List search(List allProjects, String query);

    List<SystemUser> getSystemUsersAssignedToProject(int projectId) throws Exception;

    void deleteSystemUserAssignedToProject(int projectId, String systemUserEmail) throws Exception;

    void assignSystemUserToProject(int projectId, String systemUserEmail) throws Exception;

    List<Project> getModifiedProjects(Timestamp lastCheck) throws Exception;

    List<SystemUser> getAllUserNotAssignedToProject(int projectId)  throws Exception;

    List<Project> getAllProjectsAssignedToUser(String systemUserID) throws DALException;
}
