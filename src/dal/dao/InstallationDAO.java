package dal.dao;

import be.Installation;
import dal.connectors.AbstractConnector;
import dal.connectors.SqlConnector;
import dal.interfaces.IInstallationDAO;
import exceptions.DALException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InstallationDAO implements IInstallationDAO {
    private AbstractConnector connector;

    public InstallationDAO() throws Exception {
        connector = new SqlConnector();
    }

    @Override
    public Installation createInstallation(Installation installation) throws Exception {
        Installation newInstallation = null;
        String sql = "INSERT INTO Installation (ProjectID, Name, Description, Drawing, Is_Done) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, installation.getProjectID());
            statement.setString(2, installation.getName());
            statement.setString(3, installation.getDescription());
            statement.setBytes(4, installation.getDrawingBytes());
            statement.setInt(5, installation.getIsDoneInt());

            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();

            if(resultSet.next()) {
                int ID = resultSet.getInt(1);

                //Converting int to boolean
                boolean isDone = false;
                if(installation.getIsDoneInt() == 1)
                    isDone = true;

                newInstallation = new Installation(ID, installation.getProjectID(), installation.getName(),
                        installation.getDescription(), installation.getDrawingBytes(), isDone);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DALException("Failed to create installation", e);
        }

        return newInstallation;
    }

    @Override
    public List<Installation> getInstallationsFromProject(int projectID) throws Exception {
        List<Installation> allInstallations = new ArrayList<>();

        String sql = "SELECT * FROM Installation WHERE ProjectID = ?;";

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, projectID);

            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                //Mapping the installation
                int ID = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String description = resultSet.getString(3);
                byte[] drawingBytes = resultSet.getBytes(4);
                int isDoneInt = resultSet.getInt(5);

                //Converting int to boolean
                boolean isDone = false;
                if(isDoneInt == 1)
                    isDone = true;

                Installation installation = new Installation(ID, projectID, name, description, drawingBytes, isDone);

                allInstallations.add(installation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DALException("Failed to read installations from project", e);
        }
        return allInstallations;
    }

    @Override
    public Installation updateInstallation(Installation installation) throws Exception {
        //TODO Implement
        return null;
    }
}
