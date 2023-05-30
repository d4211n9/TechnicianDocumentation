package util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class InputValidatorTest {

    @DisplayName("isEmail Test")
    @Test
    void isEmail() {
        String emailPass = "william@gmail.com"; //will pass as it includes @
        String emailFail = "williamgmail.com"; //will fail as it does not include @

        Assertions.assertTrue(InputValidator.isEmail(emailPass));
        Assertions.assertFalse(InputValidator.isEmail(emailFail));
    }

    @DisplayName("isPassword Test")
    @Test
    void isPassword() {
        //will pass as it includes a digit, lower case and upper case character,
        // special character, at least 8 characters and do not include white space
        String passwordPass = "Ørknestov12=hiQ";
        String passwordFail = "123 4"; //will fail becuase it does not include lower and upper case characters and has white space

        Assertions.assertTrue(InputValidator.isPassword(passwordPass));
        Assertions.assertFalse(InputValidator.isPassword(passwordFail));
    }

    @DisplayName("isName Test")
    @Test
    void isName() {
        String namePass = "William"; //will pass as it includes min. 1 character
        String nameFail = ""; //will fail as it is blanc

        Assertions.assertTrue(InputValidator.isName(namePass));
        Assertions.assertFalse(InputValidator.isName(nameFail));
    }

    @DisplayName("isPhone Test")
    @Test
    void isPhone() {
        String phonePass = "+00 12345678"; // will pass because it includes at least 8 digits
        String phoneFail = ""; //will fail because it is blanc

        Assertions.assertTrue(InputValidator.isPhone(phonePass));
        Assertions.assertFalse(InputValidator.isPhone(phoneFail));
    }

    @DisplayName("isStreet Test")
    @Test
    void isStreet() {
        String streetPass = "Sewi3"; //will pass as it includes min. 1 character and min. 1 digit
        String streetFalse = "";

        Assertions.assertTrue(InputValidator.isStreet(streetPass));
        Assertions.assertFalse(InputValidator.isStreet(streetFalse));
    }

    @DisplayName("isCity Test")
    @Test
    void isCity() {
        String cityPass = "NorreNebel"; //will pass as it includes min. 1 character
        String cityFail = ""; //will fail as it is blanc

        Assertions.assertTrue(InputValidator.isCity(cityPass));
        Assertions.assertFalse(InputValidator.isCity(cityFail));
    }

    @DisplayName("isPostalCode Test")
    @Test
    void isPostalCode() {
        int postalCodePass = 6700;
        int postalCodeFail = 1;

        Assertions.assertTrue(InputValidator.isPostalCode(postalCodePass));
        Assertions.assertFalse(InputValidator.isPostalCode(postalCodeFail));
    }
}