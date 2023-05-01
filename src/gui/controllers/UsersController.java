package gui.controllers;

import be.Enum.SystemRole;
import be.SystemUser;
import com.jfoenix.controls.JFXButton;
import gui.util.NodeAccessLevel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.glyphfont.FontAwesome;

import java.io.IOException;
import java.net.URL;
import java.net.http.WebSocket;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


public class UsersController extends BaseController implements Initializable {
    public TableView<SystemUser> tvUsers;
    public TableColumn<SystemUser, String> tcName, tcEmail;
    public TableColumn<SystemUser, SystemRole> tcRole;
    public HBox buttonArea;
    public VBox usersView;
    public TextField txtfSearch;
    public JFXButton btnDelete;
    NodeAccessLevel buttonAccessLevel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
            loadTableView();
            addLoadedButtons();
           tvListener();
    }

    private void tvListener() {
        tvUsers.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(isTvSelected()){
                    btnDelete.setDisable(false);
                }else {
                    btnDelete.setDisable(true);
                }
            }
        });
    }

    private boolean isTvSelected() {
        return tvUsers.getSelectionModel().getSelectedItem() != null;
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
                loadButton("➕👤 Add User", "/gui/views/CreateUserView.fxml", usersView),
                Arrays.asList(SystemRole.Administrator, SystemRole.ProjectManager));
    }

    private void loadTableView() {
        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tcRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        try {
            tvUsers.setItems(getModelsHandler().getSystemUserModel().getAllUsers());
        } catch (Exception e) {
            displayError(e);
        }
    }

    public void handleBack() {
        getMainController().mainBorderPane.setCenter(getMainController().getLastView());
        getMainController().saveLastView(usersView);

    }
    public void handleSearch(KeyEvent keyEvent) {
        try {
            getModelsHandler().getSystemUserModel().search(txtfSearch.getText());
        } catch (Exception e) {
            displayError(e);
        }
    }

    public void handleDelete(ActionEvent actionEvent) throws IOException {
       Object user = tvUsers.getSelectionModel().getSelectedItem();
        if(showQuestionDialog(user.toString(), true)){
            System.out.println("delete " + user.toString());
        }
    }
}
