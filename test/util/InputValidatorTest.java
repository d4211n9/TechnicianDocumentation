package util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class InputValidatorTest {

    @Test
    void isEmail() {
        String emailPass = "william@gmail.com"; //will pass as it includes @
        String emailFail = "williamgmail.com"; //will fail as it does not include @

        Assertions.assertTrue(InputValidator.isEmail(emailPass));
        Assertions.assertFalse(InputValidator.isEmail(emailFail));
    }

    @Test
    void isPassword() {
        //will pass as it includes a digit, lower case and upper case character,
        // special character, at least 8 characters and do not include white space
        String passwordPass = "Ørknestov12=hiQ";
        String passwordFail = "123 4"; //will fail becuase it does not include lower and upper case characters and has white space

        Assertions.assertTrue(InputValidator.isPassword(passwordPass));
        Assertions.assertFalse(InputValidator.isPassword(passwordFail));
    }

    @Test
    void isName() {
        String namePass = "William"; //will pass as it includes min. 1 character
        String nameFail = "W1ll14m"; //will fail as it includes digits

        Assertions.assertTrue(InputValidator.isName(namePass));
        Assertions.assertFalse(InputValidator.isName(nameFail));
    }

    @Test
    void isPhone() {
        String phonePass = "+00 12345678"; // will pass because it includes at least 8 digits
        String phoneFail = "1234567"; //will fail because it includes too few digits

        Assertions.assertTrue(InputValidator.isPhone(phonePass));
        Assertions.assertFalse(InputValidator.isPhone(phoneFail));
    }

    @Test
    void isStreet() {
        String streetPass = "Sewi3"; //will pass as it includes min. 1 character and min. 1 digit
        String streetFalse = "3";

        //Assertions.assertTrue(InputValidator.isStreet(streetPass));
        Assertions.assertFalse(InputValidator.isStreet(streetFalse));
    }

    @Test
    void isCity() {
        String cityPass = "NorreNebel"; //will pass as it includes min. 1 character
        String cityFail = "N0rre Nebel"; //will fail as it includes digit and space

        Assertions.assertTrue(InputValidator.isCity(cityPass));
        Assertions.assertFalse(InputValidator.isCity(cityFail));

        //Kan ikke indeholde space? Nørre Nebel
        // Kan ikke indeholde Ø
    }

    @Test
    void isPostalCode() {
        int postalCodePass = 6700;
        //String zipPass = "" + postalCodePass;


        Assertions.assertTrue(InputValidator.isPostalCode(postalCodePass));

    }
}