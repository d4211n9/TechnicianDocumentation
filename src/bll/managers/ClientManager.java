package bll.managers;

import be.Client;
import bll.interfaces.IClientManager;
import bll.util.Search;
import dal.dao.ClientDAO;
import dal.interfaces.IClientDAO;

import java.sql.Timestamp;
import java.util.List;

public class ClientManager implements IClientManager {
    private IClientDAO clientDAO;
    private Search search;

    public ClientManager() throws Exception {
        clientDAO = new ClientDAO();
        search = new Search();
    }

    @Override
    public Client createClient(Client client) throws Exception {
        return clientDAO.createClient(client);
    }

    @Override
    public List<Client> getAllClients() throws Exception {
        return clientDAO.getAllClients();
    }

    @Override
    public List<Client> search(List<Client> allClients, String query) throws Exception {
        return search.searchForString(allClients, query);
    }

    @Override
    public Client updateClient(Client client) throws Exception {
        return clientDAO.updateClient(client);
    }

    @Override
    public List<Client> getAllModifiedClients(Timestamp lastCheck) throws Exception {
        return clientDAO.getAllModifiedClients(lastCheck);
    }
}
