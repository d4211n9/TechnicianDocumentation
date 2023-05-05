package bll.managers;

import be.Project;
import be.SystemUser;
import bll.interfaces.IProjectManager;
import bll.util.Search;
import dal.facades.ProjectFacade;

import java.util.List;

public class ProjectManager implements IProjectManager {
    private ProjectFacade projectFacade;
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

    public List<SystemUser> getSystemUsersAssignedToProject(int projectId) throws Exception {
        return projectFacade.getSystemUsersAssignedToProject(projectId);
    }

    @Override
    public List<Project> search(List<Project> allProjects, String query) {
        return search.searchForString(allProjects, query);
    }
}
