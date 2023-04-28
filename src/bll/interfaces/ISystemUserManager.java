package bll.interfaces;

import be.SystemUser;

public interface ISystemUserManager {


    SystemUser systemUserValidLogin(SystemUser user) throws Exception;
}
