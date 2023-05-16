package gui.models;

public class ModelsHandler {
    private static ModelsHandler modelsHandler;
    private SystemUserModel systemUserModel;
    private ProjectModel projectModel;
    private ClientModel clientModel;
    private InstallationModel installationModel;
    private PhotoModel photoModel;


    private ModelsHandler() throws Exception {
        systemUserModel = new SystemUserModel();
        projectModel = new ProjectModel();
        clientModel = new ClientModel();
        installationModel = new InstallationModel();
        photoModel = new PhotoModel();
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

    public InstallationModel getInstallationModel() {
        return installationModel;
    }

    public PhotoModel getPhotoModel() {
        return photoModel;
    }
}
