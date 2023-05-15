package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidator {
    public static boolean isEmail(String email) {
        return email.matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}") && !email.isEmpty();
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
        return password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\S+$).{8,20}$") && !password.isEmpty();
    }


    //todo make validation
    public static boolean isName(String name){
        return name.matches("[A-Z][a-zA-Z]*");
    }

    /**
     * Must include at least 8 digits, but can include more digits and characters (for +45, 0045 and so on)
     * @param phone, the user input to validate
     * @return true if the input matches a DK phone no.
     */
    public static boolean isPhone(String phone) {
        return phone.matches("^\\D*(?:\\d\\D*){8,}$");
    }

    /**
     * Must include min. 1 character for street name and min. 1 digit for street no.
     * @param street, the user input to validate
     * @return true if street input matches pattern
     */
    public static boolean isStreet(String street) {
        return street.matches("^(?=.*[a-zA-Z])(?=.*[0-9])$");
    }

    //TODO Make validation of city.
    public static boolean isCity(String city) {
        return city.matches("[A-Z][a-zA-Z]*");
    }

    /**
     * Checks for DK postal codes only, which must be 4 digits
     * @param postalCode, the user input to validate
     * @return true if the input matches a DK postal code.
     */
    public static boolean isPostalCode(int postalCode) {
        String zip = "" + postalCode;
        return zip.matches("\\d{4}");
    }

}
