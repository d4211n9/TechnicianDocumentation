package gui.controllers.installation;

import be.Installation;
import be.Project;
import com.jfoenix.controls.JFXTextArea;
import gui.controllers.BaseController;
import gui.controllers.installation.InstallationInfoController;
import gui.util.TaskExecutor;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import util.ViewPaths;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateInstallationController extends BaseController implements Initializable {

    @FXML
    private HBox hbUserBtnArea;
    @FXML
    private JFXTextArea txtDescription;
    @FXML
    private TextField txtfName;

    private Project project;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setContent(Project project) {
        this.project = project;
    }
    public void handleConfirm() {
        Installation installation = createInstallationFromFields();
        if(installation != null){
            createInstallation(installation);
        }
    }

    private void createInstallation(Installation installation) {
        try {
            Task<Installation> createInstallationTask = getModelsHandler()
                    .getInstallationModel()
                    .createInstallation(installation);

            createInstallationTask.valueProperty().addListener((observable, oldValue, newValue) -> {
                openInstallationInfo(installation);
            });

            createInstallationTask.setOnFailed(event -> displayError(createInstallationTask.getException()));

            TaskExecutor.executeTask(createInstallationTask);
        } catch (Exception e) {
            displayError(e);
        }
    }

    private void openInstallationInfo(Installation installation) {
        FXMLLoader infoLoader = loadView(ViewPaths.INSTALLATION_INFO);
        VBox installationInfo = infoLoader.getRoot();
        InstallationInfoController infoController = infoLoader.getController();
        infoController.setContent(installation);
        getMainController().mainBorderPane.setCenter(installationInfo);
    }

    private Installation createInstallationFromFields() {
        if(validateInput()){
            String name = txtfName.getText();
            String description = txtDescription.getText();

            return new Installation(project.getID(), name, description, null,false);
        }
        return null;
    }

    private boolean validateInput() {
        //TODO Validér input
        return true;
    }

    public void handleBack() {
        getMainController().mainBorderPane.setCenter(getMainController().getLastView());
    }

    public void handleCancel() {
        handleBack();
    }
}
