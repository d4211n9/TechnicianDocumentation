import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.ViewPaths;

public class Main extends Application {
    public static void main(String[] args) throws Exception {
        Application.launch();

        //Photo photo = new Photo(16,1, new Image(SymbolPaths.LOGO), "ello");
        //PhotoDAO photoDAP = new PhotoDAO();
        //photoDAP.updatePhoto(photo);
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
