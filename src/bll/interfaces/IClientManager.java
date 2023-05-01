package bll.interfaces;

import be.Client;

import java.util.List;

public interface IClientManager {
    Client createClient(Client client) throws Exception;
    List<Client> getAllClients() throws Exception;
}
