package gui.controllers;

import be.SystemUser;
import com.jfoenix.controls.JFXCheckBox;
import gui.util.MainControllerHandler;
import gui.util.TaskExecutor;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import util.InputValidator;
import util.SymbolPaths;

import java.net.URL;
import java.time.LocalDateTime;
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
    @FXML
    private JFXCheckBox jfxcbRememberLogin;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        isLoginRemembered();
        showLogo();
        easyLogin();
    }

    //TODO Remove before publishing
    private void easyLogin() {
        txtfEmail.setText("steffan@gmail.com");
        pwfPassword.setText("P4$$word");
    }

    private void isLoginRemembered() {
        try {
            SystemUser rememberedUser = getModelsHandler().getSystemUserModel().isLoginRemembered();

            if (rememberedUser != null) {
                txtfEmail.setText(rememberedUser.getEmail());
                pwfPassword.setText(rememberedUser.getPassword());
                jfxcbRememberLogin.setSelected(true);
            }
        }
        catch (Exception e) {
            displayError(e);
        }
    }

    private void showLogo() {
        Image logo = new Image(SymbolPaths.LOGO);
        ivLogo.setImage(logo);
    }

    @FXML
    private void handleLogin() {
        String email = txtfEmail.getText();
        String password = pwfPassword.getText();

        if (InputValidator.isEmail(email) && InputValidator.isPassword(password)) {

            SystemUser user = new SystemUser(email, password);

            login(user);
        }
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

    private void login(SystemUser user) {
        try {
            Task<Boolean> validLoginTask = getModelsHandler().getSystemUserModel().SystemUserValidLogin(user, jfxcbRememberLogin.isSelected());

            validLoginTask
                    .valueProperty()
                    .addListener((observable, oldValue, newValue) -> {
                        if (newValue) {
                            MainController mainController = MainControllerHandler.getInstance().getController();
                            mainController.setLandingPage();
                            close();
                        } else {
                            //TODO Show that something went wrong
                            lblEmail.setText("Email* Wrong email or password, please try again");
                            txtfEmail.requestFocus();
                        }
                    });

            validLoginTask.setOnFailed(event -> displayError(validLoginTask.getException()));

            TaskExecutor.executeTask(validLoginTask);
        }
        catch (Exception e) {
            displayError(e);
        }
    }
}
