package gui.controllers;

import javafx.event.ActionEvent;
import javafx.scene.text.Text;

public class DeleteConfirmController {
    public Text deletedUserInfo;
    private Object object;

    public void setContent(Object object){
        this.object = object;
        deletedUserInfo.setText(object.toString());
    }

    public void handleDelete(ActionEvent actionEvent) {
    }

    public void handleCancel(ActionEvent actionEvent) {
    }
}
