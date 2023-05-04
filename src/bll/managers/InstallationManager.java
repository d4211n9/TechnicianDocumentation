package bll.managers;

import be.Installation;
import bll.interfaces.IInstallationManager;
import dal.facades.ProjectFacade;

import java.util.List;

public class InstallationManager implements IInstallationManager {
    private ProjectFacade projectFacade;

    public InstallationManager() throws Exception {
        projectFacade = new ProjectFacade();
    }

    @Override
    public Installation createInstallation(Installation installation) throws Exception {
        return projectFacade.createInstallation(installation);
    }

    @Override
    public List<Installation> getInstallationsFromProject(int projectID) throws Exception {
        return projectFacade.getInstallationsFromProject(projectID);
    }

    @Override
    public Installation updateInstallation(Installation installation) throws Exception {
        return projectFacade.updateInstallation(installation);
    }
}
