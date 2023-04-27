package gui.controllers;

import gui.util.MainControllerHandler;

public class AddProjectController extends BaseController {
    public void handleCancel() {
        MainViewController mainViewController = MainControllerHandler.getInstance().getController();
        mainViewController.mainBorderPane.setCenter(mainViewController.getLastView());
    }
}
