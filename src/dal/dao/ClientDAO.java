package dal.dao;

import be.Address;
import be.Client;
import dal.connectors.AbstractConnector;
import dal.connectors.SqlConnector;
import dal.interfaces.IClientDAO;
import exceptions.DALException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO implements IClientDAO {
    private AbstractConnector connector;

    public ClientDAO() throws Exception {
        connector = new SqlConnector();
    }

    @Override
    public Client createClient(Client client) throws Exception {
        Client newClient = null;
        String sql = "INSERT INTO Client (Name, AddressID, Email, Phone, Type, SoftDelete, LastModified) VALUES (?, ?, ?, ?, ?, ?, ?);";

        try (Connection conn = connector.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, client.getName());
            statement.setInt(2, client.getAddress().getID());
            statement.setString(3, client.getEmail());
            statement.setString(4, client.getPhone());
            statement.setString(5, client.getType());
            statement.setDate(6, null);
            Timestamp t = new Timestamp(System.currentTimeMillis());
            statement.setTimestamp(7, t);

            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();

            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                newClient = new Client(id, client.getName(), client.getAddress(), client.getEmail(), client.getPhone(), client.getType());
            }
        }
        catch (Exception e) {
            throw new Exception("Failed to create client", e);
        }

        return newClient;
    }

    @Override
    public List<Client> getAllClients() throws Exception {
        List<Client> allClients = new ArrayList<>();

        String sql = "SELECT * " +
                "FROM Client " +
                "INNER JOIN Address " +
                "ON Address.ID = Client.AddressID " +
                "WHERE SoftDelete IS NULL;";

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                //Mapping the client address
                int addressID = resultSet.getInt(8);
                String street = resultSet.getString(9);
                String postalCode = resultSet.getString(10);
                String city = resultSet.getString(11);
                Address clientAddress = new Address(addressID, street, postalCode, city);

                //Mapping the client
                int clientID = resultSet.getInt(1);
                String clientName = resultSet.getString(2);
                String email = resultSet.getString(4);
                String phone = resultSet.getString(5);
                String type = resultSet.getString(6);

                Client client = new Client(clientID, clientName, clientAddress, email, phone, type);

                allClients.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DALException("Failed to read all clients", e);
        }
        return allClients;
    }

    @Override
    public Client updateClient(Client client) throws Exception {
        Client updatedClient = null;
        String sql = "UPDATE Client SET Name=?, AddressID=?, Email=?, Phone=?, Type=? , LastModified=? WHERE ID=?;";
        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, client.getName());
            statement.setInt(2, client.getAddress().getID());
            statement.setString(3, client.getEmail());
            statement.setString(4, client.getPhone());
            statement.setString(5, client.getType());
            Timestamp t = new Timestamp(System.currentTimeMillis());
            statement.setTimestamp(6, t);
            statement.setInt(7, client.getID());

            statement.executeUpdate();

            updatedClient = client;
        } catch (SQLException e) {
            throw new Exception("Failed to update Client", e);
        }
        return updatedClient;
    }


    public List<Client> getAllModifiedClients(Timestamp lastCheck) throws Exception {
        List<Client> allClients = new ArrayList<>();

        String sql = "SELECT * FROM Client WHERE LastModified>?;";

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

                statement.setTimestamp(1, lastCheck);

            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                //Mapping the client
                int clientID = resultSet.getInt(1);
                String clientName = resultSet.getString(2);
                String clientLocation = resultSet.getString(3);
                String email = resultSet.getString(4);
                String phone = resultSet.getString(5);
                String type = resultSet.getString(6);

                Client client = new Client(clientID, clientName, clientLocation, email, phone, type);

                allClients.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DALException("Failed to read all clients", e);
        }
        return allClients;
    }
}
