package dal.interfaces;

import be.SystemUser;

import java.util.List;

public interface ISystemUserAssignedToInstallationDAO {
    List<SystemUser> getSystemUsersAssignedToInstallation(int installationId) throws Exception;
    List<SystemUser> getSystemUsersNotAssignedToInstallation(int installationId) throws Exception;
    boolean assignSystemUserToInstallation(int installationId, String systemUserEmailToAssign) throws Exception;
    boolean deleteSystemUserAssignedToInstallation(int installationId, String systemUserEmailToDelete) throws Exception;
}
