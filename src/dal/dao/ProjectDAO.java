package dal.dao;

import be.Address;
import be.Client;
import be.Enum.ClientType;
import be.Enum.ProjectStatus;
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
        String sql = "INSERT INTO Project (Name, Client, AddressID, Created, SoftDelete, Description, LastModified, Status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, project.getName());
            statement.setInt(2, project.getClient().getID());
            statement.setInt(3, project.getAddress().getID());
            Timestamp timestamp = new Timestamp(project.getCreated().getTime());
            statement.setTimestamp(4, timestamp);
            statement.setDate(5, null);
            statement.setString(6, project.getDescription());
            Timestamp t = new Timestamp(System.currentTimeMillis());
            statement.setTimestamp(7, t);
            statement.setString(8, project.getStatus().getStatus());

            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();

            if(resultSet.next()) {
                int ID = resultSet.getInt(1);
                newProject = new Project(ID, project.getName(), project.getClient(), project.getAddress(), project.getCreated(), project.getDescription(), project.getStatus());
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
                "Project.ID AS 'ProjectID', Project.Name AS 'ProjectName', Project.[AddressID] AS 'ProjectAddressID', Project.Created AS 'ProjectCreated', Project.[Description] AS 'ProjectDescription', Project.Status AS 'Status', " +
                "Client.ID AS 'ClientID', Client.Name AS 'ClientName', Client.AddressID AS 'ClientAddressID', Client.Email 'ClientEmail', Client.Phone AS 'ClientPhone', Client.[Type] AS 'ClientType', " +
                "ClientAddress.ID AS 'ClientAddressID', ClientAddress.Street AS 'ClientStreet', ClientAddress.PostalCode AS 'ClientPostalCode', ClientAddress.City AS 'ClientCity', " +
                "ProjectAddress.ID AS 'ProjectAddressID', ProjectAddress.Street AS 'ProjectStreet', ProjectAddress.PostalCode AS 'ProjectPostalCode', ProjectAddress.City AS 'ProjectCity' " +
                "FROM Project " +
                "INNER JOIN Client ON Client.ID=Project.Client " +
                "INNER JOIN Address ClientAddress ON ClientAddress.ID = Client.AddressID " +
                "INNER JOIN Address ProjectAddress ON ProjectAddress.ID = Project.AddressID " +
                "WHERE Project.SoftDelete IS NULL;";

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                //Mapping the client address
                int clientAddressID = resultSet.getInt("ClientAddressID");
                String clientStreet = resultSet.getString("ClientStreet");
                String clientPostalCode = resultSet.getString("ClientPostalCode");
                String clientCity = resultSet.getString("ClientCity");
                Address clientAddress = new Address(clientAddressID, clientStreet, clientPostalCode, clientCity);

                //Mapping the client
                int clientID = resultSet.getInt("ClientID");
                String clientName = resultSet.getString("ClientName");
                String email = resultSet.getString("ClientEmail");
                String phone = resultSet.getString("ClientPhone");
                String type = resultSet.getString("ClientType");

                Client client = new Client(clientID, clientName, clientAddress, email, phone, ClientType.getClientType(type));

                //Mapping the project address
                int projectAddressID = resultSet.getInt("ProjectAddressID");
                String projectStreet = resultSet.getString("ProjectStreet");
                String projectPostalCode = resultSet.getString("ProjectPostalCode");
                String projectCity = resultSet.getString("ProjectCity");
                Address projectAddress = new Address(projectAddressID, projectStreet, projectPostalCode, projectCity);

                //Mapping the project
                int ID = resultSet.getInt("ProjectID");
                String name = resultSet.getString("ProjectName");
                Date created = resultSet.getDate("ProjectCreated");
                String description = resultSet.getString("ProjectDescription");
                String status = resultSet.getString("Status");
                ProjectStatus projectStatus = ProjectStatus.getProjectStatus(status);

                Project project = new Project(ID, name, client, projectAddress, created, description, projectStatus);

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

        String sql = "UPDATE Project SET Name=?, AddressID=?, Description=?, SoftDelete=?, LastModified=?, Status=? WHERE ID=?;";

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, project.getName());
            statement.setInt(2, project.getAddress().getID());
            statement.setString(3, project.getDescription());
            statement.setTimestamp(4, project.getDeleted());
            Timestamp t = new Timestamp(System.currentTimeMillis());
            statement.setTimestamp(5, t);
            statement.setString(6, project.getStatus().getStatus());
            statement.setInt(7, project.getID());

            statement.executeUpdate();
            updatedProject = project;

        } catch (SQLException e) {
            DALException dalException = new DALException("Failed to update project", e);
            dalException.printStackTrace(); //TODO replace with log to the database.

            throw dalException;
        }
        return updatedProject;
    }

    public List<Project> getModifiedProjects(Timestamp lastCheck) throws Exception {
        ArrayList<Project> allProjects = new ArrayList<>();

        String sql = "SELECT " +
                "Project.ID AS 'ProjectID', Project.Name AS 'ProjectName', Project.[AddressID] AS 'ProjectAddressID', Project.Created AS 'ProjectCreated', Project.[Description] AS 'ProjectDescription', Project.Status AS 'Status', " +
                "Client.ID AS 'ClientID', Client.Name AS 'ClientName', Client.AddressID AS 'ClientAddressID', Client.Email 'ClientEmail', Client.Phone AS 'ClientPhone', Client.[Type] AS 'ClientType', " +
                "ClientAddress.ID AS 'ClientAddressID', ClientAddress.Street AS 'ClientStreet', ClientAddress.PostalCode AS 'ClientPostalCode', ClientAddress.City AS 'ClientCity', " +
                "ProjectAddress.ID AS 'ProjectAddressID', ProjectAddress.Street AS 'ProjectStreet', ProjectAddress.PostalCode AS 'ProjectPostalCode', ProjectAddress.City AS 'ProjectCity' " +
                "FROM Project " +
                "INNER JOIN Client ON Client.ID=Project.Client " +
                "INNER JOIN Address ClientAddress ON ClientAddress.ID = Client.AddressID " +
                "INNER JOIN Address ProjectAddress ON ProjectAddress.ID = Project.AddressID " +
                "WHERE Project.LastModified>?;";

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setTimestamp(1, lastCheck);

            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                //Mapping the client address
                int clientAddressID = resultSet.getInt("ClientAddressID");
                String clientStreet = resultSet.getString("ClientStreet");
                String clientPostalCode = resultSet.getString("ClientPostalCode");
                String clientCity = resultSet.getString("ClientCity");
                Address clientAddress = new Address(clientAddressID, clientStreet, clientPostalCode, clientCity);

                //Mapping the client
                int clientID = resultSet.getInt("ClientID");
                String clientName = resultSet.getString("ClientName");
                String email = resultSet.getString("ClientEmail");
                String phone = resultSet.getString("ClientPhone");
                String type = resultSet.getString("ClientType");

                Client client = new Client(clientID, clientName, clientAddress, email, phone, ClientType.getClientType(type));

                //Mapping the project address
                int projectAddressID = resultSet.getInt("ProjectAddressID");
                String projectStreet = resultSet.getString("ProjectStreet");
                String projectPostalCode = resultSet.getString("ProjectPostalCode");
                String projectCity = resultSet.getString("ProjectCity");
                Address projectAddress = new Address(projectAddressID, projectStreet, projectPostalCode, projectCity);

                //Mapping the project
                int ID = resultSet.getInt("ProjectID");
                String name = resultSet.getString("ProjectName");
                Date created = resultSet.getDate("ProjectCreated");
                String description = resultSet.getString("ProjectDescription");
                String status = resultSet.getString("Status");
                ProjectStatus projectStatus = ProjectStatus.getProjectStatus(status);

                Project project = new Project(ID, name, client, projectAddress, created, description, projectStatus);

                allProjects.add(project);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DALException("Failed to read all projects", e);
        }

        return allProjects;
    }

    public List<Project> getAllProjectsAssignedToUser(String systemUserID) throws DALException {
        ArrayList<Project> allProjects = new ArrayList<>();
        String sql = "SELECT " +
                "Project.ID AS 'ProjectID', Project.Name AS 'ProjectName', Project.[AddressID] AS 'ProjectAddressID', Project.Created AS 'ProjectCreated', Project.[Description] AS 'ProjectDescription', Project.Status AS 'Status', " +
                "Client.ID AS 'ClientID', Client.Name AS 'ClientName', Client.AddressID AS 'ClientAddressID', Client.Email 'ClientEmail', Client.Phone AS 'ClientPhone', Client.[Type] AS 'ClientType', " +
                "ClientAddress.ID AS 'ClientAddressID', ClientAddress.Street AS 'ClientStreet', ClientAddress.PostalCode AS 'ClientPostalCode', ClientAddress.City AS 'ClientCity', " +
                "ProjectAddress.ID AS 'ProjectAddressID', ProjectAddress.Street AS 'ProjectStreet', ProjectAddress.PostalCode AS 'ProjectPostalCode', ProjectAddress.City AS 'ProjectCity' " +
                "FROM Project " +
                "INNER JOIN Client ON Client.ID=Project.Client " +
                "INNER JOIN Address ClientAddress ON ClientAddress.ID = Client.AddressID " +
                "INNER JOIN Address ProjectAddress ON ProjectAddress.ID = Project.AddressID " +
                "INNER JOIN SystemUsersAssignedToProjects SystemUser ON Project.ID = SystemUser.ProjectID " +
                "WHERE Project.SoftDelete IS NULL AND SystemUser.SystemUserEmail =?;";

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, systemUserID);

            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                //Mapping the client address
                int clientAddressID = resultSet.getInt("ClientAddressID");
                String clientStreet = resultSet.getString("ClientStreet");
                String clientPostalCode = resultSet.getString("ClientPostalCode");
                String clientCity = resultSet.getString("ClientCity");
                Address clientAddress = new Address(clientAddressID, clientStreet, clientPostalCode, clientCity);

                //Mapping the client
                int clientID = resultSet.getInt("ClientID");
                String clientName = resultSet.getString("ClientName");
                String email = resultSet.getString("ClientEmail");
                String phone = resultSet.getString("ClientPhone");
                String type = resultSet.getString("ClientType");

                Client client = new Client(clientID, clientName, clientAddress, email, phone, ClientType.getClientType(type));

                //Mapping the project address
                int projectAddressID = resultSet.getInt("ProjectAddressID");
                String projectStreet = resultSet.getString("ProjectStreet");
                String projectPostalCode = resultSet.getString("ProjectPostalCode");
                String projectCity = resultSet.getString("ProjectCity");
                Address projectAddress = new Address(projectAddressID, projectStreet, projectPostalCode, projectCity);

                //Mapping the project
                int ID = resultSet.getInt("ProjectID");
                String name = resultSet.getString("ProjectName");
                Date created = resultSet.getDate("ProjectCreated");
                String description = resultSet.getString("ProjectDescription");
                String status = resultSet.getString("Status");
                ProjectStatus projectStatus = ProjectStatus.getProjectStatus(status);

                Project project = new Project(ID, name, client, projectAddress, created, description, projectStatus);
                allProjects.add(project);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new DALException("Failed to read all projects", e);
        }

        return allProjects;
    }

}