package com.cameronm.scheduleconsult.services;

import com.cameronm.scheduleconsult.DAO.DBModels;
import com.cameronm.scheduleconsult.DAO.DBQueries;
import com.cameronm.scheduleconsult.models.User;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.HashMap;

/**
 * The UserQueryService class is responsible for converting entries
 * in the users table of the database into User instances
 *
 * @author Cameron M
 * @since 02-22-2023
 */
public abstract class UserQueryService extends QueryService {

    /**
     * The getAllUsers method returns an ObservableList of all users in the database
     *
     * @return Returns an ObservableList of all users
     */
    public static ObservableList<User> getAllUsers() {
        String sqlQuery = DBQueries.SELECT_ALL;
        return getUsers(sqlQuery);
    }

    /**
     * The getUsers method returns an ObservableList of specific users in the database
     *
     * @param sqlQuery The query specifying the users to return
     * @return Returns an ObservableList of users specified by the query
     */
    public static ObservableList<User> getUsers(String sqlQuery) {
        return QueryService.getEntities(DBModels.USERS, sqlQuery);
    }

    /**
     * The getUser method takes a results set and previously retrieved common attributes and
     * retrieves all the rest of the remaining user attributes and returns the user
     *
     * @param results The results set passed in from an entry in the users table
     * @param entityAttributes Common attribute types that are shared by all audited entities
     * @return Returns a user
     * @throws SQLException Throws an SQLException of the ResultSet is invalid
     */
    static User getUser(ResultSet results, HashMap<String, String> entityAttributes) throws SQLException {
        entityAttributes.put("password", results.getString(DBModels.USERS.getAttributes().get("password")));
        String password = entityAttributes.get("password");
        return new User(
                Integer.parseInt(entityAttributes.get("id")),
                entityAttributes.get("name"),
                Timestamp.valueOf(entityAttributes.get("createDate")),
                entityAttributes.get("createdBy"),
                Timestamp.valueOf(entityAttributes.get("lastUpdate")),
                entityAttributes.get("lastUpdatedBy"),
                password
        );
    }
}
