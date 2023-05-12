package bll.interfaces;

import be.SystemUser;
import java.util.List;

public interface ISystemUserManager {

    SystemUser systemUserValidLogin(SystemUser user) throws Exception;
    List<SystemUser> search(List<SystemUser> allUsers, String query);
    SystemUser createSystemUser(SystemUser user) throws Exception;
    List<SystemUser> getAllSystemUsers() throws Exception;
    SystemUser updateSystemUser(SystemUser user) throws Exception;
    void deleteSystemUser(SystemUser deletedSystemUser) throws Exception;
}
