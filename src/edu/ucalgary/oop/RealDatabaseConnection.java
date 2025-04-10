package edu.ucalgary.oop;

import java.sql.SQLException;

/**
 * Interface for database connections that interact with a real database.
 * <p>
 * Extends the base DatabaseConnector interface with operations specific
 * to real database connections.
 * </p>
 * 
 * @author 30208786
 * @version 5.0
 * @since 2025-04-09
 */
public interface RealDatabaseConnection extends DatabaseConnector {
    /**
     * Executes a custom SQL query and returns the result.
     *
     * @param sqlQuery The SQL query to execute.
     * @return A string representation of the query result.
     * @throws SQLException if the query execution fails.
     */
    String executeCustomQuery(String sqlQuery) throws SQLException;
    
    /**
     * Updates a record in the database.
     *
     * @param tableName The name of the table to update.
     * @param id The ID of the record to update.
     * @param columnName The name of the column to update.
     * @param newValue The new value to set.
     * @return True if the update was successful, false otherwise.
     * @throws SQLException if the update operation fails.
     */
    boolean updateRecord(String tableName, int id, String columnName, Object newValue) throws SQLException;
    
    /**
     * Loads and assigns relationships between entities.
     *
     * @throws SQLException if any SQL operation fails during association mapping.
     */
    void loadAssociations() throws SQLException;
}