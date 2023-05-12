package gui.models;

import be.Project;
import be.SystemUser;
import bll.interfaces.IProjectManager;
import bll.managers.ProjectManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;

import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ProjectModel implements Runnable {
    private IProjectManager projectManager;
    private List<Project> allProjects;
    private ObservableList<Project> filteredProjectList;

    private String searchString;

    private Timestamp lastUpdateTime;

    private List<Project> copyAllProjects;

    public ProjectModel() throws Exception {
        projectManager = new ProjectManager();


        allProjects = retrieveAllProjects();
        copyAllProjects = new ArrayList<>(allProjects);

        filteredProjectList = FXCollections.observableList(copyAllProjects);
        lastUpdateTime = new Timestamp(System.currentTimeMillis());
    }

    public Task<Project> createProject(Project project) {
        Task<Project> createProjectTask = new Task<>() {
            @Override
            protected Project call() throws Exception {
                Project createdProject = projectManager.createProject(project);

                if(createdProject != null){
                    allProjects.add(createdProject);
                    search("");
                }

                updateValue(createdProject);

                return createdProject;
            }
        };

        return createProjectTask;
    }

    public List<Project> retrieveAllProjects() throws Exception {
        copyAllProjects = new ArrayList<>(projectManager.getAllProjects());
        return copyAllProjects;
    }

    public ObservableList<Project> getAllProjects() throws Exception {
        return filteredProjectList;
    }

    public Project softDeleteProject (Project project) throws Exception {
        return null;
    }

    public Task<Boolean> updateProject(Project updatedProject) {
        Task<Boolean> updateProjectTask = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                boolean successfullyUpdatedProject = projectManager.updateProject(updatedProject) != null;

                if (successfullyUpdatedProject) {

                    allProjects.removeIf(project -> project.getID() == updatedProject.getID());

                    allProjects.add(updatedProject);

                    search(searchString);
                }

                updateValue(successfullyUpdatedProject);

                return successfullyUpdatedProject;
            }
        };

        return updateProjectTask;
    }

    public void deleteProject(Project deletedProject) throws Exception {
        projectManager.deleteProject(deletedProject);
    }

    public Task<Void> assignSystemUserToProject(int projectId, String systemUserEmail) {
        Task<Void> assignUserToProjectTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                projectManager.assignSystemUserToProject(projectId, systemUserEmail);

                return null;
            }
        };

        return assignUserToProjectTask;
    }

    public Task<List<SystemUser>> getSystemUsersAssignedToProject(int projectId) {
        Task<List<SystemUser>> usersAssignedToProjectTask = new Task<>() {
            @Override
            protected List<SystemUser> call() throws Exception {
                List<SystemUser> usersAssignedToProject = projectManager.getSystemUsersAssignedToProject(projectId);

                updateValue(usersAssignedToProject);

                return usersAssignedToProject;
            }
        };

        return usersAssignedToProjectTask;
    }

    public Task<Void> deleteSystemUserAssignedToProject(int projectId, String systemUserEmail) {
        Task<Void> deleteUserAssignedToProject = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                projectManager.deleteSystemUserAssignedToProject(projectId, systemUserEmail);

                return null;
            }
        };

        return deleteUserAssignedToProject;
    }

    public void search(String query) {
        filteredProjectList.clear();
        if(query != null) {
            searchString = query;
            filteredProjectList.addAll(projectManager.search(allProjects, query));
            } else {
            filteredProjectList.addAll(allProjects);
        }

    }

    @Override
    public void run() {
        System.out.println("projects update");

        List<Project> updatedProjects;
        try {
            updatedProjects = projectManager.getModifiedProjects(lastUpdateTime);
            lastUpdateTime.setTime(System.currentTimeMillis());

            if(updatedProjects.size() > 0){
                allProjects = retrieveAllProjects();
                search(searchString);
            }
            Thread.sleep(300);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
