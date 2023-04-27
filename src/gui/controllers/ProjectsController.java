package gui.controllers;

import be.Project;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

public class ProjectsController extends BaseController implements Initializable {
    @FXML
    private HBox buttonArea;
    @FXML
    private TableView tvProjects;
    @FXML
    private TableColumn<Project, String> tcClient, tcLocation, tcProjectName;
    @FXML
    private TableColumn<Project, Date> tcCreated;

    private ObservableList<Project> allProjects = FXCollections.observableList(new ArrayList<>()); //TODO Slet, testing

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tcClient.setCellValueFactory(new PropertyValueFactory<>("client"));
        tcLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        tcProjectName.setCellValueFactory(new PropertyValueFactory<>("projectName"));
        tcCreated.setCellValueFactory(new PropertyValueFactory<>("created"));

        //TODO Slet, testing
        Calendar date = Calendar.getInstance();
        Project p1 = new Project("EASV", "Lobby", "Esbjerg", date.getTime());
        Project p2 = new Project("EASV", "Lobby", "Sønderborg", date.getTime());
        allProjects.add(p1);
        allProjects.add(p2);
        tvProjects.setItems(allProjects);
    }
}
