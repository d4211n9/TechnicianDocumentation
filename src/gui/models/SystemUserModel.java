package gui.models;

import be.SystemUser;
import bll.interfaces.ISystemUserManager;
import bll.managers.SystemUserManager;

public class SystemUserModel {

    private ISystemUserManager systemUserManager;

    public SystemUserModel() throws Exception {
        systemUserManager = new SystemUserManager();
    }

    public SystemUser SystemUserValidLogin(SystemUser user) throws Exception {
        return systemUserManager.systemUserValidLogin(user);
    }
}
