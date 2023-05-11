package gui.controllers.userController;

import be.Enum.SystemRole;
import be.SystemUser;
import com.jfoenix.controls.JFXButton;
import gui.controllers.BaseController;
import gui.util.TaskExecutor;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import util.InputValidator;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateUserController extends BaseController implements Initializable {


    @FXML
    private VBox projectsView, loginInformation;
    @FXML
    private HBox buttonArea;
    @FXML
    private Label lblCreateUser, lblEmail, lblPassword, lblConfirmPassword;
    @FXML
    private JFXButton btnConfirm;
    @FXML
    private ComboBox<SystemRole> cbRoles;
    @FXML
    private TextField txtfName, txtfEmail;
    @FXML
    private PasswordField pwfPassword, pwfConfirmPassword;

    private SystemUser selectedUser;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            cbRoles.setItems(getModelsHandler().getSystemUserModel().getAllRoles());
        } catch (Exception e) {
            displayError(e);
        }
    }

    public void handleBack() {
        getMainController().mainBorderPane.setCenter(getMainController().getLastView());
    }
    public void handleCancel() {
        handleBack();
    }

    public void handleConfirm(ActionEvent actionEvent) {
        SystemUser user = createSystemUserFromFields();
        if(user != null) {
            try {
                Task<SystemUser> createSystemUserTask = getModelsHandler()
                        .getSystemUserModel()
                        .createSystemUser(createSystemUserFromFields());

                createSystemUserTask.setOnFailed(event -> displayError(createSystemUserTask.getException()));

                TaskExecutor.executeTask(createSystemUserTask);

                handleBack();
            }
            catch (Exception e) {
                displayError(e);
            }
        }
    }

    private SystemUser createSystemUserFromFields() {
        if(validateInput()){
            String name = txtfName.getText();
            SystemRole role = cbRoles.getSelectionModel().getSelectedItem();
            String email = txtfEmail.getText();
            String password = pwfPassword.getText();

            return new SystemUser(email, password, role, name);
        }
        return null;
    }

    private boolean validateInput() {
        return InputValidator.isEmail(txtfEmail.getText()) &&
                InputValidator.isName(txtfName.getText()) &&
                InputValidator.isPassword(pwfPassword.getText()) &&
                isPasswordSame();
    }

    private boolean isPasswordSame(){
        return pwfPassword.getText().equals(pwfConfirmPassword.getText());
    }

    public void setEditContent(SystemUser user) {
        lblCreateUser.setText("Edit User");
        lblPassword.setText("New Password");
        lblConfirmPassword.setText("Confirm Password");

        txtfName.setText(user.getName());
        txtfEmail.setText(user.getEmail());
        txtfEmail.setEditable(false);

        cbRoles.getSelectionModel().select(user.getRole());
        buttonArea.getChildren().remove(btnConfirm);
        addEditBtn(user);

        selectedUser = user;
    }

    private void addEditBtn(SystemUser user) {
        JFXButton button = createButton("✔ Confirm Edit");
        buttonArea.getChildren().add(0, button);

        button.setOnMouseClicked(event -> {
            if(validateInput()) {
                SystemUser systemUser = createSystemUserFromFields();
                try {
                    Task<Boolean> updateSystemUserTask = getModelsHandler()
                            .getSystemUserModel()
                            .updateSystemUser(systemUser, selectedUser);

                    updateSystemUserTask.setOnFailed(failedEvent -> displayError(updateSystemUserTask.getException()));

                    TaskExecutor.executeTask(updateSystemUserTask);

                    handleBack();
                }
                catch (Exception e) {
                    displayError(e);
                }
            }
        });
    }


}
