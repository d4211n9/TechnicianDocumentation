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

        allUsers = retrieveAllUsers().get();

        copyAllUsers = new ArrayList<>(allUsers);
        filteredUserList = FXCollections.observableList(copyAllUsers);
    }

    public Task<List<SystemUser>> retrieveAllUsers() throws Exception {
        Task<List<SystemUser>> retrieveAllUsersTask = new Task<>() {
            @Override
            protected List<SystemUser> call() throws Exception {
                List<SystemUser> allSystemUsers = systemUserManager.getAllSystemUsers();

                updateValue(allSystemUsers);

                return allSystemUsers;
            }
        };

        executeTask(retrieveAllUsersTask);

        return retrieveAllUsersTask;
    }

    public Task<Boolean> SystemUserValidLogin(SystemUser user) throws Exception {
        Task<Boolean> validLoginTask = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                loggedInSystemUser = new SimpleObjectProperty<>(systemUserManager.systemUserValidLogin(user));

                boolean isValidLogin = loggedInSystemUser.get() != null;

                updateValue(isValidLogin);

                return isValidLogin;
            }
        };

        executeTask(validLoginTask);

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

    public Task<SystemUser> createSystemUser(SystemUser user) throws Exception {
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

        executeTask(createSystemUserTask);

        return createSystemUserTask;
    }

    public ObservableList<SystemRole> getAllRoles(){
        ObservableList<SystemRole> list = FXCollections.observableList(Arrays.stream(SystemRole.values()).toList());
        return list;
    }

    public Task<Boolean> updateSystemUser(SystemUser user, SystemUser originalUser) throws Exception{
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

        executeTask(updateSystemUserTask);

        return updateSystemUserTask;
    }

    private <T> void executeTask(Task<T> task) {
        try (ExecutorService executorService = Executors.newSingleThreadExecutor()) {
            executorService.submit(task);
        }
    }
}
