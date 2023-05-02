package gui.controllers.userController;

import be.Enum.SystemRole;
import be.SystemUser;
import com.jfoenix.controls.JFXButton;
import gui.controllers.BaseController;
import gui.util.NodeAccessLevel;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
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

    private JFXButton editButton, deleteButton;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
            loadTableView();
            initializeButtonAccessLevels();
            addLoadedButtons();
            tvListener();
    }


    private void tvListener() {
        tvUsers.setOnMouseClicked(event -> {
            if(isTvSelected()){
                deleteButton.setDisable(false);
                editButton.setDisable(false);
            }else {
                deleteButton.setDisable(true);
                editButton.setDisable(true);
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

        addDeleteBtn();
        addEditBtn();

        buttonAccessLevel.addNodeAccessLevel(
                loadButton("➕👤 Add User", "/gui/views/userViews/CreateUserView.fxml", usersView),
                Arrays.asList(SystemRole.Administrator, SystemRole.ProjectManager));
    }

    private void addEditBtn() {
        editButton = createButton("Edit User");
        buttonAccessLevel.addNodeAccessLevel(editButton,
                Arrays.asList(SystemRole.Administrator, SystemRole.ProjectManager));
        editButton.setDisable(true);

        editButton.setOnMouseClicked(event -> {
            FXMLLoader loader = loadView("/gui/views/userViews/CreateUserView.fxml");
            CreateUserController controller = loader.getController();
            controller.setEditContent(tvUsers.getSelectionModel().getSelectedItem());
            loadInMainView(loader.getRoot(), usersView);
        });
    }

    private void addDeleteBtn() {
        deleteButton = createButton("Delete User");
        buttonAccessLevel.addNodeAccessLevel(deleteButton,
                Arrays.asList(SystemRole.Administrator, SystemRole.ProjectManager));
        deleteButton.setDisable(true);

        deleteButton.setOnMouseClicked(event -> {
            Object user = tvUsers.getSelectionModel().getSelectedItem();
            if(showQuestionDialog(user.toString(), true)){
                System.out.println("delete " + user.toString());
            }
        });
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

    public void handleSearch() {
        try {
            getModelsHandler().getSystemUserModel().search(txtfSearch.getText());
        } catch (Exception e) {
            displayError(e);
        }
    }
}
