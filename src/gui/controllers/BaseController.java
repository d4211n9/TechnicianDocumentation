package gui.controllers;

import be.Enum.SystemRole;
import com.jfoenix.controls.JFXButton;
import exceptions.GUIException;
import gui.models.ModelsHandler;
import gui.util.MainControllerHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import util.ViewPaths;

import java.io.IOException;
import java.util.Optional;

public class BaseController {
    public ModelsHandler getModelsHandler() throws Exception {
        return ModelsHandler.getInstance();
    }

    public MainController getMainController() {
        return MainControllerHandler.getInstance().getController();
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
        //stage.initStyle(StageStyle.UNDECORATED); //TODO Fjern outcommenting når resizing spiller?
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


    public SystemRole getLoggedInUser(){
        //Gets the logged-in user's role
        try {
            SystemRole loggedInUserRole = getModelsHandler()
                    .getSystemUserModel()
                    .getLoggedInSystemUser()
                    .getValue()
                    .getRole();
            return loggedInUserRole;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public Button loadButton(String text, String fxmlPath, Node pageNode) {
        JFXButton button = new JFXButton(text);
        button.setFont(Font.font(16));
        button.setPrefWidth(150);
        button.setPrefHeight(60);

        MainController mainController = MainControllerHandler.getInstance().getController();
        button.setOnAction(e -> {
            mainController.saveLastView(pageNode);
            mainController.mainBorderPane.setCenter(loadView(fxmlPath).getRoot());
        });

        return button;
    }

    public void loadInMainView(Node node, Node pageNode){
        MainController mainController = MainControllerHandler.getInstance().getController();
        mainController.saveLastView(pageNode);
        mainController.mainBorderPane.setCenter(node);
    }
    public boolean showQuestionDialog(String message, boolean defaultYes) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
        alert.setContentText(message);
        alert.setHeaderText("Are you sure you want to delete?");
        Button noButton = (Button) alert.getDialogPane().lookupButton(ButtonType.NO);
        Button yesButton = (Button) alert.getDialogPane().lookupButton(ButtonType.YES);
        noButton.setDefaultButton(!defaultYes);
        yesButton.setDefaultButton(defaultYes);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.YES;
    }
    public JFXButton createButton(String text){
        JFXButton button = new JFXButton(text);
        button.setFont(Font.font(16));
        button.setPrefWidth(150);
        button.setPrefHeight(60);
        return button;
    }
}
