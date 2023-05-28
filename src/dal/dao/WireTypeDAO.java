package dal.dao;

import be.WireType;
import dal.connectors.AbstractConnector;
import dal.connectors.SqlConnector;
import dal.interfaces.IWireTypeDAO;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class WireTypeDAO implements IWireTypeDAO {

    private AbstractConnector connector;

    public WireTypeDAO() throws Exception {
        connector = new SqlConnector();
    }

    public WireTypeDAO(AbstractConnector connector) {
        this.connector = connector;
    }

    @Override
    public List<WireType> getAllWireTypes() throws Exception {

        WireType wireType = new WireType("hdmi", Color.color(0.5,0.5,0.5));
        WireType wireType1 = new WireType("power", Color.color(0,1.0,0.1));
        WireType wireType2 = new WireType("signal", Color.color(1.0,0.0,0.5));
        List l = new ArrayList<>();
        l.add(wireType);
        l.add(wireType1);
        l.add(wireType2);
        return l; //todo get all method from wireType... look at devicetypeDAO
    }

    @Override
    public boolean createWireType(WireType wireType) throws Exception {
        System.out.println("DAO class got: " + wireType.getName() + "  in this Color:  " + wireType.getColor().toString());
        return false; //todo make create methode look at device type..
    }
}
