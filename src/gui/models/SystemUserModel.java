package gui.models;

import be.Enum.SystemRole;
import be.SystemUser;
import bll.interfaces.ISystemUserManager;
import bll.managers.SystemUserManager;
import gui.util.TaskExecutor;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

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

    public Task<Boolean> SystemUserValidLogin(SystemUser user) {
        Task<Boolean> validLoginTask = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                loggedInSystemUser = new SimpleObjectProperty<>(systemUserManager.systemUserValidLogin(user));

                boolean isValidLogin = loggedInSystemUser.get() != null;

                updateValue(isValidLogin);

                return isValidLogin;
            }
        };

        return validLoginTask;
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

    public Task<SystemUser> createSystemUser(SystemUser user) {
        Task<SystemUser> createSystemUserTask = new Task<>() {
            @Override
            protected SystemUser call() throws Exception {
                SystemUser createdUser = systemUserManager.createSystemUser(user);

                if(createdUser != null){
                    allUsers.add(createdUser);
                    search(searchString);
                }

                updateValue(createdUser);

                return createdUser;
            }
        };

        return createSystemUserTask;
    }

    public ObservableList<SystemRole> getAllRoles(){
        ObservableList<SystemRole> list = FXCollections.observableList(Arrays.stream(SystemRole.values()).toList());
        return list;
    }

    public Task<Boolean> updateSystemUser(SystemUser user, SystemUser originalUser) {
        Task<Boolean> updateSystemUserTask = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                boolean successfullyUpdatedUser = systemUserManager.updateSystemUser(user) != null;

                if(successfullyUpdatedUser){
                    allUsers.remove(originalUser);
                    allUsers.add(user);

                    search(searchString);
                }

                updateValue(successfullyUpdatedUser);

                return successfullyUpdatedUser;
            }
        };

        return updateSystemUserTask;
    }
}
