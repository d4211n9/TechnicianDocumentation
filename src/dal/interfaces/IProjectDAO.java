package dal.interfaces;

import be.Project;

public interface IProjectDAO {
    Project createProject(Project project) throws Exception;
}
