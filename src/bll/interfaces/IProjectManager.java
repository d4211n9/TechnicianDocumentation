package bll.interfaces;

import be.Project;
import be.SystemUser;

import java.sql.Timestamp;
import java.util.List;

public interface IProjectManager {

    Project createProject(Project project) throws Exception;

    List<Project> getAllProjects() throws Exception;

    Project softDeleteProject(Project project) throws Exception;

    Project updateProject(Project project) throws Exception;

    List<Project> search(List<Project> allProjects, String query);

    List<SystemUser> getSystemUsersAssignedToProject(int projectId) throws Exception;

    void deleteSystemUserAssignedToProject(int projectId, String systemUserEmail) throws Exception;

    void assignSystemUserToProject(int projectId, String systemUserEmail) throws Exception;

    List<Project> getModifiedProjects(Timestamp lastCheck) throws Exception;
}
