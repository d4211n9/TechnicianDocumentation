package dal.dao;

import be.Client;
import be.Project;
import dal.connectors.AbstractConnector;
import dal.connectors.SqlConnector;
import dal.interfaces.IClientDAO;
import exceptions.DALException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClientDAO implements IClientDAO {
    private AbstractConnector connector;

    public ClientDAO() throws Exception {
        connector = new SqlConnector();
    }

    @Override
    public Client createClient(Client client) throws Exception {
        Client newClient = null;
        String sql = "INSERT INTO Client (Name, Email, Phone, Type) VALUES (?, ?, ?, ?);";

        try (Connection conn = connector.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, client.getName());
            statement.setString(2, client.getEmail());
            statement.setString(3, client.getPhone());
            statement.setString(4, client.getType());

            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();

            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                newClient = new Client(id, client.getName(), client.getEmail(), client.getPhone(), client.getType());
            }
        }
        catch (Exception e) {
            throw new Exception("Failed to create event", e);
        }

        return newClient;
    }

    @Override
    public List<Client> getAllClients() throws Exception {
        List<Client> allClients = new ArrayList<>();

        String sql = "SELECT * FROM Client;";

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                //Mapping the client
                int clientID = resultSet.getInt(1);
                String clientName = resultSet.getString(2);
                String email = resultSet.getString(3);
                String phone = resultSet.getString(4);
                String type = resultSet.getString(5);

                Client client = new Client(clientID, clientName, email, phone, type);

                allClients.add(client);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DALException("Failed to read all clients", e);
        }

        return allClients;
    }
}
