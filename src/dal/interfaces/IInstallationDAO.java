package dal.interfaces;

import be.Installation;

import java.util.List;

public interface IInstallationDAO {
    Installation createInstallation(Installation installation) throws Exception;
    List<Installation> getInstallationsFromProject(int projectID) throws Exception;
    Installation updateInstallation(Installation installation) throws Exception;
}
