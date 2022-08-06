package dao;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDao {

    /** List for all objects representing customers. */
    public static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

    public static ObservableList<Customer> populateCustomerList(){
        String sql = "select * from customers";

        PreparedStatement ps;

        {
            try {
                ps = JDBC.getConnection().prepareStatement(sql);

                ResultSet rs = ps.executeQuery();
                allCustomers.clear();
                while (rs.next()) {
                    int customerId = rs.getInt("Customer_ID");
                    String customerName = rs.getString("Customer_Name");
                    String address = rs.getString("Address");
                    String postalCode = rs.getString("Postal_Code");
                    String phone = rs.getString("Phone");
                    int divisionId = rs.getInt("Division_ID");
                    Customer newCustomer = new Customer(customerId, customerName, address, postalCode, phone,
                            divisionId);
                    allCustomers.add(newCustomer);
                }
                return allCustomers;

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }
        return null;
    }

     public static void insert(String customerName, String address, String postalCode, String phone,
                               String createdBy, int divisionId) throws SQLException {

        String sql = "insert into customers (customer_name, address, postal_code, phone, create_date, created_by, " +
                "division_id) values(?, ?, ?, ?, NOW(), ?, ?)";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setString(1,customerName);
        ps.setString(2, address);
        ps.setString(3, postalCode);
        ps.setString(4, phone);
        ps.setString(5, createdBy);
        ps.setInt(6, divisionId);

        ps.execute();
    }

    public static void update(String customerName, String address, String postalCode, String phone, String updatedBy,
                              int divisionId, int customerId) throws SQLException {
        String sql = "update customers set customer_name = ?, address = ?, postal_code = ?, phone = ?, " +
                "last_update = NOW(), last_updated_by = ?, division_id = ? where customer_id = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setString(1,customerName);
        ps.setString(2, address);
        ps.setString(3, postalCode);
        ps.setString(4, phone);
        ps.setString(5, updatedBy);
        ps.setInt(6, divisionId);
        ps.setInt(7, customerId);

        ps.execute();
    }

    public static void delete(int customerId) throws SQLException {
        String sql = "delete from appointments where customer_id = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setInt(1, customerId);
        ps.execute();

        sql = "delete from customers where customer_id = ?";
        ps = JDBC.getConnection().prepareStatement(sql);
        ps.setInt(1, customerId);
        ps.execute();
    }
}
