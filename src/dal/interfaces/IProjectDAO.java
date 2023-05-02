package dal.interfaces;

import be.Project;

import java.util.List;

public interface IProjectDAO {
    Project softDeleteProject(Project project) throws Exception;

    Project createProject(Project project) throws Exception;
    List<Project> getAllProjects() throws Exception;
}
