package gui.controllers.projectControllers;

import be.Project;
import be.SystemUser;
import gui.controllers.TableViewController;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import util.ViewPaths;

import java.net.URL;
import java.util.ResourceBundle;


public class RemoveUserFromProject extends TableViewController implements Initializable {

    @FXML
    private TableColumn tcName, tcEmail, tcRole;
    @FXML
    private HBox buttonArea;
    @FXML
    private VBox usersView;
    private Project project;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addDoneButton();
    }

    public void setContent(Project project){
        this.project = project;
        loadTableView();
        tableViewListener();
    }

    private void tableViewListener() {
        tableView.setOnMouseClicked(event -> {
            try {
                SystemUser s = ((SystemUser)tableView.getSelectionModel().getSelectedItem());
                Task t = getModelsHandler().getProjectModel().deleteSystemUserAssignedToProject(project.getID(), s.getEmail());
                tableView.setDisable(true);
                t.run();
                t.setOnSucceeded(event1 -> {
                    loadTableView();
                    tableView.setDisable(false);
                });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
    private void loadTableView() {
        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tcRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        try {
            tableView.setItems(getModelsHandler().getProjectModel().getAllUsersOnProject(project.getID()));
        } catch (Exception e) {
            displayError(e);
        }
    }

    public void handleBack(ActionEvent actionEvent) {
        getMainController().mainBorderPane.setCenter(getMainController().getLastView());
    }

    public void handleAddUsers(MouseEvent mouseEvent) {
        FXMLLoader loader = loadView(ViewPaths.ADD_TO_PROJECT);
        AddUserToProjectController controller = loader.getController();
        loadInMainView(loader.getRoot(), usersView);
        controller.setProject(project);
    }

    private void addDoneButton() {
        Button b = createButton("✅ Done");
        b.setOnMousePressed(event -> {
            handleBack();
        });
        buttonArea.getChildren().add(0, b);
    }

    public void handleBack() {
        getMainController().mainBorderPane.setCenter(getMainController().getLastView());
    }
}
