package bll.managers;

import be.Project;
import bll.interfaces.IProjectManager;
import bll.util.Search;
import dal.dao.ProjectDAO;
import dal.interfaces.IProjectDAO;

import java.util.List;

public class ProjectManager implements IProjectManager {
    IProjectDAO projectDAO;
    Search search;

    public ProjectManager() throws Exception {
        projectDAO = new ProjectDAO();
        search = new Search();
    }

    @Override
    public Project createProject(Project project) throws Exception {
        return projectDAO.createProject(project);
    }

    @Override
    public List<Project> search(List<Project> allProjects, String query) {
        return search.searchForString(allProjects, query);
    }
}
