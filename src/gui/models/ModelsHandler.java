package gui.models;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ModelsHandler {
    private static ModelsHandler modelsHandler;
    private SystemUserModel systemUserModel;
    private ProjectModel projectModel;
    private ClientModel clientModel;
    private InstallationModel installationModel;
    private AddressModel addressModel;
    private DrawingModel drawingModel;
    private PhotoModel photoModel;


    private ModelsHandler() throws Exception {
        try (ExecutorService executorService = Executors.newFixedThreadPool(6)) {
            Future<SystemUserModel> systemUserModelFuture = executorService.submit(SystemUserModel::new);
            Future<ProjectModel> projectModelFuture = executorService.submit(ProjectModel::new);
            Future<ClientModel> clientModelFuture = executorService.submit(ClientModel::new);
            Future<InstallationModel> installationModelFuture = executorService.submit(InstallationModel::new);
            Future<AddressModel> addressModelFuture = executorService.submit(AddressModel::new);
            Future<PhotoModel> photoModelFuture = executorService.submit(PhotoModel::new);

            systemUserModel = systemUserModelFuture.get();
            projectModel = projectModelFuture.get();
            clientModel = clientModelFuture.get();
            installationModel = installationModelFuture.get();
            addressModel =  addressModelFuture.get();
            drawingModel = new DrawingModel();
            photoModel = photoModelFuture.get();
        }
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
    public AddressModel getAddressModel() {
        return addressModel;
    }

    public DrawingModel getDrawingModel() {
        return drawingModel;
    }

    public PhotoModel getPhotoModel() {
        return photoModel;
    }
}
