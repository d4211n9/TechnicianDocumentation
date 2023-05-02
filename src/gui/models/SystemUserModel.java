package gui.models;

import be.Enum.SystemRole;
import be.SystemUser;
import bll.interfaces.ISystemUserManager;
import bll.managers.SystemUserManager;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SystemUserModel {

    private ISystemUserManager systemUserManager;
    private static ObservableObjectValue<SystemUser> loggedInSystemUser;

    private List<SystemUser> allUsers;

    private String searchString;
    private ObservableList<SystemUser> filteredUserList;

    private  List<SystemUser> copyAllUsers;

    public SystemUserModel() throws Exception {
        loggedInSystemUser = new SimpleObjectProperty<>(null);
        systemUserManager = new SystemUserManager();

        allUsers = retrieveAllUsers();
        copyAllUsers = new ArrayList<>(allUsers);
        filteredUserList = FXCollections.observableList(copyAllUsers);
    }

    public List<SystemUser> retrieveAllUsers() throws Exception {
        return systemUserManager.getAllSystemUsers();
    }

    public boolean SystemUserValidLogin(SystemUser user) throws Exception {
        loggedInSystemUser = new SimpleObjectProperty<>(systemUserManager.systemUserValidLogin(user));
        return loggedInSystemUser.get() != null;
    }

    public ObservableValue<SystemUser> getLoggedInSystemUser() {
        return loggedInSystemUser;
    }

    public ObservableList<SystemUser> getAllUsers() {
        return filteredUserList;
    }

    public void search(String query){
        filteredUserList.clear();
        if(query != null) {
            searchString = query;
            filteredUserList.addAll(systemUserManager.search(allUsers, query));
        }else {
            filteredUserList.addAll(systemUserManager.search(allUsers, ""));
        }
    }

    public SystemUser createSystemUser(SystemUser user) throws Exception {
        SystemUser createdUser = systemUserManager.createSystemUser(user);
        if(createdUser != null){
            allUsers.add(createdUser);
            search(searchString);
        }
        return createdUser;
    }

    public ObservableList<SystemRole> getAllRoles(){
        ObservableList<SystemRole> list = FXCollections.observableList(Arrays.stream(SystemRole.values()).toList());
        return list;
    }

    public boolean updateSystemUser(SystemUser user, SystemUser originalUser) throws Exception{
        if(systemUserManager.updateSystemUser(user) != null){
            allUsers.remove(user);
            allUsers.add(originalUser);
            search(searchString);
            return true;
        }
        return false;
    }
}
