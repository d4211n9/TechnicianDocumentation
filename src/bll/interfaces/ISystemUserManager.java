package bll.interfaces;

import be.SystemUser;

public interface ISystemUserManager {


    public SystemUser systemUserValidLogin(SystemUser user) throws Exception;
}
