package dal.facades;

import be.Client;
import be.Installation;
import be.Project;
import be.SystemUser;
import dal.dao.ClientDAO;
import dal.dao.InstallationDAO;
import dal.dao.ProjectDAO;
import dal.dao.SystemUserDAO;
import dal.interfaces.IClientDAO;
import dal.interfaces.IInstallationDAO;
import dal.interfaces.IProjectDAO;
import dal.interfaces.ISystemUserDAO;

import java.sql.Timestamp;

public class DeleteFacade {
    private ISystemUserDAO systemUserDAO;
    private IClientDAO clientDAO;
    private IInstallationDAO installationDAO;
    private IProjectDAO projectDAO;

    public DeleteFacade() throws Exception {
        systemUserDAO = new SystemUserDAO();
        clientDAO = new ClientDAO();
        installationDAO = new InstallationDAO();
        projectDAO = new ProjectDAO();
    }

    public void deleteSystemUser(SystemUser deletedSystemUser) throws Exception {
        systemUserDAO.updateSystemUser(deletedSystemUser);
    }

    public void deleteInstallation(Installation deletedInstallation) throws Exception {
        deletedInstallation.setDeleted(new Timestamp(System.currentTimeMillis()));
        installationDAO.updateInstallation(deletedInstallation);
    }

    public void deleteProject(Project deletedProject) throws Exception {
        for (Installation i : installationDAO.getInstallationsFromProject(deletedProject.getID())) {
            deleteInstallation(i);
        }
        deletedProject.setDeleted(new Timestamp(System.currentTimeMillis()));
        projectDAO.updateProject(deletedProject);
    }

    public void deleteClient(Client deletedClient) throws Exception {
        for (Project p : projectDAO.getAllProjects()) {
            if(p.getClient().getID() == deletedClient.getID()) {
                deleteProject(p);
            }
        }
        deletedClient.setDeleted(new Timestamp(System.currentTimeMillis()));
        clientDAO.updateClient(deletedClient);
    }

}
