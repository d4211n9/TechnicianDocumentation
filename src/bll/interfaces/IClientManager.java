package bll.interfaces;

import be.Client;

import java.sql.Timestamp;
import java.util.List;

public interface IClientManager {
    Client createClient(Client client) throws Exception;
    List<Client> getAllClients() throws Exception;
    List<Client> search(List<Client> allClients, String query) throws Exception;
    Client updateClient(Client client) throws Exception;
    void deleteClient(Client deletedClient) throws Exception;
    public List<Client> getAllModifiedClients(Timestamp lastCheck) throws Exception;
}
