package com.gmail.dymitr.kuzmin;

import java.sql.SQLException;

/**
 * Java Data Base Controller,serves
 * for manage data base with scrapped information.
 *
 * @author dimonium_239
 */

public interface JDBC {

    /**
     * Connect to existed data base.
     *
     * @throws  SQLException
     *          If function throws this exception that mean that function does not exist.
     * @param DBName
     *          String value that contain name of database with this name.
     */
    void connect(String DBName) throws SQLException;

    /**
     * Add element to data base
     * @param element
     *          Object that must be saved in data base.
     */
    void addToDB(DBElement element) throws SQLException;

    /**
     * Remove element from data base by key,
     * where key is URL of element/person in
     * internet shop/social network
     * @param URL
     *          URL acts as unique key that give access to delete element under that key.
     */
    void removeFromDB(String URL) throws SQLException;

    /**
     * Run by all links in data base, check is links are actual,
     * and if it is necessary update information in data base
     */
    void refreshDB();

    /**
     * Get and return element from data base.
     * @param URL
     *          URL of element that must be gated.
     * @return Element from data base
     */
    DBElement getFromDB(String URL) throws SQLException;

    /**
     * Disconnect from data base.
     */
    void disconnect() throws SQLException;

    /**
     * Create new data base if it did not exist
     */
    void createDB() throws SQLException;

    /**
     * Create new table in new schema
     * Table must be created with at lest one column.
     * For Scraper best solution of new column is text column with not null and values
     *  that will contain URL of chosen elements.
     */
    void createTable() throws SQLException;

    /**
     * Create new column in existed data base.
     * @param collName
     *          String name of new column
     * @param collType
     *          String name of data type of new column (text, integer, float etc.)
     */
    void createColumn(String collName, String collType) throws SQLException;
}
