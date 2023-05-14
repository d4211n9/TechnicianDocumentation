package gui.controllers.projectControllers;

import be.Project;
import be.SystemUser;
import gui.controllers.TableViewController;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class AddUserToProjectController extends TableViewController implements Initializable {
    public TableColumn tcName;
    public TableColumn tcEmail;
    public TableColumn tcRole;

    public Project project;

    public void setProject(Project project){
        this.project = project;

        loadTableView();
        //initializeButtonAccessLevels();
        //usersView.getChildren().add(addButtons());

        tableView.setOnMouseClicked(event -> {
            try {
                SystemUser s = ((SystemUser)tableView.getSelectionModel().getSelectedItem());
                Task t = getModelsHandler().getProjectModel().assignSystemUserToProject(project.getID(), s.getEmail());
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

    public void handleDoubleClick(MouseEvent mouseEvent) {
    }

    public void handleBack(ActionEvent actionEvent) {
        getMainController().mainBorderPane.setCenter(getMainController().getLastView());
    }

    public void handleSearch(KeyEvent keyEvent) {
    }

    public void handleAddUser(MouseEvent mouseEvent) {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }


    private void loadTableView() {
        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tcRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        try {
            tableView.setItems(getModelsHandler().getProjectModel().getAllUsersNotAssignedProject(project.getID()));
        } catch (Exception e) {
            displayError(e);
        }
    }
}
