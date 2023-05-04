package bll.interfaces;

import be.Project;
import be.SystemUser;

import java.util.List;

public interface IProjectManager {
    Project createProject(Project project) throws Exception;
    List<Project> getAllProjects() throws Exception;
    List<Project> search(List<Project> allProjects, String query);
    List<SystemUser> getSystemUsersAssignedToProject(int projectId) throws Exception;
    void deleteSystemUserAssignedToProject(int projectId, String systemUserEmail) throws Exception;
}
