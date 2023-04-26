import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.StylePaths;
import util.ViewPaths;

public class Main extends Application {
    public static void main(String[] args) {
        Application.launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(ViewPaths.LOGIN_VIEW));
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root));
        primaryStage.getScene().getStylesheets().add(StylePaths.LOGIN);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
    }
}
