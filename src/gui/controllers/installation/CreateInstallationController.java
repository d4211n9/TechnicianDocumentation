package gui.controllers.installation;

import be.Installation;
import be.Project;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import gui.controllers.BaseController;
import gui.controllers.installation.*;
import gui.controllers.projectControllers.ProjectInfoController;
import gui.util.TaskExecutor;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import util.InputValidator;
import util.ViewPaths;

public class CreateInstallationController extends BaseController {

    @FXML
    private JFXTextArea txtDescription;
    @FXML
    private TextField txtfName;
    @FXML
    private HBox buttonArea;
    @FXML
    private JFXButton btnConfirm;

    private Project project;
    private Installation installation;
    private int installationToEditId = -1;
    private ProjectInfoController projectInfoController;


    public void setContent(Project project, ProjectInfoController projectInfoController) {
        this.project = project;
        this.projectInfoController = projectInfoController;
    }

    public void setEditContent(Installation installation, ProjectInfoController projectInfoController) {
        this.installation = installation;
        this.projectInfoController = projectInfoController;

        buttonArea.getChildren().remove(btnConfirm);

        installationToEditId = installation.getID();
        txtfName.setText(installation.getName());
        txtDescription.setText(installation.getDescription());

        addEditButton();
    }

    private void addEditButton() {
        JFXButton button = createButton("✔ Confirm Edit");
        buttonArea.getChildren().add(0, button);

        button.setOnMouseClicked(event -> {
            if (validateInput()) {
                Installation installation = createInstallationFromFields();

                updateInstallation(installation);
            }
        });
    }

    private void updateInstallation(Installation installation) {
        try {
            Task<Installation> updateInstallationTask = getModelsHandler().getInstallationModel().updateInstallation(installation);

            updateInstallationTask.setOnFailed(failedEvent -> displayError(updateInstallationTask.getException()));

            TaskExecutor.executeTask(updateInstallationTask);

            updateInstallationTask.valueProperty().addListener((observable, oldValue, newValue) -> {
                projectInfoController.loadInstallations();
                openInstallationInfo(newValue);
            });
        } catch (Exception e) {
            displayError(e);
        }
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

            createInstallationTask.setOnFailed(event -> displayError(createInstallationTask.getException()));

            TaskExecutor.executeTask(createInstallationTask);

            createInstallationTask.valueProperty().addListener((observable, oldValue, newValue) -> {

                try {
                    getModelsHandler().getInstallationModel().setSelectedInstallation(newValue);
                } catch (Exception e) {
                    displayError(e);
                }

                openInstallationInfo(newValue);

                projectInfoController.loadInstallations();
            });
        } catch (Exception e) {
            displayError(e);
        }
    }

    private void openInstallationInfo(Installation installation) {
        FXMLLoader infoLoader = loadView(ViewPaths.INSTALLATION_INFO);
        VBox installationInfo = infoLoader.getRoot();
        InstallationInfoController infoController = (InstallationInfoController) infoLoader.getController();
        infoController.setContent(installation, projectInfoController);
        getMainController().mainBorderPane.setCenter(installationInfo);
    }

    private Installation createInstallationFromFields() {
        if(validateInput()){
            String name = txtfName.getText();
            String description = txtDescription.getText();

            if(installationToEditId != -1) {
                return new Installation(installationToEditId, installation.getProjectID(),
                        name, description, installation.getIsDone());
            }

            return new Installation(project.getID(), name, description,false);
        }
        return null;
    }

    private boolean validateInput() {
        return InputValidator.isName(txtfName.getText());
    }

    public void handleBack() {
        getMainController().mainBorderPane.setCenter(getMainController().getLastView());
    }

    public void handleCancel() {
        handleBack();
    }
}
