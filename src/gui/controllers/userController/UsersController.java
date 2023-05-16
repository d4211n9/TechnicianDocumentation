package gui.controllers.userController;

import be.Enum.SystemRole;
import be.SystemUser;
import gui.controllers.TableViewController;
import gui.util.NodeAccessLevel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import util.ViewPaths;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;


public class UsersController extends TableViewController implements Initializable {
    @FXML
    private TableColumn<SystemUser, String> tcName, tcEmail;
    @FXML
    private TableColumn<SystemUser, SystemRole> tcRole;

    @FXML
    private VBox usersView;
    @FXML
    private TextField txtfSearch;
        @Override
    public void initialize(URL location, ResourceBundle resources) {
            loadTableView();
            initializeButtonAccessLevels();
            usersView.getChildren().add(addButtons());
            tvListener();

            userBackgroundUpdate();
        }

    private void userBackgroundUpdate() {
        try {
            List<Runnable> backgroundUpdateList = new ArrayList<>();
            backgroundUpdateList.add(getModelsHandler().getSystemUserModel());

            backgroundUpdate(backgroundUpdateList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void loadTableView() {
        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tcRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        try {
            tableView.setItems(getModelsHandler().getSystemUserModel().getAllUsers());
        } catch (Exception e) {
            displayError(e);
        }
    }

    private void initializeButtonAccessLevels() {
        buttonAccessLevel = new NodeAccessLevel();

        addDeleteBtn();
        addEditBtn();

        buttonAccessLevel.addNodeAccessLevel(
                loadButton("➕👤 Add User", ViewPaths.CREATE_USER_VIEW, usersView),
                Arrays.asList(SystemRole.Administrator, SystemRole.ProjectManager));
    }

    private void addEditBtn() {
        editButton = createButton("✏ Edit User");
        buttonAccessLevel.addNodeAccessLevel(editButton,
                Arrays.asList(SystemRole.Administrator, SystemRole.ProjectManager));
        editButton.setDisable(true);

        editButton.setOnMouseClicked(event -> {
            FXMLLoader loader = loadView(ViewPaths.CREATE_USER_VIEW);
            CreateUserController controller = loader.getController();
            controller.setEditContent((SystemUser) tableView.getSelectionModel().getSelectedItem());
            loadInMainView(loader.getRoot(), usersView);
        });
    }

    private void addDeleteBtn() {
        deleteButton = createButton("🗑 Delete User");
        buttonAccessLevel.addNodeAccessLevel(deleteButton,
                Arrays.asList(SystemRole.Administrator, SystemRole.ProjectManager));
        deleteButton.setDisable(true);

        deleteButton.setOnMouseClicked(event -> {
            SystemUser user = (SystemUser) tableView.getSelectionModel().getSelectedItem();
            if(showQuestionDialog(user.toString(), true)){
                try {
                    getModelsHandler().getSystemUserModel().deleteSystemUser(user);
                    handleSearch();
                } catch (Exception e) {
                    displayError(e);
                }
            }
        });
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
