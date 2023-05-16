import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.ViewPaths;

public class Main extends Application {
    public static void main(String[] args) {
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
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
    }
}
