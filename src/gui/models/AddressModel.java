package gui.models;

import be.Address;
import bll.interfaces.IAddressManager;
import bll.managers.AddressManager;

public class AddressModel {
    private IAddressManager addressManager;

    public AddressModel() throws Exception {
        addressManager = new AddressManager();
    }

    public Address createAddress(Address address) throws Exception {
        return addressManager.createAddress(address);
    }

    public Address getAddressFromID(int addressID) throws Exception {
        return addressManager.getAddressFromID(addressID);
    }

    public Address updateAddress(Address address) throws Exception {
        return addressManager.updateAddress(address);
    }
}
