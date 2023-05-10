package dal.dao;

import be.Client;
import be.Project;
import dal.connectors.AbstractConnector;
import dal.connectors.SqlConnector;
import dal.interfaces.IProjectDAO;
import exceptions.DALException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProjectDAO implements IProjectDAO {
    private AbstractConnector connector;

    public ProjectDAO() throws Exception {
        connector = new SqlConnector();
    }

    @Override
    public Project softDeleteProject(Project project) throws Exception {

        Project softDeletedProject = null;

        String sql = "UPDATE Project SET SoftDelete=CURRENT_DATE WHERE ID=?;";

        try(Connection connection = connector.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setDate(6, java.sql.Date.valueOf(LocalDate.now()));
            statement.executeQuery();

        } catch (Exception e) {
            throw new Exception("Failed to soft delete project", e);
        }
        return softDeletedProject;

    }

    @Override
    public Project createProject(Project project) throws Exception {
        Project newProject = null;
        String sql = "INSERT INTO Project (Name, Client, Location, Created, SoftDelete, Description) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, project.getName());
            statement.setInt(2, project.getClient().getID());
            statement.setString(3, project.getLocation());
            Timestamp timestamp = new Timestamp(project.getCreated().getTime());
            statement.setTimestamp(4, timestamp);
            statement.setDate(5, null);
            statement.setString(6, project.getDescription());

            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();

            if(resultSet.next()) {
                int ID = resultSet.getInt(1);
                newProject = new Project(ID, project.getName(), project.getClient(), project.getLocation(), project.getCreated(), project.getDescription());
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

        String sql = "SELECT " +
                "Project.ID AS 'ProjectID', Project.Name AS 'ProjectName', Project.[Location] AS 'ProjectLocation', Project.Created AS 'ProjectCreated', Project.[Description] AS 'ProjectDescription', " +
                "Client.ID AS 'ClientID', Client.Name AS 'ClientName', Client.ClientLocation, Client.Email 'ClientEmail', Client.Phone AS 'ClientPhone', Client.[Type] AS 'ClientType' " +
                "FROM Project " +
                "INNER JOIN Client " +
                "ON Client.ID=Project.Client " +
                "WHERE Project.SoftDelete IS NULL;";

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                //Mapping the client
                int clientID = resultSet.getInt("ClientID");
                String clientName = resultSet.getString("ClientName");
                String clientLocation = resultSet.getString("ClientLocation");
                String email = resultSet.getString("ClientEmail");
                String phone = resultSet.getString("ClientPhone");
                String type = resultSet.getString("ClientType");
                
                Client client = new Client(clientID, clientName, clientLocation, email, phone, type);

                //Mapping the project
                int ID = resultSet.getInt("ProjectID");
                String name = resultSet.getString("ProjectName");
                String location = resultSet.getString("ProjectLocation");
                Date created = resultSet.getDate("ProjectCreated");
                String description = resultSet.getString("ProjectDescription");

                Project project = new Project(ID, name, client, location, created, description);

                allProjects.add(project);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DALException("Failed to read all projects", e);
        }

        return allProjects;
    }

    @Override
    public Project updateProject(Project project) throws Exception {
        Project updatedProject = null;

        String sql = "UPDATE Project SET Name=?, Location=?, Description=? WHERE ID=?;";

        try (Connection connection = connector.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, project.getName());
            statement.setString(2, project.getLocation());
            statement.setString(3, project.getDescription());
            statement.setInt(4, project.getID());

            statement.executeUpdate();
            updatedProject = project;

        } catch (SQLException e) {
            DALException dalException = new DALException("Failed to update project", e);
            dalException.printStackTrace(); //TODO replace with log to the database.

            throw dalException;
        }
        return updatedProject;
    }
}
