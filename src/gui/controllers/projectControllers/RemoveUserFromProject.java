package gui.controllers.projectControllers;

import be.Project;
import be.SystemUser;
import gui.controllers.TableViewController;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import util.ViewPaths;


public class RemoveUserFromProject extends TableViewController {

    public TableColumn tcName;
    public TableColumn tcEmail;
    public TableColumn tcRole;
    public VBox usersView;
    private Project project;

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
}
