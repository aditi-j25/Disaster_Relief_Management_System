package edu.ucalgary.oop;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/**
 * Parent interface for all database connector implementations 
 * in the Disaster Relief Management System.
 * <p>
 * This interface defines the contract for classes that manage database connections
 * and data loading operations for the application.
 * </p>
 * 
 * @author You
 * @version 1.0
 * @since 2025-04-09
 */
public interface DatabaseConnector {
    /**
     * Returns a valid database connection.
     *
     * @return A valid database connection object.
     * @throws SQLException if the connection cannot be established.
     */
    Connection getConnection() throws SQLException;
    
    /**
     * Safely closes the current database connection.
     */
    void closeConnection();
    
    /**
     * Returns a copy of all loaded {@link Person} objects indexed by ID.
     *
     * @return A map of person ID to {@code Person} objects.
     */
    Map<Integer, Person> getPersons();
    
    /**
     * Returns a copy of all loaded {@link Location} objects indexed by ID.
     *
     * @return A map of location ID to {@code Location} objects.
     */
    Map<Integer, Location> getLocations();
    
    /**
     * Returns a copy of all loaded {@link Supply} objects indexed by ID.
     *
     * @return A map of supply ID to {@code Supply} objects.
     */
    Map<Integer, Supply> getSupplies();
    
    /**
     * Returns a copy of all loaded {@link Inquiry} objects indexed by ID.
     *
     * @return A map of inquiry ID to {@code Inquiry} objects.
     */
    Map<Integer, Inquiry> getInquiries();
    
    /**
     * Returns a copy of all loaded {@link MedicalRecord} objects indexed by ID.
     *
     * @return A map of medical record ID to {@code MedicalRecord} objects.
     */
    Map<Integer, MedicalRecord> getMedicalRecords();
    
    /**
     * Returns a copy of all loaded {@link FamilyGroup} objects indexed by ID.
     *
     * @return A map of family group ID to {@code FamilyGroup} objects.
     */
    Map<Integer, FamilyGroup> getFamilyGroups();
    
    /**
     * Loads data from the data source into memory.
     */
    void loadData();
    
    /**
     * Calls the appropriate method to handle expired water supplies.
     */
    void cleanupExpiredWater();
    
    /**
     * Logs an error message and exception.
     *
     * @param message The custom error message to log.
     * @param e The exception that was thrown.
     */
    static void logError(String message, Exception e) {}; 
}