package bll.interfaces;

import be.Project;

import java.util.List;

public interface IProjectManager {
    Project createProject(Project project) throws Exception;
    List<Project> getAllProjects() throws Exception;

    Project updateProject(Project project) throws Exception;

    List<Project> search(List<Project> allProjects, String query);
}
