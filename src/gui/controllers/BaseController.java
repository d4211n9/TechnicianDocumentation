package gui.controllers;

import exceptions.GUIException;
import gui.models.ModelsHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import util.ViewPaths;

import java.io.IOException;

public class BaseController {
    public ModelsHandler getModelsHandler() throws Exception {
        return ModelsHandler.getInstance();
    }

    /**
     * Opens a new window
     *
     * @param fxmlPath,   the path of the FXML to load
     * @param sceneTitle, the title for the scene
     * @return FXMLLoader, in case a handle to the new controller is needed
     */
    public FXMLLoader openStage(String fxmlPath, String sceneTitle) {
        //Load the new stage & view
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = null;

        try {
            root = loader.load();
        } catch (IOException e) {
            displayError(new GUIException("Failed to open window", e));
        }

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle(sceneTitle);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();

        return loader;
    }

    /**
     * Loads a new view to show in center
     *
     * @param fxmlPath, the path of the FXML to load
     * @return FXMLLoader, in case a handle to the new controller is needed
     */
    public FXMLLoader loadView(String fxmlPath) {
        //Load the new stage & view
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = null;

        try {
            root = loader.load();
        } catch (IOException e) {
            displayError(new GUIException("Failed to load view", e));
        }

        return loader;
    }

    public void displayError(Throwable throwable) {
        FXMLLoader loader = openStage(ViewPaths.ERROR_DISPLAYER, "Error");
        ErrorDisplayController controller = loader.getController();
        controller.setContent(throwable);
        throwable.printStackTrace(); //TODO Slet når vi kan logge fejl?
    }
}
