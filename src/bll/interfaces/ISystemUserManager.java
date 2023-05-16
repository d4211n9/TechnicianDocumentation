package bll.interfaces;

import be.SystemUser;

import java.sql.Timestamp;
import java.util.List;

public interface ISystemUserManager {

    SystemUser systemUserValidLogin(SystemUser user, boolean rememberLogin) throws Exception;
    List<SystemUser> search(List<SystemUser> allUsers, String query);
    SystemUser createSystemUser(SystemUser user) throws Exception;
    List<SystemUser> getAllSystemUsers() throws Exception;
    SystemUser updateSystemUser(SystemUser user) throws Exception;
    void deleteSystemUser(SystemUser deletedSystemUser) throws Exception;
    List<SystemUser> getAllModifiedUsers(Timestamp lastUpdateTime) throws Exception;
    SystemUser isLoginRemembered();
    void deleteRememberedLogin();
}
