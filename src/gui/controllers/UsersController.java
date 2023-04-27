package gui.controllers;

import be.Enum.SystemRole;
import be.Project;
import be.SystemUser;
import com.jfoenix.controls.JFXButton;
import gui.util.MainControllerHandler;
import gui.util.NodeAccessLevel;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import util.ViewPaths;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class UsersController extends BaseController implements Initializable {

    public TableView tvUsers;
    public TableColumn<SystemUser, String>  tcCName, tcEmail;
    public TableColumn<SystemUser, SystemRole> tcAccessLevel;
    public HBox buttonArea;
    public VBox usersView;

    NodeAccessLevel buttonAccessLevel;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
            loadTableView();
            addLoadedButtons();
    }

    private void addLoadedButtons() {
        initializeButtonAccessLevels();

        try {
            //Gets the logged-in user's role
            SystemRole loggedInUserRole = getLoggedInUser();

            // Loops through the buttons and adds them to the sidebar if the user has the right access level
            for (Node button : buttonAccessLevel.getNodes()) {

                List<SystemRole> accessLevel = buttonAccessLevel.getAccessLevelsForNode(button);

                if(accessLevel.contains(loggedInUserRole)) addButton((Button) button);
            }
        } catch (Exception e) {
            displayError(e);
        }
    }

    private void addButton(Button button) {
        buttonArea.getChildren().add(0, button);
    }

    private void initializeButtonAccessLevels() {
        buttonAccessLevel = new NodeAccessLevel();

        //TODO Slet, testing
        buttonAccessLevel.addNodeAccessLevel(
                loadButton("➕📄 Add Project", ViewPaths.ADD_PROJECT_VIEW, usersView),
                Arrays.asList(SystemRole.Administrator, SystemRole.ProjectManager));

        buttonAccessLevel.addNodeAccessLevel(
                loadButton("👥 Users", ViewPaths.USERS_VIEW, usersView),
                Arrays.asList(SystemRole.Administrator));

        buttonAccessLevel.addNodeAccessLevel(
                loadButton("👥 Users", ViewPaths.USERS_VIEW, usersView),
                Arrays.asList(SystemRole.Administrator));

    }


    private void loadTableView(){
        tcCName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tcAccessLevel.setCellValueFactory(new PropertyValueFactory<>("role"));

        try {
            tvUsers.setItems(getModelsHandler().getSystemUserModel().getAllUsers());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
