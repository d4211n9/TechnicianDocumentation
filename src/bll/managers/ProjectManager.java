package bll.managers;

import be.Project;
import be.SystemUser;
import bll.interfaces.IProjectManager;
import bll.util.Search;
import dal.facades.DeleteFacade;
import dal.facades.ProjectFacade;

import java.sql.Timestamp;
import java.util.List;

public class ProjectManager implements IProjectManager {
    private ProjectFacade projectFacade;
    private DeleteFacade deleteFacade;
    private Search search;

    public ProjectManager() throws Exception {
        projectFacade = new ProjectFacade();
        search = new Search();
    }

    @Override
    public Project createProject(Project project) throws Exception {
        return projectFacade.createProject(project);
    }

    @Override
    public List<Project> getAllProjects() throws Exception {
        return projectFacade.getAllProjects();
    }

    @Override
    public Project updateProject(Project project) throws Exception {
        return projectFacade.updateProject(project);
    }

    @Override
    public void deleteProject(Project deletedProject) throws Exception {
        deleteFacade.deleteProject(deletedProject);
    }

    @Override
    public List<SystemUser> getSystemUsersAssignedToProject(int projectId) throws Exception {
        return projectFacade.getSystemUsersAssignedToProject(projectId);
    }

    @Override
    public void deleteSystemUserAssignedToProject(int projectId, String systemUserEmail) throws Exception {
        projectFacade.deleteSystemUserAssignedToProject(projectId, systemUserEmail);
    }

    @Override
    public void assignSystemUserToProject(int projectId, String systemUserEmail) throws Exception {
        projectFacade.assignSystemUserToProject(projectId, systemUserEmail);
    }

    public List<SystemUser> getAllUserNotAssignedToProject(int projectId)  throws Exception{
        return projectFacade.getAllUserNotAssignedToProject(projectId);
    }

    @Override
    public List<Project> getModifiedProjects(Timestamp lastCheck) throws Exception {
        return projectFacade.getModifiedProjects(lastCheck);
    }

    @Override
    public Project softDeleteProject(Project project) throws Exception {
        return projectFacade.softDeleteProject(project);
    }

    @Override
    public List search(List allProjects, String query) {
        return search.searchForString(allProjects, query);
    }
}
