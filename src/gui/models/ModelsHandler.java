package gui.models;

import be.Client;

public class ModelsHandler {
    private static ModelsHandler modelsHandler;
    private SystemUserModel systemUserModel;
    private ProjectModel projectModel;
    private ClientModel clientModel;


    private ModelsHandler() throws Exception {
        systemUserModel = new SystemUserModel();
        projectModel = new ProjectModel();
        clientModel = new ClientModel();
    }

    public static ModelsHandler getInstance() throws Exception {
        if (modelsHandler == null) modelsHandler = new ModelsHandler();

        return modelsHandler;
    }

    public SystemUserModel getSystemUserModel() {
        return systemUserModel;
    }

    public ProjectModel getProjectModel() {
        return projectModel;
    }


    public ClientModel getClientModel() {
        return clientModel;
    }
}
