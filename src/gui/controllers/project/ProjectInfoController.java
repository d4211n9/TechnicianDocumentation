package gui.controllers.project;

import be.Client;
import be.Project;
import gui.controllers.BaseController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ProjectInfoController extends BaseController {
    @FXML
    private VBox projectsView;
    @FXML
    private FlowPane fpStaff, fpInstallations;
    @FXML
    private HBox buttonArea;
    @FXML
    private Label lblProjectTitle, lblClientName, lblClientLocation, lblClientType, lblClientEmail, lblClientPhone;

    private Client client;
    private Project project;

    public void setContent(Project project) {
        this.project = project;
        client = project.getClient();

        lblProjectTitle.setText(project.getName());
        lblClientName.setText(client.getName());
        lblClientLocation.setText(client.getLocation()); //TODO Obs.: Client og Project kan have forskellige locations, bør vi vise clients her?
        lblClientType.setText(client.getType());
        lblClientEmail.setText(client.getEmail());
        lblClientPhone.setText(client.getPhone());
    }
    public void handleBack(ActionEvent actionEvent) {
    }
}
