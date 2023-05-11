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
    public Address createAddress(Address address) throws Exception {
        Address existingAddress = existingAddress(address);
        if(existingAddress != null) {
            return existingAddress;
        }
        return addressDAO.createAddress(address);
    }

    @Override
    public Address getAddressFromID(int addressID) throws Exception {
        return addressDAO.getAddressFromID(addressID);
    }

    @Override
    public Address updateAddress(Address address) throws Exception {
        return addressDAO.updateAddress(address);
    }

    @Override
    public Address existingAddress(Address address) throws Exception {
        return addressDAO.existingAddress(address);
    }
}
