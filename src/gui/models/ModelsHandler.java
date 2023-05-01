package gui.models;

import be.Client;

public class ModelsHandler {
    private static ModelsHandler modelsHandler;
    private SystemUserModel systemUserModel;
    private ClientModel clientModel;


    private ModelsHandler() throws Exception {
        systemUserModel = new SystemUserModel();
        clientModel = new ClientModel();
    }

    public static ModelsHandler getInstance() throws Exception {
        if (modelsHandler == null) modelsHandler = new ModelsHandler();

        return modelsHandler;
    }

    public SystemUserModel getSystemUserModel() {
        return systemUserModel;
    }

    public ClientModel getClientModel() {
        return clientModel;
    }
}
