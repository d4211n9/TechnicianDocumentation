package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidator {
    public static boolean isEmail(String email) {
        Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        Matcher mat = pattern.matcher(email);

        if(mat.matches() && !email.isEmpty()){
            return true;
        }
        return false;
    }

    /**
     * Checks if the password satisfies all constraints
     * (https://www.geeksforgeeks.org/how-to-validate-a-password-using-regular-expressions-in-java/)
     *
     * ^ represents starting character of the string.
     * (?=.*[0-9]) represents a digit must occur at least once.
     * (?=.*[a-z]) represents a lower case alphabet must occur at least once.
     * (?=.*[A-Z]) represents an upper case alphabet that must occur at least once.
     * (?=.*[@#$%^&-+=()] represents a special character that must occur at least once.
     * (?=\\S+$) white spaces don’t allowed in the entire string.
     * .{8, 20} represents at least 8 characters and at most 20 characters.
     * $ represents the end of the string.
     * @param password, the string to validate
     * @return true if password is validated
     */
    public static boolean isPassword(String password) {
        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\S+$).{8,20}$");
        Matcher mat = pattern.matcher(password);

        if(mat.matches() && !password.isEmpty()){
            return true;
        }
        return false;
    }

}
