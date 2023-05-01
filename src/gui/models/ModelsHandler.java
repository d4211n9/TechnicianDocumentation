package gui.models;

public class ModelsHandler {
    private static ModelsHandler modelsHandler;
    private SystemUserModel systemUserModel;
    private ProjectModel projectModel;


    private ModelsHandler() throws Exception {
        systemUserModel = new SystemUserModel();
        projectModel = new ProjectModel();
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

}
