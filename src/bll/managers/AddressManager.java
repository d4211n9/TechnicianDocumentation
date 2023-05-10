package bll.managers;

import be.Address;
import bll.interfaces.IAddressManager;
import dal.dao.AddressDAO;
import dal.interfaces.IAddressDAO;

public class AddressManager implements IAddressManager {
    private IAddressDAO addressDAO;

    public AddressManager() throws Exception {
        addressDAO = new AddressDAO();
    }

    @Override
    public Address createBillingAddress(Address address) throws Exception {
        return addressDAO.createBillingAddress(address);
    }

    @Override
    public Address getBillingAddressFromID(int addressID) throws Exception {
        return addressDAO.getBillingAddressFromID(addressID);
    }

    @Override
    public Address updateBillingAddress(Address address) throws Exception {
        return addressDAO.updateBillingAddress(address);
    }

    @Override
    public Address createProjectAddress(Address address) throws Exception {
        return addressDAO.createProjectAddress(address);
    }

    @Override
    public Address getProjectAddressFromID(int addressID) throws Exception {
        return addressDAO.getProjectAddressFromID(addressID);
    }

    @Override
    public Address updateProjectAddress(Address address) throws Exception {
        return addressDAO.updateProjectAddress(address);
    }
}
