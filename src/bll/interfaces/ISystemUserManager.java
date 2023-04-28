package bll.interfaces;

import be.SystemUser;
import java.util.List;

public interface ISystemUserManager {

    SystemUser systemUserValidLogin(SystemUser user) throws Exception;

    List<SystemUser> getAllSystemUsers() throws Exception;
}
