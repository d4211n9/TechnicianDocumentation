package gui.models;

import be.Address;
import bll.interfaces.IAddressManager;
import bll.managers.AddressManager;

public class AddressModel {
    private IAddressManager addressManager;

    public AddressModel() throws Exception {
        addressManager = new AddressManager();
    }

    public Address createBillingAddress(Address address) throws Exception {
        return addressManager.createBillingAddress(address);
    }

    public Address getBillingAddressFromID(int addressID) throws Exception {
        return addressManager.getBillingAddressFromID(addressID);
    }

    public Address updateBillingAddress(Address address) throws Exception {
        return addressManager.updateBillingAddress(address);
    }

    public Address createProjectAddress(Address address) throws Exception {
        return addressManager.createProjectAddress(address);
    }

    public Address getProjectAddressFromID(int addressID) throws Exception {
        return addressManager.getProjectAddressFromID(addressID);
    }

    public Address updateProjectAddress(Address address) throws Exception {
        return addressManager.updateProjectAddress(address);
    }
}
