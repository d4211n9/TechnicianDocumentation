package gui.models;

import be.Installation;
import bll.interfaces.IInstallationManager;
import bll.managers.InstallationManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
}
