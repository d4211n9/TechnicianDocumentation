package dal.interfaces;

import be.SystemUser;

public interface ISystemUserDAO {

    SystemUser systemUserValidLogin(SystemUser user) throws Exception;
}
