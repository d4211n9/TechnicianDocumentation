import be.Drawing;
import dal.dao.DrawingDAO;
import gui.models.DrawingModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.SymbolPaths;
import util.ViewPaths;

public class Main extends Application {
    public static void main(String[] args) throws Exception {Application.launch();

        //byte[] image = SymbolPaths.LOGO.getBytes();
        //Drawing drawing = new Drawing(5, image);
        //DrawingDAO drawingDAO = new DrawingDAO();
        //DrawingModel drawingModel = new DrawingModel();
        //drawingModel.createDrawing(drawing);

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
