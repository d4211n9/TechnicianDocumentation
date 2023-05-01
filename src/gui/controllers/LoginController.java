package gui.controllers;

import be.SystemUser;
import exceptions.DALException;
import gui.models.ModelsHandler;
import gui.util.MainControllerHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import util.InputValidator;
import util.StylePaths;
import util.SymbolPaths;
import util.ViewPaths;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController extends BaseController implements Initializable {
    @FXML
    private Label lblEmail;
    @FXML
    private ImageView ivLogo;
    @FXML
    private TextField txtfEmail;
    @FXML
    private PasswordField pwfPassword;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showLogo();
        easyLogin();
    }

    //TODO Remove before publishing
    private void easyLogin() {
        txtfEmail.setText("steffan@gmail.com");
        pwfPassword.setText("P4$$word");
    }

    private void showLogo() {
        Image logo = new Image(SymbolPaths.LOGO);
        ivLogo.setImage(logo);
    }

    @FXML
    private void handleLogin() {
        String email = txtfEmail.getText();
        String password = pwfPassword.getText();
        if(InputValidator.isEmail(email) && InputValidator.isPassword(password)) {
            SystemUser user = new SystemUser(email, password);
            try {
                if(getModelsHandler().getSystemUserModel().SystemUserValidLogin(user)) {
                    MainControllerHandler.getInstance().getController();
                    close();

                    return;
                }
            } catch (Exception e) {
                displayError(e);
            }
        }

        //TODO Show that something went wrong
        lblEmail.setText("Email* Wrong email or password, please try again");
        txtfEmail.requestFocus();
    }

    @FXML
    private void handleEnter(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            handleLogin();
        }
    }

    private void close() {
        Stage stage = (Stage) ivLogo.getScene().getWindow();
        stage.close();
    }
}
