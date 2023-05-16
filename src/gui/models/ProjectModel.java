package gui.models;

import be.Enum.ProjectStatus;
import be.Project;
import be.SystemUser;
import bll.interfaces.IProjectManager;
import bll.managers.ProjectManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProjectModel implements Runnable {
    private IProjectManager projectManager;

    private String searchString;
    private String userSearchString;

    private Timestamp lastUpdateTime;

    private List<Project> allProjects;
    private ObservableList<Project> filteredProjectList;
    private List<Project> copyAllProjects;

    private List<Project> allMyProjects;
    private ObservableList<Project> myFilteredProjectList;
    private List<Project> copyAllMyProjects;



    private ObservableList<SystemUser> users;

    private List<SystemUser> allUsersOnProject;
    private ObservableList<SystemUser> filteredUserList;
    private List<SystemUser> copyUsersOnProject;

    private List<SystemUser> allUsersNotOnProject;
    private List<SystemUser> copyUsersNotOnProject;

    public ProjectModel() throws Exception {
        projectManager = new ProjectManager();


        allProjects = retrieveAllProjects();
        copyAllProjects = new ArrayList<>(allProjects);
        filteredProjectList = FXCollections.observableList(copyAllProjects);

        lastUpdateTime = new Timestamp(System.currentTimeMillis());
    }

    public ObservableList<SystemUser> getAllUsersOnProject(int projectID) throws Exception {
        allUsersOnProject = projectManager.getSystemUsersAssignedToProject(projectID);
        copyUsersOnProject = new ArrayList<>(allUsersOnProject);
        filteredUserList = FXCollections.observableList(copyUsersOnProject);
        return filteredUserList;
    }

    public ObservableList<SystemUser> getAllUsersNotOnProject(int projectID) throws Exception {
        allUsersNotOnProject = projectManager.getAllUserNotAssignedToProject(projectID);
        copyUsersNotOnProject = new ArrayList<>(allUsersNotOnProject);
        filteredUserList = FXCollections.observableList(copyUsersNotOnProject);
        return filteredUserList;
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

    public List<Project> retrieveAllMyProjects(String systemUserEmail) throws Exception {
        allMyProjects = projectManager.getAllProjectsAssignedToUser(systemUserEmail);
        copyAllMyProjects = new ArrayList<>(allMyProjects);
        myFilteredProjectList = FXCollections.observableList(copyAllMyProjects);
        copyAllMyProjects = new ArrayList<>(
                projectManager.getAllProjectsAssignedToUser(systemUserEmail));

        return copyAllMyProjects;
    }


    public ObservableList<Project> getAllMyProjects(String userEmail) throws Exception {
        retrieveAllMyProjects(userEmail);
        return myFilteredProjectList;
    }

    public ObservableList<Project> getAllProjects() throws Exception {
        return filteredProjectList;
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

    public Task<Void> deleteProject(Project deletedProject) throws Exception {

        Task<Void> deleteProjectTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                projectManager.deleteProject(deletedProject);
                return null;
            }
        };

        return deleteProjectTask;
    }

    public Task<Void> assignSystemUserToProject(int projectId, SystemUser user) {

        Task<Void> assignUserToProjectTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                projectManager.assignSystemUserToProject(projectId, user.getEmail());
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
                copyUsersOnProject = new ArrayList<SystemUser>(usersAssignedToProject);
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
    public void searchMyProjects(String query) {
        myFilteredProjectList.clear();
        if(query != null) {
            searchString = query;
            myFilteredProjectList.addAll(projectManager.search(allMyProjects, query));
        } else {
            myFilteredProjectList.addAll(allMyProjects);
        }
    }


    public ObservableList<ProjectStatus> getAllStatuses() {
        return FXCollections.observableList(Arrays.stream(ProjectStatus.values()).toList());
    }

    @Override
    public void run() {
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

    public ObservableList<SystemUser> getAllUsersNotAssignedProject(int projectId) throws Exception {
        users = FXCollections.observableList(projectManager.getAllUserNotAssignedToProject(projectId));
        return users;
    }




}
