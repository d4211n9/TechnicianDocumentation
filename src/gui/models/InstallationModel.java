package gui.models;

import be.Installation;
import be.SystemUser;
import bll.interfaces.IInstallationManager;
import bll.managers.InstallationManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class InstallationModel {
    private IInstallationManager installationManager;
    private ObservableList<Installation> allInstallations;

    public InstallationModel() throws Exception {
        installationManager = new InstallationManager();
    }

    public Installation createInstallation(Installation installation) throws Exception {
        return installationManager.createInstallation(installation);
    }

    public ObservableList<Installation> getAllInstallations(int projectID) throws Exception {
        allInstallations = FXCollections.observableList(installationManager.getInstallationsFromProject(projectID));
        return allInstallations;
    }

    public Installation updateInstallation(Installation installation) throws Exception {
        return installationManager.updateInstallation(installation);
    }

    public List<SystemUser> getSystemUsersAssignedToInstallation(int installationId) throws Exception {
        return installationManager.getSystemUsersAssignedToInstallation(installationId);
    }
    public List<SystemUser> getSystemUsersNotAssignedToInstallation(int installationId) throws Exception {
        return installationManager.getSystemUsersNotAssignedToInstallation(installationId);
    }
    public boolean assignSystemUserToInstallation(int installationId, String systemUserEmailToAssign) throws Exception {
        return installationManager.assignSystemUserToInstallation(installationId, systemUserEmailToAssign);
    }
    public boolean deleteSystemUserAssignedToInstallation(int installationId, String systemUserEmailToDelete) throws Exception {
        return installationManager.deleteSystemUserAssignedToInstallation(installationId, systemUserEmailToDelete);
    }
}
