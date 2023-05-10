package dal.dao;

import be.Address;
import dal.connectors.AbstractConnector;
import dal.connectors.SqlConnector;
import dal.interfaces.IAddressDAO;
import exceptions.DALException;

import java.sql.*;

public class AddressDAO implements IAddressDAO {
    private AbstractConnector connector;
    public AddressDAO() throws Exception {
        connector = new SqlConnector();
    }

    @Override
    public Address createBillingAddress(Address address) throws Exception {
        Address newAddress = null;
        String sql = "INSERT INTO BillingAddress (Street, PostalCode, City) VALUES (?, ?, ?);";

        try (Connection conn = connector.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, address.getStreet());
            statement.setString(2, address.getPostalCode());
            statement.setString(3, address.getCity());

            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();

            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                newAddress = new Address(id, address.getStreet(), address.getPostalCode(), address.getCity());
            }
        }
        catch (Exception e) {
            throw new Exception("Failed to create billing address", e);
        }
        return newAddress;
    }

    @Override
    public Address getBillingAddressFromID(int addressID) throws Exception {
        Address address = null;
        String sql = "SELECT * FROM BillingAddress WHERE ID = ?;";

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, addressID);

            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                //Mapping the address
                int ID = resultSet.getInt(1);
                String street = resultSet.getString(2);
                String postalCode = resultSet.getString(3);
                String city = resultSet.getString(4);

                address = new Address(ID, street, postalCode, city);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DALException("Failed to read billing address", e);
        }

        return address;
    }

    @Override
    public Address updateBillingAddress(Address address) throws Exception {
        Address updatedAddress = null;
        String sql = "UPDATE BillingAddress SET Street=?, PostalCode=?, City=? WHERE ID=?;";
        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, address.getStreet());
            statement.setString(2, address.getPostalCode());
            statement.setString(3, address.getCity());
            statement.setInt(4, address.getID());

            statement.executeUpdate();

            updatedAddress = address;
        } catch (SQLException e) {
            throw new Exception("Failed to update billing address", e);
        }
        return updatedAddress;
    }

    @Override
    public Address createProjectAddress(Address address) throws Exception {
        Address newAddress = null;
        String sql = "INSERT INTO ProjectAddress (Street, PostalCode, City) VALUES (?, ?, ?);";

        try (Connection conn = connector.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, address.getStreet());
            statement.setString(2, address.getPostalCode());
            statement.setString(3, address.getCity());

            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();

            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                newAddress = new Address(id, address.getStreet(), address.getPostalCode(), address.getCity());
            }
        }
        catch (Exception e) {
            throw new Exception("Failed to create project address", e);
        }
        return newAddress;
    }

    @Override
    public Address getProjectAddressFromID(int addressID) throws Exception {
        Address address = null;
        String sql = "SELECT * FROM ProjectAddress WHERE ID = ?;";

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, addressID);

            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                //Mapping the address
                int ID = resultSet.getInt(1);
                String street = resultSet.getString(2);
                String postalCode = resultSet.getString(3);
                String city = resultSet.getString(4);

                address = new Address(ID, street, postalCode, city);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DALException("Failed to read project address", e);
        }

        return address;
    }

    @Override
    public Address updateProjectAddress(Address address) throws Exception {
        Address updatedAddress = null;
        String sql = "UPDATE ProjectAddress SET Street=?, PostalCode=?, City=? WHERE ID=?;";
        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, address.getStreet());
            statement.setString(2, address.getPostalCode());
            statement.setString(3, address.getCity());
            statement.setInt(4, address.getID());

            statement.executeUpdate();

            updatedAddress = address;
        } catch (SQLException e) {
            throw new Exception("Failed to update project address", e);
        }
        return updatedAddress;
    }
}
