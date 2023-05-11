package dal.facades;

import be.Installation;
import be.Photo;
import be.Project;
import be.SystemUser;
import dal.dao.InstallationDAO;
import dal.dao.ProjectDAO;
import dal.dao.SystemUserAssignedToInstallationDAO;
import dal.dao.SystemUsersAssignedToProjectsDAO;
import dal.interfaces.*;
import exceptions.DALException;

import java.util.List;

public class ProjectFacade {
    private IProjectDAO projectDAO;
    private ISystemUsersAssignedToProjectsDAO systemUsersAssignedToProjectsDAO;
    private IInstallationDAO installationDAO;
    private ISystemUserAssignedToInstallationDAO systemUsersAssignedToInstallationDAO;
    private IPhotoDAO photoDAO;

    public ProjectFacade() throws Exception {
        projectDAO = new ProjectDAO();
        systemUsersAssignedToProjectsDAO = new SystemUsersAssignedToProjectsDAO();
        installationDAO = new InstallationDAO();
        systemUsersAssignedToInstallationDAO = new SystemUserAssignedToInstallationDAO();
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

    public Project softDeleteProject(Project project) throws Exception {
        return projectDAO.softDeleteProject(project);
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

    public List<SystemUser> getSystemUsersAssignedToInstallation(int installationId) throws Exception {
        return systemUsersAssignedToInstallationDAO.getSystemUsersAssignedToInstallation(installationId);
    }
    public List<SystemUser> getSystemUsersNotAssignedToInstallation(int installationId) throws Exception {
        return systemUsersAssignedToInstallationDAO.getSystemUsersNotAssignedToInstallation(installationId);
    }
    public boolean assignSystemUserToInstallation(int installationId, String systemUserEmailToAssign) throws Exception {
        return systemUsersAssignedToInstallationDAO.assignSystemUserToInstallation(installationId, systemUserEmailToAssign);
    }
    public boolean deleteSystemUserAssignedToInstallation(int installationId, String systemUserEmailToDelete) throws Exception {
        return systemUsersAssignedToInstallationDAO.deleteSystemUserAssignedToInstallation(installationId, systemUserEmailToDelete);
    }

    public Photo uploadPhoto(Photo photo) throws Exception {
        return photoDAO.uploadPhoto(photo);
    }

    public Photo deletePhoto(Photo photo) throws Exception{
        return photoDAO.deletePhoto(photo);
    }
}