package gui.controllers.projectControllers;

import be.Client;
import be.Project;
import gui.controllers.BaseController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import util.ViewPaths;

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

        //TODO Slet, tester InstallationCard in action
        FXMLLoader loader1 = loadView(ViewPaths.INSTALLATION_CARD);
        FXMLLoader loader2 = loadView(ViewPaths.INSTALLATION_CARD);
        HBox installationCard1 = loader1.getRoot();
        HBox installationCard2 = loader2.getRoot();
        fpInstallations.getChildren().add(installationCard1);
        fpInstallations.getChildren().add(installationCard2);
    }
    public void handleBack(ActionEvent actionEvent) {
    }
}
