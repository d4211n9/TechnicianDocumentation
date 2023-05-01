package dal.interfaces;

import be.Project;

import java.util.List;

public interface IProjectDAO {
    Project createProject(Project project) throws Exception;
    List<Project> getAllProjects() throws Exception;
}
