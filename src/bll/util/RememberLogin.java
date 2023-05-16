package bll.util;

import be.SystemUser;

import java.util.prefs.Preferences;

public class RememberLogin {
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";

    public static void rememberLogin(String email, String password) {
        Preferences preferences = Preferences.userRoot();

        preferences.put(EMAIL, email);
        preferences.put(PASSWORD, password);
    }

    public static SystemUser getRememberedLogin() {
        Preferences preferences = Preferences.userRoot();

        String email = preferences.get(EMAIL, "");
        String password = preferences.get(PASSWORD, "");

        if (email.isBlank() || password.isBlank()) return null;

        return new SystemUser(email, password);
    }

    public static void deleteRememberedLogin() {
        Preferences preferences = Preferences.userRoot();

        preferences.remove(EMAIL);
        preferences.remove(PASSWORD);
    }
}
