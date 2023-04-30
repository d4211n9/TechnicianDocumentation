package bll.managers;

import be.Project;
import bll.interfaces.IProjectManager;
import dal.dao.ProjectDAO;
import dal.interfaces.IProjectDAO;

public class ProjectManager implements IProjectManager {
    IProjectDAO projectDAO;

    public ProjectManager() throws Exception {
        projectDAO = new ProjectDAO();
    }

    @Override
    public Project createProject(Project project) throws Exception {
        return projectDAO.createProject(project);
    }
}
