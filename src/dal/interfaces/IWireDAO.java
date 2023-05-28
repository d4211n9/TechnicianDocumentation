package dal.interfaces;

import be.Wire;
import exceptions.DALException;

public interface IWireDAO {


    Wire createWire(Wire wire) throws DALException;
}
