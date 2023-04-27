package gui.models;

import be.SystemUser;
import bll.interfaces.ISystemUserManager;
import bll.managers.SystemUserManager;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableValue;

public class SystemUserModel {

    private ISystemUserManager systemUserManager;
    private static ObservableObjectValue<SystemUser> loggedInSystemUser;

    public SystemUserModel() throws Exception {
        loggedInSystemUser = new SimpleObjectProperty<>(null);
        systemUserManager = new SystemUserManager();
    }

    public boolean SystemUserValidLogin(SystemUser user) throws Exception {
        loggedInSystemUser = new SimpleObjectProperty<>(systemUserManager.systemUserValidLogin(user));
        return loggedInSystemUser.get() != null;
    }

    public ObservableValue<SystemUser> getLoggedInSystemUser() {
        return loggedInSystemUser;
    }
}
