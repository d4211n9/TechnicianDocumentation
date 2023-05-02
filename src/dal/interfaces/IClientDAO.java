package dal.interfaces;

import be.Client;

import java.util.List;

public interface IClientDAO {
    Client createClient(Client client) throws Exception;
    List<Client> getAllClients() throws Exception;

    Client updateClient(Client client) throws Exception;
}
