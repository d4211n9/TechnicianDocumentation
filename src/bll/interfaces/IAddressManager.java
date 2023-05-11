package bll.interfaces;

import be.Address;

public interface IAddressManager {
    Address createAddress(Address address) throws Exception;
    Address getAddressFromID(int addressID) throws Exception;
    Address updateAddress(Address address) throws Exception;
    Address existingAddress(Address address) throws Exception;
}
