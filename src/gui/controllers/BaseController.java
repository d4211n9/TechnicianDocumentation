package gui.controllers;

import be.Enum.SystemRole;
import com.jfoenix.controls.JFXButton;
import exceptions.GUIException;
import gui.models.ModelsHandler;
import gui.util.MainControllerHandler;
import gui.util.NodeAccessLevel;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import util.ViewPaths;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BaseController {


    public NodeAccessLevel buttonAccessLevel;

    public static ScheduledExecutorService executorService;


    public ModelsHandler getModelsHandler() throws Exception {
        return ModelsHandler.getInstance();
    }

    public MainController getMainController() {
        return MainControllerHandler.getInstance().getController();
    }

    public static void backgroundUpdate(List<Runnable> runnable) throws Exception {
        if (executorService != null) {
            executorService.shutdown();
        }

        executorService = Executors.newScheduledThreadPool(runnable.size(), r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        });
        for (Runnable run: runnable) {
            try {
                executorService.scheduleWithFixedDelay(run, 0, 3, TimeUnit.SECONDS);
            } catch (Exception e) {
                GUIException guiException = new GUIException("Failed to background update", e);
                guiException.printStackTrace();
                throw guiException;
            }
        }
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
        button.setMinWidth(150);
        button.setPrefWidth(150);
        button.setPrefHeight(60);
        return button;
    }

    public ArrayList<Button> addLoadedButtons(NodeAccessLevel buttonAccessLevel) {
        ArrayList<Button> buttons = new ArrayList<>();
        try {
            //Gets the logged-in user's role
            SystemRole loggedInUserRole = getLoggedInUser();

            // Loops through the buttons and adds them to the contentArea if the user has the right access level
            for (Node button : buttonAccessLevel.getNodes()) {

                List<SystemRole> accessLevel = buttonAccessLevel.getAccessLevelsForNode(button);

                if(accessLevel.contains(loggedInUserRole))
                    buttons.add((Button) button);

            }
        } catch (Exception e) {
            displayError(e);
        }
        return buttons;
    }

    public HBox addButtons(){
        ArrayList<Button> buttons = addLoadedButtons(buttonAccessLevel);

        HBox buttonAreaHBox = new HBox();
        buttonAreaHBox.setAlignment(Pos.CENTER);

        for (Button button: buttons){
            buttonAreaHBox.getChildren().add(0, button);
        }
        return buttonAreaHBox;
    }
}
