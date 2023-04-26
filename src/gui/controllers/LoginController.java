package gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import util.StylePaths;
import util.SymbolPaths;
import util.ViewPaths;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController extends BaseController implements Initializable {
    @FXML
    private ImageView ivLogo;
    @FXML
    private TextField txtfEmail;
    @FXML
    private PasswordField pwfPassword;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showLogo();
    }

    private void showLogo() {
        Image logo = new Image(SymbolPaths.LOGO);
        ivLogo.setImage(logo);
    }

    public void handleLogin(ActionEvent actionEvent) {
        String email = txtfEmail.getText();
        String password = pwfPassword.getText();
        //TODO Tjek for korrekt login før MainView loades
        openStage(ViewPaths.MAIN_VIEW, "");
        close();
    }

    private void close() {
        Stage stage = (Stage) ivLogo.getScene().getWindow();
        stage.close();
    }
}
