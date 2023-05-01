package gui.models;

import be.Project;
import bll.interfaces.IProjectManager;
import bll.managers.ProjectManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class ProjectModel {
    private IProjectManager projectManager;
    private List<Project> allProjects;
    private ObservableList<Project> filteredProjectList;

    public ProjectModel() throws Exception {
        projectManager = new ProjectManager();
        loadProjects();
    }

    public void loadProjects() throws Exception {
        List<Project> copyAllProjects = new ArrayList<>();
        allProjects = retrieveAllProjects();
        allProjects.forEach(project -> copyAllProjects.add(project));
        filteredProjectList = FXCollections.observableList(copyAllProjects);
    }

    public Project createProject(Project project) throws Exception {
        Project createdProject = projectManager.createProject(project);
        loadProjects();
        return createdProject;
    }

    public List<Project> retrieveAllProjects() throws Exception {
        return projectManager.getAllProjects();
    }

    public ObservableList<Project> getAllProjects() throws Exception {
        return filteredProjectList;
    }

    public void search(String query) {
        filteredProjectList.clear();

        if (!query.isBlank()) {
            filteredProjectList.addAll(projectManager.search(allProjects, query));
        }
        else {
            filteredProjectList.addAll(allProjects);
        }
    }
}
