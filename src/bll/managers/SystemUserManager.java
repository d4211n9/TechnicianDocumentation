package bll.managers;

import be.SystemUser;
import bll.interfaces.ISystemUserManager;
import bll.util.BCrypt;
import bll.util.Search;
import dal.dao.SystemUserDAO;
import dal.interfaces.ISystemUserDAO;
import util.Searchable;


import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

public class SystemUserManager implements ISystemUserManager {
    ISystemUserDAO systemUserDAO;
    Search<SystemUser> search;
    public SystemUserManager() throws Exception {
        systemUserDAO = new SystemUserDAO();
        search = new Search<>();
    }

    @Override
    public SystemUser systemUserValidLogin(SystemUser user) throws Exception {

        SystemUser systemUser = systemUserDAO.systemUserValidLogin(user);

        if(BCrypt.checkpw(user.getPassword(), systemUser.getPassword())){
            return systemUser;
        }
        return null;
    }

    @Override
    public List<SystemUser> search(List<SystemUser> allUsers, String query) {
        return search.searchForString(allUsers, query);
    }

    @Override
    public SystemUser createSystemUser(SystemUser user) throws Exception {
        String salt = BCrypt.gensalt(10);
        String hashedPassword = BCrypt.hashpw(user.getPassword(), salt);
        user.setPassword(hashedPassword);

        return systemUserDAO.createSystemUser(user);
    }

    @Override
    public List<SystemUser> getAllSystemUsers() throws Exception {
        return systemUserDAO.getAllSystemUsers();
    }

    @Override
    public SystemUser updateSystemUser(SystemUser user) throws Exception {
        String salt = BCrypt.gensalt(10);
        String hashedPassword = BCrypt.hashpw(user.getPassword(), salt);
        user.setPassword(hashedPassword);

        return systemUserDAO.updateSystemUser(user);
    }

    @Override
    public List<SystemUser> getAllModifiedUsers(Timestamp lastUpdateTime) throws Exception {
        return systemUserDAO.getAllModifiedUsers(lastUpdateTime);
    }
}
