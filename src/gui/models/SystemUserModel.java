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
import java.util.Objects;

public class SystemUserModel {

    private ISystemUserManager systemUserManager;
    private static ObservableObjectValue<SystemUser> loggedInSystemUser;

    private List<SystemUser> allUsers;

    private ObservableList<SystemUser> filteredUserList;

    public SystemUserModel() throws Exception {
        loggedInSystemUser = new SimpleObjectProperty<>(null);
        systemUserManager = new SystemUserManager();

        allUsers = retrieveAllUsers();
        List<SystemUser> copyAllUsers = new ArrayList<>();
        allUsers.forEach(systemUser -> copyAllUsers.add(systemUser));
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

        System.out.println(allUsers.size());
        if (!query.isBlank()) {
            filteredUserList.addAll(systemUserManager.search(allUsers, query));
        }
        else {
            filteredUserList.addAll(allUsers);
        }
    }

    public SystemUser createSystemUser(SystemUser user) throws Exception {
        SystemUser createdUser = systemUserManager.createSystemUser(user);
        if(createdUser != null){
            allUsers.add(createdUser);
            filteredUserList.add(createdUser);
        }
        return createdUser;
    }

    public ObservableList<SystemRole> getAllRoles(){
        ObservableList<SystemRole> list = FXCollections.observableList(Arrays.stream(SystemRole.values()).toList());
        return list;
    }
}
