package com.cameronm.scheduleconsult.services;

import com.cameronm.scheduleconsult.DAO.DBModels;
import com.cameronm.scheduleconsult.DAO.DBQueries;
import com.cameronm.scheduleconsult.models.Contact;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * The ContactQueryService class is responsible for converting entries
 * in the contacts table of the database into Contact instances
 *
 * @author Cameron M
 * @since 02-22-2023
 */
public abstract class ContactQueryService extends QueryService {

    /**
     * The getAllContacts method returns an ObservableList of all contacts in the database
     *
     * @return Returns an ObservableList of all contacts
     */
    public static ObservableList<Contact> getAllContacts() {
        String sqlQuery = DBQueries.SELECT_ALL;
        return getContacts(sqlQuery);
    }

    /**
     * The getContacts method returns an ObservableList of specific contacts in the database
     *
     * @param sqlQuery The query specifying the contacts to return
     * @return Returns an ObservableList of contacts specified by the query
     */
    public static ObservableList<Contact> getContacts(String sqlQuery) {
        return QueryService.getEntities(DBModels.CONTACTS, sqlQuery);
    }

    /**
     * The getContact method takes a results set and previously retrieved common attributes and
     * retrieves all the rest of the remaining contact attributes and returns the contact
     *
     * @param results The results set passed in from an entry in the contacts table
     * @param entityAttributes Common attribute types that are shared by all named entities
     * @return Returns a contact
     * @throws SQLException Throws an SQLException of the ResultSet is invalid
     */
    static Contact getContact(ResultSet results, HashMap<String, String> entityAttributes) throws SQLException {
        entityAttributes.put("email", results.getString(DBModels.CONTACTS.getAttributes().get("email")));
        String email = entityAttributes.get("email");
        return new Contact(
                Integer.parseInt(entityAttributes.get("id")),
                entityAttributes.get("name"),
                email
        );
    }

    /**
     * The retrieveMatchFromDatabase method retrieves a matching entry from a contact if
     * the appointment's contact ID matches the contact's primary ID
     *
     * @param contactId The primary ID of the contact that must match contact ID foreign key of the appointment
     * @param requestedColumn The contact column type requested by the appointment
     *
     * @return Returns the requested match
     */
    public static String retrieveMatchFromDatabase(int contactId, String requestedColumn) {
        return retrieveMatchFromDatabase(DBModels.CONTACTS, DBModels.APPOINTMENTS, contactId, requestedColumn);
    }
}
