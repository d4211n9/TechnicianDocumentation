package gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import util.SymbolPaths;

import java.net.URL;
import java.util.ResourceBundle;

public class ErrorDisplayController extends BaseController implements Initializable {

    @FXML
    private VBox displayErrorVBox;
    @FXML
    private HBox displayErrorHeaderHBox;
    @FXML
    private ImageView headerImageView;
    @FXML
    private Text headerText, errorMessageText, contactSupportText;

    private Throwable throwable;

    public void setContent(Throwable throwable) {
        this.throwable = throwable;
        if (throwable != null) errorMessageText.setText(throwable.getMessage());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        headerImageView.setImage(new Image(SymbolPaths.LOGO_NO_BG));
    }
}
