package gui.controllers.projectControllers;

import be.Enum.ProjectStatus;
import be.Project;
import gui.controllers.TableViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import util.ViewPaths;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class MyProjectController extends TableViewController implements Initializable{

    @FXML
    private VBox projectsView;
    @FXML
    private TextField txtfSearch;
    @FXML
    private TableColumn<Project, String> tcProjectName, tcClient, tcStreet, tcPostalCode, tcCity;
    @FXML
    private TableColumn<Project, ProjectStatus> tcStatus;
    @FXML
    private TableColumn<Project, Date> tcCreated;

    @FXML
    private void handleBack(ActionEvent actionEvent) {
        getMainController().mainBorderPane.setCenter(getMainController().getLastView());
    }

    @FXML
    private void handleSearch(KeyEvent keyEvent) {
        try {
            getModelsHandler().getProjectModel().searchMyProjects(txtfSearch.getText());
        } catch (Exception e) {
            displayError(e);
        }
    }

    @FXML
    private void handleDoubleClick(MouseEvent mouseEvent) {
        if(mouseEvent.getButton().equals(MouseButton.PRIMARY) && isTvSelected()){
            if(mouseEvent.getClickCount() == 2){
                FXMLLoader loader = loadView(ViewPaths.PROJECT_INFO_VIEW);
                getMainController().mainBorderPane.setCenter(loader.getRoot());
                getMainController().saveLastView(projectsView);

                ProjectInfoController controller = loader.getController();
                Project selected = (Project) tableView.getSelectionModel().getSelectedItem();
                controller.setContent(selected);
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadTableView();
    }

    private void loadTableView() {
        tcStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tcClient.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        tcStreet.setCellValueFactory(new PropertyValueFactory<>("street"));
        tcPostalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        tcCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        tcProjectName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcCreated.setCellValueFactory(new PropertyValueFactory<>("created"));
        try {
            tableView.setItems(getModelsHandler().getProjectModel().getAllMyProjects(
                    getModelsHandler().getSystemUserModel().getLoggedInSystemUser().getValue().getEmail()
            ));
        } catch (Exception e) {
            displayError(e);
        }
    }
}
