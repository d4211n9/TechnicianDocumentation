package gui.models;

import be.Installation;
import be.SystemUser;
import bll.interfaces.IInstallationManager;
import bll.managers.InstallationManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.util.List;

public class InstallationModel implements Runnable {
    private IInstallationManager installationManager;
    private ObservableList<Installation> allInstallations;
    private Installation selectedInstallation;

    public InstallationModel() throws Exception {
        installationManager = new InstallationManager();
    }

    public Task<Installation> createInstallation(Installation installation) {
        Task<Installation> createInstallationTask = new Task<>() {
            @Override
            protected Installation call() throws Exception {
                Installation createdInstallation = installationManager.createInstallation(installation);

                updateValue(createdInstallation);

                return createdInstallation;
            }
        };
        return createInstallationTask;
    }

    public Task<ObservableList<Installation>> getAllInstallations(int projectID) {
        Task<ObservableList<Installation>> allInstallationsTask = new Task<>() {
            @Override
            protected ObservableList<Installation> call() throws Exception {
                allInstallations = FXCollections.observableList(installationManager.getInstallationsFromProject(projectID));

                updateValue(allInstallations);

                return allInstallations;
            }
        };

        return allInstallationsTask;
    }

    public Task<Installation> updateInstallation(Installation installation) {
        Task<Installation> updateInstallationTask = new Task<>() {
            @Override
            protected Installation call() throws Exception {
                Installation updatedInstallation = installationManager.updateInstallation(installation);

                updateValue(updatedInstallation);

                return updatedInstallation;
            }
        };

        return updateInstallationTask;
    }

    public Task<List<SystemUser>> getSystemUsersAssignedToInstallation(int installationId) {
        Task<List<SystemUser>> usersAssignedToInstallationTask = new Task<>() {
            @Override
            protected List<SystemUser> call() throws Exception {
                List<SystemUser> usersAssignedToInstallation =  installationManager
                        .getSystemUsersAssignedToInstallation(installationId);

                updateValue(usersAssignedToInstallation);

                return usersAssignedToInstallation;
            }
        };

        return usersAssignedToInstallationTask;
    }
    public Task<List<SystemUser>> getSystemUsersNotAssignedToInstallation(int installationId) {
        Task<List<SystemUser>> usersNotAssignedToInstallationTask = new Task<>() {
            @Override
            protected List<SystemUser> call() throws Exception {
                List<SystemUser> usersNotAssignedToInstallation =  installationManager
                        .getSystemUsersNotAssignedToInstallation(installationId);

                updateValue(usersNotAssignedToInstallation);

                return usersNotAssignedToInstallation;
            }
        };

        return usersNotAssignedToInstallationTask;
    }
    public Task<Boolean> assignSystemUserToInstallation(int installationId, String systemUserEmailToAssign) {
        Task<Boolean> assignUserToInstallationTask = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                boolean successfullyAssignedUserToInstallation = installationManager
                        .assignSystemUserToInstallation(installationId, systemUserEmailToAssign);

                updateValue(successfullyAssignedUserToInstallation);

                return successfullyAssignedUserToInstallation;
            }
        };
        return assignUserToInstallationTask;
    }

    public Task<Void> deleteInstallation(Installation deletedInstallation) throws Exception {
        Task<Void> deleteInstallationTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                installationManager.deleteInstallation(deletedInstallation);
                return null;
            }
        };

        return deleteInstallationTask;
    }

    public Task<Boolean> deleteSystemUserAssignedToInstallation(int installationId, String systemUserEmailToDelete) {
        Task<Boolean> deleteUserAssignedToInstallationTask = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                boolean successfullyDeletedUserAssignedToInstallation = installationManager
                        .deleteSystemUserAssignedToInstallation(installationId, systemUserEmailToDelete);

                updateValue(successfullyDeletedUserAssignedToInstallation);

                return successfullyDeletedUserAssignedToInstallation;
            }
        };

        return deleteUserAssignedToInstallationTask;
    }

    public Installation getSelectedInstallation() {
        return selectedInstallation;
    }

    public void setSelectedInstallation(Installation selectedInstallation) {
        this.selectedInstallation = selectedInstallation;
    }

    @Override
    public void run() {
        System.out.println("installation update");
    }
}
