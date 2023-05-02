package bll.interfaces;

import be.Project;

import java.util.List;

public interface IProjectManager {
    Project createProject(Project project) throws Exception;
    List<Project> getAllProjects() throws Exception;

    Project softDeleteProject(Project project) throws Exception;

    List<Project> search(List<Project> allProjects, String query);
}
