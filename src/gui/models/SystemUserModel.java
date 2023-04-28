package gui.models;

import be.SystemUser;
import bll.interfaces.ISystemUserManager;
import bll.managers.SystemUserManager;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
<<<<<<< Updated upstream
=======

import java.util.ArrayList;
import java.util.Arrays;
>>>>>>> Stashed changes
import java.util.List;
import java.util.Objects;

public class SystemUserModel {

    private ISystemUserManager systemUserManager;
    private static ObservableObjectValue<SystemUser> loggedInSystemUser;
<<<<<<< Updated upstream
    private ObservableList<SystemUser> allUsers;
=======


    private List<SystemUser> allUsers;

    private ObservableList<SystemUser> filteredUserList;
>>>>>>> Stashed changes

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
}
