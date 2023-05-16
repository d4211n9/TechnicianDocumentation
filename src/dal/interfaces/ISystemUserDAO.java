package dal.interfaces;

import be.SystemUser;

import java.sql.Timestamp;
import java.util.List;

public interface ISystemUserDAO {
    SystemUser systemUserValidLogin(SystemUser user) throws Exception;
    SystemUser createSystemUser(SystemUser user) throws Exception;
    List<SystemUser> getAllSystemUsers() throws Exception;
    SystemUser updateSystemUser(SystemUser user) throws Exception;

    List<SystemUser> getAllModifiedUsers(Timestamp lastUpdateTime) throws Exception;
}
