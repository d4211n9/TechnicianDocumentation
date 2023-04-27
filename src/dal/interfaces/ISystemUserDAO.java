package dal.interfaces;

import be.SystemUser;

import java.util.List;

public interface ISystemUserDAO {

    SystemUser systemUserValidLogin(SystemUser user) throws Exception;

    List<SystemUser> getAllSystemUsers() throws Exception;
}
