package gui.models;

import be.Client;
import be.Project;
import be.SystemUser;
import bll.interfaces.IClientManager;
import bll.managers.ClientManager;
import exceptions.GUIException;
import gui.util.TaskExecutor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.sql.Timestamp;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ClientModel implements Runnable {
    private IClientManager clientManager;
    private List<Client> allClients;
    private List<Client> copyAllClients;
    private ObservableList<Client> filteredClients;
    private String searchString;

    private Timestamp lastUpdatedTime;

    public ClientModel() throws Exception {
        clientManager = new ClientManager();

        allClients = retrieveAllClients();
        copyAllClients = new ArrayList<>(allClients);
        filteredClients = FXCollections.observableList(copyAllClients);

        lastUpdatedTime = new Timestamp(System.currentTimeMillis());

    }

    public Task<Client> createClient(Client client) {
        Task<Client> createClientTask = new Task<>() {
            @Override
            protected Client call() throws Exception {
                Client finalClient = clientManager.createClient(client);

                if (finalClient != null) {
                    allClients.add(finalClient);
                    search(searchString);
                }

                updateValue(finalClient);

                return finalClient;
            }
        };

        return createClientTask;
    }

    public List<Client> retrieveAllClients() throws Exception {
        copyAllClients = new ArrayList<>(clientManager.getAllClients());
        return copyAllClients;
    }

    public ObservableList<Client> getAllClients() {
        return filteredClients;
    }

    public void search(String query) throws Exception {
        filteredClients.clear();
        if (query != null) {
            searchString = query;
            filteredClients.addAll(clientManager.search(allClients, query));
        } else {
            filteredClients.addAll(clientManager.search(allClients, ""));
        }
    }

    public Task<Boolean> updateClient(Client client, Client originalClient) {
        Task<Boolean> updateClientTask = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                boolean successfullyUpdatedClient = clientManager.updateClient(client) != null;

                if (successfullyUpdatedClient) {
                    allClients.remove(originalClient);
                    allClients.add(client);
                    search(searchString);
                }

                updateValue(successfullyUpdatedClient);
                return successfullyUpdatedClient;
            }
        };
        return updateClientTask;
    }

    @Override
    public void run() {
        System.out.println("client update");

        List<Client> updatedClients;
        try {
            updatedClients = clientManager.getAllModifiedClients(lastUpdatedTime);
            lastUpdatedTime.setTime(System.currentTimeMillis());

            if(updatedClients.size() > 0){
                allClients = retrieveAllClients();
                search(searchString);
            }
            Thread.sleep(300);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteClient(Client deletedClient) throws Exception {
        clientManager.deleteClient(deletedClient);
    }
}