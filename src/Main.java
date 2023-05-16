import be.Photo;
import dal.dao.PhotoDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.SymbolPaths;
import util.ViewPaths;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

public class Main extends Application {

    public static void main(String[] args) throws Exception {
        Application.launch();

        //Photo photo = new Photo(1,1, new Image(SymbolPaths.LOGO), "ello");
        //PhotoDAO photoDAP = new PhotoDAO();
        //photoDAP.uploadPhoto(photo);
        //photoDAP.deletePhoto(photo);
        //photoDAP.getPhotoFromInstallation(photo.getInstallationID());
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(ViewPaths.LOGIN_VIEW));
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
    }
}
