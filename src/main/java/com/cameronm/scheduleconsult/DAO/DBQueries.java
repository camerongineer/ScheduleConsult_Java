package com.cameronm.scheduleconsult.DAO;

/**
 * The DBQueries interface contains string variables of common sql commands used within the program
 *
 * @author Cameron M
 * @since 02-26-2023
 */
public interface DBQueries {
    String SELECT_ALL = " SELECT * FROM %s ";
    String INSERT = " INSERT INTO %s (%s) ";
    String UPDATE = " UPDATE %s ";
    String DELETE = "DELETE FROM %s ";
    String SET = " SET %s ";
    String VALUES = " VALUES(%s) ";
    String AND = " AND ";
    String WHERE = " WHERE ";
    String JOIN = " JOIN %s ON %s = %s ";
    String EQUALS_STRING = " %s = '%s' ";
    String EQUALS_INTEGER = " %s = %d ";
    String TIME_RANGE = " %s >= '%s' AND %s <= '%s' ";
    String NOT_IN_TIME_RANGE = " %s < '%s' AND %s > '%s' ";
    String NOT_EQUAL_INTEGER = " %s != %s ";
}
