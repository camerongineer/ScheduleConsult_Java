package com.cameronm.scheduleconsult.services;

import com.cameronm.scheduleconsult.DAO.DBModels;
import com.cameronm.scheduleconsult.DAO.DBQueries;
import com.cameronm.scheduleconsult.models.Appointment;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;

/**
 * The AppointmentQueryService class is responsible for converting entries
 * in the appointments table of the database into Appointment instances
 *
 * @author Cameron M
 * @since 02-22-2023
 */
public abstract class AppointmentQueryService extends QueryService {

    /**
     * The getAllAppointments method returns an ObservableList of all appointments in the database
     *
     * @return Returns an ObservableList of all appointments
     */
    public static ObservableList<Appointment> getAllAppointments() {
        String sqlQuery = DBQueries.SELECT_ALL;
        return getAppointments(sqlQuery);
    }

    /**
     * The getAppointments method returns an ObservableList of specific appointments in the database
     *
     * @param sqlQuery The query specifying the appointments to return
     * @return Returns an ObservableList of appointments specified by the query
     */
    public static ObservableList<Appointment> getAppointments(String sqlQuery) {
        return QueryService.getEntities(DBModels.APPOINTMENTS, sqlQuery);
    }

    /**
     * The getAppointment method takes a results set and previously retrieved common attributes and
     * retrieves all the rest of the remaining appointment attributes and returns the appointment
     *
     * @param results The results set passed in from an entry in the appointments table
     * @param entityAttributes Common attribute types that are shared by all audited entities
     * @return Returns an appointment
     * @throws SQLException Throws an SQLException of the ResultSet is invalid
     */
    static Appointment getAppointment(ResultSet results, HashMap<String, String> entityAttributes) throws SQLException {
        entityAttributes.put("description", results.getString(DBModels.APPOINTMENTS.getAttributes().get("description")));
        entityAttributes.put("location", results.getString(DBModels.APPOINTMENTS.getAttributes().get("location")));
        entityAttributes.put("type", results.getString(DBModels.APPOINTMENTS.getAttributes().get("type")));
        entityAttributes.put("start", results.getString(DBModels.APPOINTMENTS.getAttributes().get("start")));
        entityAttributes.put("end", results.getString(DBModels.APPOINTMENTS.getAttributes().get("end")));
        entityAttributes.put("customerId", results.getString(DBModels.APPOINTMENTS.getAttributes().get("customerId")));
        entityAttributes.put("userId", results.getString(DBModels.APPOINTMENTS.getAttributes().get("userId")));
        entityAttributes.put("contactId", results.getString(DBModels.APPOINTMENTS.getAttributes().get("contactId")));
        String description = entityAttributes.get("description");
        String location = entityAttributes.get("location");
        String type = entityAttributes.get("type");
        Timestamp start = Timestamp.valueOf(entityAttributes.get("start"));
        Timestamp end = Timestamp.valueOf(entityAttributes.get("end"));
        int customerId = Integer.parseInt(entityAttributes.get("customerId"));
        int userId = Integer.parseInt(entityAttributes.get("userId"));
        int contactId = Integer.parseInt(entityAttributes.get("contactId"));
        return new Appointment(
                Integer.parseInt(entityAttributes.get("id")),
                entityAttributes.get("name"),
                Timestamp.valueOf(entityAttributes.get("createDate")),
                entityAttributes.get("createdBy"),
                Timestamp.valueOf(entityAttributes.get("lastUpdate")),
                entityAttributes.get("lastUpdatedBy"),
                description,
                location,
                type,
                start,
                end,
                customerId,
                userId,
                contactId
        );
    }
}
