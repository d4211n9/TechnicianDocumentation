package bll.managers;

import be.Installation;
import be.SystemUser;
import bll.interfaces.IInstallationManager;
import dal.facades.DeleteFacade;
import dal.facades.ProjectFacade;

import java.util.List;

public class InstallationManager implements IInstallationManager {
    private ProjectFacade projectFacade;
    private DeleteFacade deleteFacade;

    public InstallationManager() throws Exception {
        projectFacade = new ProjectFacade();
        deleteFacade = new DeleteFacade();
    }

    @Override
    public Installation createInstallation(Installation installation) throws Exception {
        return projectFacade.createInstallation(installation);
    }

    @Override
    public List<Installation> getInstallationsFromProject(int projectID) throws Exception {
        return projectFacade.getInstallationsFromProject(projectID);
    }

    @Override
    public Installation updateInstallation(Installation installation) throws Exception {
        return projectFacade.updateInstallation(installation);
    }

    @Override
    public void deleteInstallation(Installation deletedInstallation) throws Exception {
        deleteFacade.deleteInstallation(deletedInstallation);
    }

    @Override
    public List<SystemUser> getSystemUsersAssignedToInstallation(int installationId) throws Exception {
        return projectFacade.getSystemUsersAssignedToInstallation(installationId);
    }

    @Override
    public List<SystemUser> getSystemUsersNotAssignedToInstallation(int installationId) throws Exception {
        return projectFacade.getSystemUsersNotAssignedToInstallation(installationId);
    }

    @Override
    public boolean assignSystemUserToInstallation(int installationId, String systemUserEmailToAssign) throws Exception {
        return projectFacade.assignSystemUserToInstallation(installationId, systemUserEmailToAssign);
    }

    @Override
    public boolean deleteSystemUserAssignedToInstallation(int installationId, String systemUserEmailToDelete) throws Exception {
        return projectFacade.deleteSystemUserAssignedToInstallation(installationId, systemUserEmailToDelete);
    }

}
