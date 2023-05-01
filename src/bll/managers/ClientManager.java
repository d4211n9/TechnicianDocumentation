package bll.managers;

import be.Client;
import bll.interfaces.IClientManager;
import dal.dao.ClientDAO;
import dal.interfaces.IClientDAO;

import java.util.List;

public class ClientManager implements IClientManager {
    private IClientDAO clientDAO;

    public ClientManager() throws Exception {
        clientDAO = new ClientDAO();
    }

    @Override
    public Client createClient(Client client) throws Exception {
        return clientDAO.createClient(client);
    }

    @Override
    public List<Client> getAllClients() throws Exception {
        return clientDAO.getAllClients();
    }
}
