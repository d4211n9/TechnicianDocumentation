package gui.controllers;

import gui.util.MainControllerHandler;

public class AddProjectController extends BaseController {
    public void handleCancel() {
        MainController mainController = MainControllerHandler.getInstance().getController();
        mainController.mainBorderPane.setCenter(mainController.getLastView());
    }
}
