package com.cameronm.scheduleconsult.services;

import com.cameronm.scheduleconsult.DAO.DBConnection;
import com.cameronm.scheduleconsult.DAO.DBModels;
import com.cameronm.scheduleconsult.DAO.DBQueries;
import com.cameronm.scheduleconsult.models.Customer;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * The CustomerQueryService class is responsible for converting entries in the customers table of the database into
 * Customer instances
 *
 * @author Cameron M
 * @since 02-22-2023
 */
public abstract class CustomerQueryService extends QueryService {

    public static final String MODIFY_CUSTOMERS_QUERY = updateValuesQueriesSelector(DBModels.CUSTOMERS);
    public static final String ADD_CUSTOMERS_QUERY = insertValuesQueriesSelector(DBModels.CUSTOMERS);
    public static final Map<String, String> ATTRIBUTES = DBModels.CUSTOMERS.getAttributes();

    /**
     * The getAllCustomers method returns an ObservableList of all customers in the database
     *
     * @return Returns an ObservableList of all customers
     */
    public static ObservableList<Customer> getAllCustomers() {
        return getCustomers(DBQueries.SELECT_ALL);
    }

    /**
     * The getCustomers method returns an ObservableList of specific customers in the database
     *
     * @param sqlQuery The query specifying the customers to return
     * @return Returns an ObservableList of customers specified by the query
     */
    public static ObservableList<Customer> getCustomers(String sqlQuery) {
        return QueryService.getEntities(DBModels.CUSTOMERS, sqlQuery);
    }

    /**
     * The getCustomer method takes a results set and previously retrieved common attributes and retrieves all the rest
     * of the remaining customer attributes and returns the customer
     *
     * @param results          The results set passed in from an entry in the customers table
     * @param entityAttributes Common attribute types that are shared by all audited entities
     * @return Returns a customer
     * @throws SQLException Throws an SQLException of the ResultSet is invalid
     */
    static Customer getCustomer(ResultSet results, HashMap<String, String> entityAttributes) throws SQLException {
        entityAttributes.put("address",
                             results.getString(DBModels.CUSTOMERS.getAttributes()
                                                                 .get("address")));
        entityAttributes.put("postalCode",
                             results.getString(DBModels.CUSTOMERS.getAttributes()
                                                                 .get("postalCode")));
        entityAttributes.put("phone",
                             results.getString(DBModels.CUSTOMERS.getAttributes()
                                                                 .get("phone")));
        entityAttributes.put("divisionId",
                             results.getString(DBModels.CUSTOMERS.getAttributes()
                                                                 .get("divisionId")));
        String address = entityAttributes.get("address");
        String postalCode = entityAttributes.get("postalCode");
        String phone = entityAttributes.get("phone");
        int divisionId = Integer.parseInt(entityAttributes.get("divisionId"));
        return new Customer(
                Integer.parseInt(entityAttributes.get("id")),
                entityAttributes.get("name"),
                TimeConversionService.convertFromServerTime(Timestamp.valueOf(entityAttributes.get("createDate"))),
                entityAttributes.get("createdBy"),
                TimeConversionService.convertFromServerTime(Timestamp.valueOf(entityAttributes.get("lastUpdate"))),
                entityAttributes.get("lastUpdatedBy"),
                address,
                postalCode,
                phone,
                divisionId
        );
    }

    /**
     * The getCustomerById method returns a customer specified by the ID
     *
     * @param customerId The ID of the customer
     * @return returns a customer entity
     */
    public static Customer getCustomerById(int customerId) {
        return QueryService.getEntityById(DBModels.CUSTOMERS, customerId);
    }

    /**
     * The deleteCustomer method deletes a customer from the customers table in the database
     *
     * @param customerToBeDeleted The customer being deleted
     * @return Returns a boolean values specifying if the customer was deleted successfully
     */
    public static boolean deleteCustomer(Customer customerToBeDeleted) {
        if (!QueryService.queryHasMatches(
                String.format(SELECT_ALL, DBModels.APPOINTMENTS.getTableName()) +
                        WHERE +
                        String.format(EQUALS_INTEGER,
                                      DBModels.APPOINTMENTS.getAttributes()
                                                           .get("customerId"),
                                      customerToBeDeleted.getId())
        )) {
            return QueryService.deleteEntityById(DBModels.CUSTOMERS, customerToBeDeleted.getId());
        }
        return false;
    }

    /**
     * The addCustomer method adds a customer to the customers table in the database
     *
     * @param customer The customer being added to the database
     * @return Returns the added customer
     */
    public static Customer addCustomer(Customer customer) {
        try (PreparedStatement statement = DBConnection.getConnection()
                                                       .prepareStatement(ADD_CUSTOMERS_QUERY,
                                                                         Statement.RETURN_GENERATED_KEYS)) {
            setCustomerStatement(statement, customer, true);
            statement.execute();
            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    return getCustomerById(id);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * The modifyCustomer method modifies an existing customer in the customers table of the database
     *
     * @param customer The customer being modified in the database
     */
    public static void modifyCustomer(Customer customer) {
        try (PreparedStatement statement = DBConnection.getConnection()
                                                       .prepareStatement(
                                                               String.format(MODIFY_CUSTOMERS_QUERY,
                                                                             ATTRIBUTES.get("id"),
                                                                             customer.getId()))) {
            setCustomerStatement(statement, customer, false);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The setCustomerStatement method sets the PreparedStatement with the values of the customer
     *
     * @param statement    The statement being set
     * @param customer     The customer whose values are being set
     * @param setCreatedBy Boolean specifying if the created_by field is being set
     * @throws SQLException Throws SQLException in the event of an error
     */
    private static void setCustomerStatement(PreparedStatement statement,
                                             Customer customer,
                                             boolean setCreatedBy) throws SQLException {
        statement.setString(1, customer.getAddress());
        statement.setInt(2, customer.getDivisionId());
        statement.setString(3, customer.getName());
        statement.setString(4, customer.getPhone());
        statement.setString(5, customer.getPostalCode());
        if (setCreatedBy) {
            statement.setString(6, customer.getCreatedBy());
        }
        statement.setString(setCreatedBy ? 7 : 6, customer.getLastUpdatedBy());
    }
}
