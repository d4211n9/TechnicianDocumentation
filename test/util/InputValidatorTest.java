package util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class InputValidatorTest {

    @Test
    void isEmail() {
        String email = "stef@gmail.com";

        Assertions.assertTrue(email.matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}"));
    }

    @Test
    void isPassword() {
        String password = "Ørknestov12=hiQ";

        Assertions.assertTrue(password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\S+$).{8,20}$"));
    }

    @Test
    void isName() {
        String name = "William";

        Assertions.assertTrue(name.matches("[A-ZÆØÅ -.][æøåa-zÆØÅA-Z -.]*"));
    }

    @Test
    void isPhone() {
        String phone = "+0012345678";

        Assertions.assertTrue(phone.matches("^\\D*(?:\\d\\D*){8,}$"));
    }

    @Test
    void isStreet() {
        String street = "Sewi 3";

        Assertions.assertTrue(street.matches("^(?=.*[a-zA-Z])(?=.*[0-9])$"));

    }

    @Test
    void isCity() {
        String city = "NorreNebel";

        Assertions.assertTrue(city.matches("[A-Z][a-zA-Z]*"));

        //Kan ikke indeholde space? Nørre Nebel
        // Kan ikke indeholde Ø
    }

    @Test
    void isPostalCode() {
        int postalCode = 6700;
        String zip = "" + postalCode;

        Assertions.assertTrue(zip.matches("\\d{4}"));

    }
}