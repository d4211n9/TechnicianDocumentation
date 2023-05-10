package gui.models;

import be.Client;
import bll.interfaces.IClientManager;
import bll.managers.ClientManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class ClientModel {
    private IClientManager clientManager;
    private List<Client> allClients;
    private ObservableList<Client> filteredClients;
    private String searchString;

    public ClientModel() throws Exception {
        clientManager = new ClientManager();

        List<Client> copyAllClients = new ArrayList<>();
        allClients = retrieveAllClients();
        allClients.forEach(client -> copyAllClients.add(client));
        filteredClients = FXCollections.observableList(copyAllClients);
    }

    public Client createClient(Client client) throws Exception {
        Client finalClient = clientManager.createClient(client);
        if(finalClient != null){
            allClients.add(finalClient);
            search(searchString);
        }
        return finalClient;
    }

    public List<Client> retrieveAllClients() throws Exception {
        return clientManager.getAllClients();
    }

    public ObservableList<Client> getAllClients() {
        return filteredClients;
    }

    public void search(String query) throws Exception {
        filteredClients.clear();
        if(query != null) {
            searchString = query;
            filteredClients.addAll(clientManager.search(allClients, query));
            } else {
                filteredClients.addAll(clientManager.search(allClients, ""));
        }
    }

    public boolean updateClient(Client client, Client originalClient) throws Exception{
        if(clientManager.updateClient(client) != null){
            allClients.remove(originalClient);
            allClients.add(client);
            search(searchString);
            return true;
        }
        return false;
    }
}
