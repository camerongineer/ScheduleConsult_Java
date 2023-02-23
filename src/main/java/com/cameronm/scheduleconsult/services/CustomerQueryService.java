package com.cameronm.scheduleconsult.services;

import com.cameronm.scheduleconsult.DAO.DBModels;
import com.cameronm.scheduleconsult.DAO.DBQueries;
import com.cameronm.scheduleconsult.models.Customer;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;

/**
 * The CustomerQueryService class is responsible for converting entries
 * in the customers table of the database into Customer instances
 *
 * @author Cameron M
 * @since 02-22-2023
 */
public abstract class CustomerQueryService extends QueryService {

    /**
     * The getAllCustomers method returns an ObservableList of all customers in the database
     *
     * @return Returns an ObservableList of all customers
     */
    public static ObservableList<Customer> getAllCustomers() {
        String sqlQuery = DBQueries.SELECT_ALL;
        return getCustomers(sqlQuery);
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
     * The getCustomer method takes a results set and previously retrieved common attributes and
     * retrieves all the rest of the remaining customer attributes and returns the customer
     *
     * @param results The results set passed in from an entry in the customers table
     * @param entityAttributes Common attribute types that are shared by all audited entities
     * @return Returns a customer
     * @throws SQLException Throws an SQLException of the ResultSet is invalid
     */
    static Customer getCustomer(ResultSet results, HashMap<String, String> entityAttributes) throws SQLException {
        entityAttributes.put("address", results.getString(DBModels.CUSTOMERS.getAttributes().get("address")));
        entityAttributes.put("postalCode", results.getString(DBModels.CUSTOMERS.getAttributes().get("postalCode")));
        entityAttributes.put("phone", results.getString(DBModels.CUSTOMERS.getAttributes().get("phone")));
        entityAttributes.put("divisionId", results.getString(DBModels.CUSTOMERS.getAttributes().get("divisionId")));
        String address = entityAttributes.get("address");
        String postalCode = entityAttributes.get("postalCode");
        String phone = entityAttributes.get("phone");
        int divisionId = Integer.parseInt(entityAttributes.get("divisionId"));
        return new Customer(
                Integer.parseInt(entityAttributes.get("id")),
                entityAttributes.get("name"),
                Timestamp.valueOf(entityAttributes.get("createDate")),
                entityAttributes.get("createdBy"),
                Timestamp.valueOf(entityAttributes.get("lastUpdate")),
                entityAttributes.get("lastUpdatedBy"),
                address,
                postalCode,
                phone,
                divisionId
        );
    }
}
