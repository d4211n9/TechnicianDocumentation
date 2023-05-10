package dal.interfaces;

import be.Address;

public interface IAddressDAO {
    Address createAddress(Address address) throws Exception;
    Address getAddressFromID(int addressID) throws Exception;
    Address updateAddress(Address address) throws Exception;
}
