package dal.interfaces;

import be.Wire;
import exceptions.DALException;

import java.util.List;

public interface IWiresOnInstallationDAO {
    boolean addWireToInstallation(Wire wire, int id) throws DALException;

    boolean removeWiresFromInstallation(int id) throws DALException;

    List<Wire> getWiresFromInstallation(int id) throws DALException;
}
