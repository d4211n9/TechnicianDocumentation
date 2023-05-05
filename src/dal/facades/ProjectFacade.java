package dal.facades;

import be.Project;
import be.SystemUser;
import dal.dao.ProjectDAO;
import dal.dao.SystemUsersAssignedToProjectsDAO;
import dal.interfaces.IProjectDAO;
import dal.interfaces.ISystemUsersAssignedToProjectsDAO;

import java.util.List;

public class ProjectFacade {
    private IProjectDAO projectDAO;
    private ISystemUsersAssignedToProjectsDAO systemUsersAssignedToProjectsDAO;

    public ProjectFacade() throws Exception {
        projectDAO = new ProjectDAO();
        systemUsersAssignedToProjectsDAO = new SystemUsersAssignedToProjectsDAO();
    }

    public Project createProject(Project project) throws Exception {
        return projectDAO.createProject(project);
    }

    public List<Project> getAllProjects() throws Exception {
        return projectDAO.getAllProjects();
    }

    public Project updateProject(Project project) throws Exception {
        return projectDAO.updateProject(project);
    }

    public List<SystemUser> getSystemUsersAssignedToProject(int projectId) throws Exception {
        return systemUsersAssignedToProjectsDAO.getSystemUsersAssignedToProject(projectId);
    }
}
