package dal.interfaces;

import be.WireType;

import java.util.List;

public interface IWireTypeDAO {

    boolean createWireType(WireType wireType) throws Exception;
    List<WireType> getAllWireTypes() throws Exception;

}
