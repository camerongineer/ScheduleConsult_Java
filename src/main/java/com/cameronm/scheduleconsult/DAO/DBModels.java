//package com.cameronm.scheduleconsult.DAO;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public enum DBModels {
//    APPOINTMENTS(new ArrayList<>(List.of("")), "appointments", "Appointment_ID", "Title", "Create_Date", "Created_By", "Last_Update", "Last_Updated_By"),
//    CONTACTS("contacts"),
//    USERS("users"),
//    CUSTOMERS("customers"),
//    DIVISIONS("divisions"),
//    COUNTRIES("countries");
//
//    private final List<String> values;
//    private final String id;
//    private final String name;
//
//    DBModels(List<String> values,
//             String tableName,
//             String id,
//             String name,
//             String createDate,
//             String createdBy,
//             String lastUpdate,
//             String lastUpdatedBy,
//             String address, //Customers only
//             String postalCode, //Customers only
//             String phone, //Customer only
//             String contactId, //Foreign Key for Appointments
//             String countryId, //Foreign Key for Divisions
//             String customerId, //Foreign Key for Appointments
//             String divisionId, //Foreign Key for Customers
//             String userId //Foreign Key for Appointments
//    ) {
//        this.name = name;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public String toString() {
//        return name;
//    }
//
//}
package com.cameronm.scheduleconsult.DAO;

import java.util.Map;

/**
 * The DBModels Enum class represents the different database models used in the application. Each model has a
 * corresponding table name and a map of attributes and their column names in the table.
 *
 * @author Cameron M
 * @since 02-21-2023
 */
public enum DBModels {
    APPOINTMENTS("appointments", Map.ofEntries(
            Map.entry("id", "Appointment_ID"),
            Map.entry("name", "Title"),
            Map.entry("description", "Description"),
            Map.entry("location", "Location"),
            Map.entry("type", "Type"),
            Map.entry("start", "Start"),
            Map.entry("end", "End"),
            Map.entry("createDate", "Create_Date"),
            Map.entry("createdBy", "Created_By"),
            Map.entry("lastUpdate", "Last_Update"),
            Map.entry("lastUpdatedBy", "Last_Updated_By"),
            Map.entry("customerId", "Customer_ID"),
            Map.entry("userId", "User_ID"),
            Map.entry("contactId", "Contact_ID")
    )),
    CONTACTS("contacts", Map.ofEntries(
            Map.entry("id", "Contact_ID"),
            Map.entry("name", "Contact_Name"),
            Map.entry("email", "Email")
    )),
    USERS("users", Map.ofEntries(
            Map.entry("id", "User_ID"),
            Map.entry("name", "User_Name"),
            Map.entry("createDate", "Create_Date"),
            Map.entry("createdBy", "Created_By"),
            Map.entry("lastUpdate", "Last_Update"),
            Map.entry("lastUpdatedBy", "Last_Updated_By"),
            Map.entry("password", "Password")
    )),
    CUSTOMERS("customers", Map.ofEntries(
            Map.entry("id", "Customer_ID"),
            Map.entry("name", "Customer_Name"),
            Map.entry("createDate", "Create_Date"),
            Map.entry("createdBy", "Created_By"),
            Map.entry("lastUpdate", "Last_Update"),
            Map.entry("lastUpdatedBy", "Last_Updated_By"),
            Map.entry("address", "Address"),
            Map.entry("postalCode", "Postal_Code"),
            Map.entry("phone", "Phone"),
            Map.entry("divisionId", "Division_ID")
    )),
    DIVISIONS("first_level_divisions", Map.ofEntries(
            Map.entry("id", "Division_ID"),
            Map.entry("name", "Division"),
            Map.entry("createDate", "Create_Date"),
            Map.entry("createdBy", "Created_By"),
            Map.entry("lastUpdate", "Last_Update"),
            Map.entry("lastUpdatedBy", "Last_Updated_By"),
            Map.entry("countryId", "Country_ID")
    )),
    COUNTRIES("countries", Map.ofEntries(
            Map.entry("id", "Country_ID"),
            Map.entry("name", "Country"),
            Map.entry("createDate", "Create_Date"),
            Map.entry("createdBy", "Created_By"),
            Map.entry("lastUpdate", "Last_Update"),
            Map.entry("lastUpdatedBy", "Last_Updated_By")
    ));

    /**
     * The name of the table in the database
     */
    private final String tableName;

    /**
     * The different attributes in each table and the respective variable name in each class
     */
    private final Map<String, String> attributes;

    /**
     * The constructor for the enum class
     *
     * @param tableName The name of the table in the database
     * @param attributes The different attributes in each table and the respective variable name in each class
     */
    DBModels(String tableName, Map<String, String> attributes) {
        this.tableName = tableName;
        this.attributes = attributes;
    }

    /**
     * The getTableName method returns the table name
     *
     * @return Returns the name of the table
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * The getTableName method returns the table name and a map containing each attribute and variable name
     *
     * @return returns a map where the key is variable name for each model
     * and the value is the column name in the database
     */
    public Map<String, String> getAttributes() {
        return attributes;
    }
}
