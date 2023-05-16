package dal.interfaces;

import be.Project;
import exceptions.DALException;

import java.sql.Timestamp;
import java.util.List;

public interface IProjectDAO {
    Project softDeleteProject(Project project) throws Exception;

    Project createProject(Project project) throws Exception;
    List<Project> getAllProjects() throws Exception;

    Project updateProject(Project project) throws Exception;
    List<Project> getModifiedProjects(Timestamp lastCheck) throws Exception;

    List<Project> getAllProjectsAssignedToUser(String systemUserID) throws DALException;
}
