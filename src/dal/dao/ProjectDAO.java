package dal.dao;

import be.Project;
import dal.connectors.AbstractConnector;
import dal.connectors.SqlConnector;
import dal.interfaces.IProjectDAO;
import exceptions.DALException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProjectDAO implements IProjectDAO {
    private AbstractConnector connector;

    public ProjectDAO() throws Exception {
        connector = new SqlConnector();
    }

    @Override
    public Project createProject(Project project) throws Exception {
        Project newProject = null;
        String sql = "INSERT INTO Project (Name, Client, Location, Created) VALUES (?, ?, ?, ?)";

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, project.getName());
            statement.setInt(2, project.getClientID());
            statement.setString(3, project.getLocation());
            Timestamp timestamp = new Timestamp(project.getCreated().getTime());
            statement.setTimestamp(4, timestamp);

            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();

            if(resultSet.next()) {
                int ID = resultSet.getInt(1);
                newProject = new Project(ID, project.getName(), project.getClientID(), project.getLocation(), project.getCreated());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DALException("Failed to create project", e);
        }

        return newProject;
    }

    @Override
    public List<Project> getAllProjects() throws Exception {
        ArrayList<Project> allProjects = new ArrayList<>();

        String sql = "SELECT * FROM Project;";

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                int ID = resultSet.getInt(1);
                String name = resultSet.getString(2);
                int clientID = resultSet.getInt(3);
                String location = resultSet.getString(4);
                Date created = resultSet.getDate(5);
                Project project = new Project(ID, name, clientID, location, created);
                allProjects.add(project);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DALException("Failed to read all projects", e);
        }

        return allProjects;
    }
}
