package gui.controllers;

import be.SystemUser;
import exceptions.DALException;
import gui.models.ModelsHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    private void easyLogin() {
        txtfEmail.setText("micdra01@easv365.dk");
        pwfPassword.setText("P4$$word");
    }

    private void showLogo() {
        Image logo = new Image(SymbolPaths.LOGO);
        ivLogo.setImage(logo);
    }

    public void handleLogin(ActionEvent actionEvent) {
        String email = txtfEmail.getText();
        String password = pwfPassword.getText();
        if(InputValidator.isEmail(email) && InputValidator.isPassword(password)) {
            SystemUser user = new SystemUser(email, password);
            try {
                if(getModelsHandler().getSystemUserModel().SystemUserValidLogin(user)) {
                    openStage(ViewPaths.MAIN_VIEW, "");
                    close();
                } else {
                    //TODO Vis at noget gik galt
                    lblEmail.setText("Email* Wrong email or password, please try again");
                    txtfEmail.requestFocus();
                }
            } catch (Exception e) {
                e.printStackTrace();
                displayError(new DALException("Failed to login", e));
            }
        } else {
            //TODO Vis at noget gik galt
            lblEmail.setText("Email* Wrong email or password, please try again");
            txtfEmail.requestFocus();
        }
    }

    private void close() {
        Stage stage = (Stage) ivLogo.getScene().getWindow();
        stage.close();
    }
}
