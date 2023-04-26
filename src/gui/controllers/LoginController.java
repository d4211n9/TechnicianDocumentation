package gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.StylePaths;
import util.SymbolPaths;
import util.ViewPaths;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private ImageView ivLogo;
    @FXML
    private Label lblEmail, lblPassword;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showLogo();
    }

    private void showLogo() {
        Image logo = new Image(SymbolPaths.LOGO);
        ivLogo.setImage(logo);
    }

    public void handleLogin(ActionEvent actionEvent) {
        //TODO Tjek for korrekt login før MainView loades
        FXMLLoader loader = new FXMLLoader(getClass().getResource(ViewPaths.MAIN_VIEW));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            //TODO ErrorDisplayer
        }
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        //stage.getScene().getStylesheets().add(StylePaths.LOGIN);
        stage.show();
        close();
    }

    private void close() {
        Stage stage = (Stage) ivLogo.getScene().getWindow();
        stage.close();
    }
}
