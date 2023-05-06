package bll.interfaces;

import be.Installation;
import be.SystemUser;

import java.util.List;

public interface IInstallationManager {
    Installation createInstallation(Installation installation) throws Exception;
    List<Installation> getInstallationsFromProject(int projectID) throws Exception;
    Installation updateInstallation(Installation installation) throws Exception;
    List<SystemUser> getSystemUsersAssignedToInstallation(int installationId) throws Exception;
    List<SystemUser> getSystemUsersNotAssignedToInstallation(int installationId) throws Exception;
    boolean assignSystemUserToInstallation(int installationId, String systemUserEmailToAssign) throws Exception;
    boolean deleteSystemUserAssignedToInstallation(int installationId, String systemUserEmailToDelete) throws Exception;
}
