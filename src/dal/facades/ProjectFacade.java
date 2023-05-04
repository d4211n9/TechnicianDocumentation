package dal.facades;

import be.Installation;
import be.Project;
import be.SystemUser;
import dal.dao.InstallationDAO;
import dal.dao.ProjectDAO;
import dal.dao.SystemUsersAssignedToProjectsDAO;
import dal.interfaces.IInstallationDAO;
import dal.interfaces.IProjectDAO;
import dal.interfaces.ISystemUsersAssignedToProjectsDAO;

import java.util.List;

public class ProjectFacade {
    private IProjectDAO projectDAO;
    private ISystemUsersAssignedToProjectsDAO systemUsersAssignedToProjectsDAO;
    private IInstallationDAO installationDAO;

    public ProjectFacade() throws Exception {
        projectDAO = new ProjectDAO();
        systemUsersAssignedToProjectsDAO = new SystemUsersAssignedToProjectsDAO();
        installationDAO = new InstallationDAO();
    }

    public Project createProject(Project project) throws Exception {
        return projectDAO.createProject(project);
    }

    public List<Project> getAllProjects() throws Exception {
        return projectDAO.getAllProjects();
    }

    public List<SystemUser> getSystemUsersAssignedToProject(int projectId) throws Exception {
        return systemUsersAssignedToProjectsDAO.getSystemUsersAssignedToProject(projectId);
    }

    public void deleteSystemUserAssignedToProject(int projectId, String systemUserEmail) throws Exception {
        systemUsersAssignedToProjectsDAO.deleteSystemUserAssignedToProject(projectId, systemUserEmail);
    }

    public void assignSystemUserToProject(int projectId, String systemUserEmail) throws Exception {
        systemUsersAssignedToProjectsDAO.assignSystemUserToProject(projectId, systemUserEmail);
    }

    public Installation createInstallation(Installation installation) throws Exception {
        return installationDAO.createInstallation(installation);
    }

    public List<Installation> getInstallationsFromProject(int projectID) throws Exception {
        return installationDAO.getInstallationsFromProject(projectID);
    }

    public Installation updateInstallation(Installation installation) throws Exception {
        return installationDAO.updateInstallation(installation);
    }
}