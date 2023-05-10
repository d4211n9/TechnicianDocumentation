package gui.models;

import be.Project;
import be.SystemUser;
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

    private String searchString;

    public ProjectModel() throws Exception {
        projectManager = new ProjectManager();

        allProjects = retrieveAllProjects();
        List<Project> copyAllUsers = new ArrayList<>(allProjects);
        filteredProjectList = FXCollections.observableList(copyAllUsers);
    }

    public Project createProject(Project project) throws Exception {
        Project createdProject = projectManager.createProject(project);


        if(createdProject != null){
            allProjects.add(createdProject);
            search("");
        }
        return createdProject;
    }

    public List<Project> retrieveAllProjects() throws Exception {
        return projectManager.getAllProjects();
    }

    public ObservableList<Project> getAllProjects() throws Exception {
        return filteredProjectList;
    }

    public Project softDeleteProject (Project project) throws Exception {
        return null;
    }

    public boolean updateProject(Project updatedProject) throws Exception {

        if (projectManager.updateProject(updatedProject) != null) {

            allProjects.removeIf(project -> project.getID() == updatedProject.getID());

            allProjects.add(updatedProject);

            search(searchString);
            return true;
        }
            return false;
    }

    public void assignSystemUserToProject(int projectId, String systemUserEmail) throws Exception {
        projectManager.assignSystemUserToProject(projectId, systemUserEmail);
    }

    public List<SystemUser> getSystemUsersAssignedToProject(int projectId) throws Exception {
        return projectManager.getSystemUsersAssignedToProject(projectId);
    }

    public void deleteSystemUserAssignedToProject(int projectId, String systemUserEmail) throws Exception {
        projectManager.deleteSystemUserAssignedToProject(projectId, systemUserEmail);
    }

    public void search(String query) {
        filteredProjectList.clear();
        if(query != null) {
            searchString = query;
            if (!query.isBlank()) {
                filteredProjectList.addAll(projectManager.search(allProjects, query));
            } else {
                filteredProjectList.addAll(allProjects);
            }
        }
    }
}
