package dal.interfaces;

import be.Address;

public interface IAddressDAO {
    Address createBillingAddress(Address address) throws Exception;
    Address getBillingAddressFromID(int addressID) throws Exception;
    Address updateBillingAddress(Address address) throws Exception;
    Address createProjectAddress(Address address) throws Exception;
    Address getProjectAddressFromID(int addressID) throws Exception;
    Address updateProjectAddress(Address address) throws Exception;
}
