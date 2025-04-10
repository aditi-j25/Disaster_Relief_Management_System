package edu.ucalgary.oop;

/**
 * Interface for mock database connections used in testing.
 * <p>
 * Extends the base DatabaseConnector interface with operations specific
 * to mock database implementations.
 * </p>
 * 
 * @author You
 * @version 1.0
 * @since 2025-04-09
 */
public interface MockDatabaseConnection extends DatabaseConnector {
    /**
     * Resets the mock database to its initial state.
     */
    void reset();
    
    /**
     * Populates the mock database with test data.
     */
    void populateTestData();
    
    /**
     * Simulates a database failure scenario.
     *
     * @param failureType The type of failure to simulate.
     */
    void simulateFailure(String failureType);
    
    /**
     * Validates that the in-memory data structures are consistent.
     *
     * @return True if the data is consistent, false otherwise.
     */
    boolean validateDataConsistency();
}