package bll.managers;

import be.SystemUser;
import bll.interfaces.ISystemUserManager;
import dal.dao.SystemUserDAO;
import dal.interfaces.ISystemUserDAO;

public class SystemUserManager implements ISystemUserManager {
    ISystemUserDAO systemUserDAO;

    public SystemUserManager() throws Exception {
        systemUserDAO = new SystemUserDAO();
    }

    @Override
    public SystemUser systemUserValidLogin(SystemUser user) throws Exception {
        return systemUserDAO.systemUserValidLogin(user);
        //todo should check the encryption with b crypt from this method.
    }
}
