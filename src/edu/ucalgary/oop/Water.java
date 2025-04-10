package edu.ucalgary.oop;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a water supply in the disaster relief management system.
 * This class extends {@link Supply} and includes functionality specific to water allocation,
 * including expiry checks, and allocation to persons and locations.
 * 
 * Water has a unique allocation expiry rule: it expires one day after allocation when allocated to a person.
 * It does not expire when allocated to a location.
 * 
 * @author 30208786
 * @version 2.0
 * @since 2025-04-05
 * @see Supply
 */
public class Water extends Supply {
    private static final Logger LOGGER = Logger.getLogger(Water.class.getName());
    private LocalDateTime allocationDate;

    /**
     * Constructs a new Water object with the type set to "water".
     */
    public Water() {
        super("water");
    }

    /**
     * Sets the allocation date of the water supply.
     * 
     * @param date the date and time the water was allocated
     */
    public void setAllocationDate(LocalDateTime date) {
        this.allocationDate = date;
    }

    /**
     * Gets the allocation date of the water supply.
     * 
     * @return the date and time the water was allocated
     */
    public LocalDateTime getAllocationDate() {
        return allocationDate;
    }

    /**
     * Checks if this water allocation has expired (only applicable to person allocations).
     * Water expires one day after allocation to a person. It does not expire if allocated to a location.
     * 
     * @return true if water has expired (allocated to a person and past the expiry date), false otherwise
     */
    public boolean isExpired() {
        if (allocationDate == null) {
            return false; // Not allocated yet
        }
        LocalDateTime expiryDate = allocationDate.plusDays(1);
        return LocalDateTime.now().isAfter(expiryDate);
    }

    /**
     * Allocates water to a person and sets the allocation date to the current time.
     * The water will expire one day after allocation to the person.
     * 
     * @param personId The ID of the person receiving the water
     * @param db The database connection to use
     * @throws SQLException If a database error occurs or personId is invalid
     */
    public void allocateToPersonWithExpiry(int personId, DatabaseConnector db) throws SQLException {
        if (getId() == 0) {
            throw new SQLException("Water supply ID is not set before allocation");
        }
        if (!db.getPersons().containsKey(personId)) {
            throw new SQLException("Invalid person ID: " + personId);
        }

        try (PreparedStatement ps = db.getConnection().prepareStatement(
                "INSERT INTO SupplyAllocation (supply_id, person_id, location_id, allocation_date) " +
                "VALUES (?, ?, NULL, CURRENT_TIMESTAMP)")) {
            ps.setInt(1, getId());
            ps.setInt(2, personId);
            ps.executeUpdate();
            
            this.allocationDate = LocalDateTime.now();
            LOGGER.log(Level.INFO, "Water allocated to person ID {0} successfully.", personId);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error allocating water to person with ID " + personId, e);
            throw e;
        }
    }

    /**
     * Allocates water to a location (no expiry).
     * 
     * @param locationId The ID of the location where the water is being allocated
     * @param db The database connection to use
     * @throws SQLException If a database error occurs or locationId is invalid
     */
    public void allocateToLocation(int locationId, DatabaseConnector db) throws SQLException {
        if (getId() == 0) {
            throw new SQLException("Water supply ID is not set before allocation");
        }
        if (!db.getLocations().containsKey(locationId)) {
            throw new SQLException("Invalid location ID: " + locationId);
        }

        try (PreparedStatement ps = db.getConnection().prepareStatement(
                "INSERT INTO SupplyAllocation (supply_id, person_id, location_id, allocation_date) " +
                "VALUES (?, NULL, ?, CURRENT_TIMESTAMP)")) {
            ps.setInt(1, getId());
            ps.setInt(2, locationId);
            ps.executeUpdate();
            LOGGER.log(Level.INFO, "Water allocated to location ID {0} successfully.", locationId);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error allocating water to location with ID " + locationId, e);
            throw e;
        }
    }

    /**
     * Removes expired water allocations from the database (person allocations only).
     * Water allocations to persons that are older than 1 day are considered expired.
     * 
     * @param db The database connection to use
     * @return The number of expired water supplies removed, or -1 if an error occurs
     */
    public static int cleanupExpiredWater(DatabaseConnector db) {
        try (PreparedStatement ps = db.getConnection().prepareStatement(
                "DELETE FROM SupplyAllocation WHERE supply_id IN " +
                "(SELECT supply_id FROM Supply WHERE type = 'water') " +
                "AND allocation_date < CURRENT_TIMESTAMP - INTERVAL '1 day' " +
                "AND person_id IS NOT NULL")) {
            int rowsDeleted = ps.executeUpdate();
            LOGGER.log(Level.INFO, "{0} expired water supplies removed from database.", rowsDeleted);
            return rowsDeleted;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to cleanup expired water", e);
            return -1;
        }
    }
}
