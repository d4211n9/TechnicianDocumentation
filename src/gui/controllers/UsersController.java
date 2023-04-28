package gui.controllers;

import be.Enum.SystemRole;
import be.SystemUser;
import gui.util.MainControllerHandler;
import gui.util.NodeAccessLevel;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import util.ViewPaths;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class UsersController extends BaseController implements Initializable {
    public TableView tvUsers;
    public TableColumn<SystemUser, String> tcName, tcEmail;
    public TableColumn<SystemUser, SystemRole> tcRole;
    public HBox buttonArea;
    public VBox usersView;
    public TextField txtfSearch;
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

                if(accessLevel.contains(loggedInUserRole))
                    addButton((Button) button);
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
                loadButton("➕👥 Add User", ViewPaths.ADD_PROJECT_VIEW, usersView),
                Arrays.asList(SystemRole.Administrator, SystemRole.ProjectManager));

        buttonAccessLevel.addNodeAccessLevel(
                loadButton("➖👥 Delete User", ViewPaths.ADD_PROJECT_VIEW, usersView),
                Arrays.asList(SystemRole.Administrator, SystemRole.ProjectManager));
    }

    private void loadTableView() {
        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tcRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        try {
            tvUsers.setItems(getModelsHandler().getSystemUserModel().getAllUsers());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

<<<<<<< Updated upstream
    public void handleBack() {
        getMainController().mainBorderPane.setCenter(getMainController().getLastView());
        getMainController().saveLastView(usersView);
=======
    public void handleSearch(KeyEvent keyEvent) {
        try {
            getModelsHandler().getSystemUserModel().search(txtfSearch.getText());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
>>>>>>> Stashed changes
    }
}
