package edu.ucalgary.oop;

import java.sql.*;

/**
 * The Supply class represents a supply item that can be allocated to people or locations.
 * It contains information such as the type of supply, comments about the supply, 
 * and methods for interacting with a database for allocation purposes.
 * 
 * <p>This class supports operations such as allocating supplies to people or locations, 
 * viewing the current supplies, and editing existing supplies.</p>
 * 
 * @author 30208786
 * @version 6.0
 * @since 2025-04-08
 */
public class Supply {

    private int id;
    private String type;
    private String comments;

    /**
     * Constructs a new Supply with the specified type and ID.
     * 
     * @param type the type of the supply
     * @param id the unique identifier for the supply
     */
    public Supply(String type, int id) {
        this.type = type;
        this.id = id;
    }

    /**
     * Constructs a new Supply with the specified type.
     * 
     * @param type the type of the supply
     */
    public Supply(String type) {
        this.type = type;
    }

    /**
     * Default constructor for Supply. This constructor allows for the creation 
     * of a supply with default values.
     */
    public Supply() { }

    /**
     * Constructs a new Supply with the specified ID.
     * 
     * @param id the unique identifier for the supply
     */
    public Supply(int id) {
        this.id = id;
    }

    /**
     * Sets the ID of the supply.
     * 
     * @param id the unique identifier for the supply
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the ID of the supply.
     * 
     * @return the unique identifier of the supply
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the type of the supply.
     * 
     * @return the type of the supply
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of the supply.
     * 
     * @param type the type of the supply
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the comments associated with the supply.
     * 
     * @return the comments for the supply
     */
    public String getComments() {
        return comments;
    }

    /**
     * Sets the comments associated with the supply.
     * 
     * @param comments the comments for the supply
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * Allocates the supply to a person or location by interacting with the user interface.
     * This method handles input, validation, and allocation operations to either a person or location.
     * 
     * @param ui the user interface implied, used for displaying prompts and getting inputs
     */
    public void allocateSupplies(UserInterfaceImplied ui) {
        try {
            ui.showPrompt(ui.getLanguageSupport().getText("select_supply"));
            String supplyIdInput = ui.getInput();
            int supplyId = Integer.parseInt(supplyIdInput);

            Supply supply = ui.getSupplies().get(supplyId);
            if (supply == null) {
                ui.displayError(ui.getLanguageSupport().getText("invalid_supply"));
                return;
            }

            ui.showPrompt(ui.getLanguageSupport().getText("allocate_to_person_or_location"));
            String choice = ui.getInput().toLowerCase();

            if ("p".equals(choice)) {
                int personId = selectPerson("select_recipient", ui);
                if (personId != -1 && checkLocationMatch(supplyId, personId, ui.getDbConnection())) {
                    allocateSupplyToPerson(supplyId, personId, ui.getDbConnection());
                    ui.showSuccess(ui.getLanguageSupport().getText("supply_allocated"));
                } else {
                    ui.displayError(ui.getLanguageSupport().getText("location_mismatch"));
                }
            } else if ("l".equals(choice)) {
                int locationId = selectLocation(ui);  // Changed from DisasterVictim.selectLocation
                allocateSupplyToLocation(supplyId, locationId, ui.getDbConnection());
                ui.getLocations().get(locationId).addSupply(supply);
                ui.showSuccess(ui.getLanguageSupport().getText("supply_allocated"));
            } else {
                ui.displayError(ui.getLanguageSupport().getText("invalid_choice"));
            }
        } catch (NumberFormatException e) {
            ui.displayError(ui.getLanguageSupport().getText("invalid_input"));
        } catch (SQLException e) {
            ui.displayError(ui.getLanguageSupport().getText("error_allocating_supply"));
        }
    }

    /**
     * Views all supplies and their allocation status (either to a person or location).
     * Displays the supply information to the user.
     * 
     * @param ui the user interface implied, used for displaying the supplies and their allocation details
     */
    public void viewSupplies(UserInterfaceImplied ui) {
        try {
            Statement stmt = ui.getDbConnection().getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(
                "SELECT s.supply_id, s.type, s.comments, sa.person_id, sa.location_id " +
                "FROM Supply s LEFT JOIN SupplyAllocation sa ON s.supply_id = sa.supply_id");
            while (rs.next()) {
                String allocation = rs.getInt("person_id") != 0 ? 
                    "Person: " + ui.getPersons().get(rs.getInt("person_id")).getFullName() :
                    rs.getInt("location_id") != 0 ? 
                    "Location: " + ui.getLocations().get(rs.getInt("location_id")).getName() : "Unallocated";
                System.out.println(rs.getInt("supply_id") + ". " + rs.getString("type") +
                    (rs.getString("comments") != null ? " - " + rs.getString("comments") : "") +
                    " (" + allocation + ")");
            }
        } catch (SQLException e) {
            ui.logError("Failed to view supplies", e);
            ui.displayError(ui.getLanguageSupport().getText("error_viewing_supplies"));
        }
    }

    /**
     * Edits an existing supply by allowing the user to update its type and comments.
     * This method updates the supply information in the database.
     * 
     * @param ui the user interface implied, used for prompting and input validation
     */
    public static void editSupply(UserInterfaceImplied ui) {
        // Prompt for the supply ID to edit.
        ui.showPrompt(ui.getLanguageSupport().getText("enter_supply_id") + " ");
        String input = ui.getInput();
        try {
            int supplyId = Integer.parseInt(input);
            Supply supply = ui.getSupplies().get(supplyId);
            if (supply == null) {
                ui.displayError(ui.getLanguageSupport().getText("invalid_supply_id"));
                return;
            }
            // Prompt for the new supply type.
            String newType = ui.getValidatedInput(
                ui.getLanguageSupport().getText("input_supply_type"),
                ".+",
                ui.getLanguageSupport().getText("invalid_supply_type")
            );
            if (newType != null) {
                supply.setType(newType);
            }
            // Prompt for new comments (optional) for that specific supply.
            String newComments = ui.getValidatedInput(
                ui.getLanguageSupport().getText("input_supply_comments"),
                ".*",
                ""
            );
            supply.setComments(newComments);
            supply.updateDatabase(ui);
            ui.showSuccess(ui.getLanguageSupport().getText("update_successful"));
        } catch (NumberFormatException e) {
            ui.displayError(ui.getLanguageSupport().getText("invalid_input"));
        } catch (SQLException e) {
            ui.displayError(ui.getLanguageSupport().getText("update_failed"));
            ui.logError("Failed to update supply", e);
        }
    }

    /**
     * Updates the supply record in the database with the current type and comments.
     * 
     * @param ui the user interface implied, used for database connections
     * @throws SQLException if a database access error occurs during the update
     */
    public void updateDatabase(UserInterfaceImplied ui) throws SQLException {
        if (getId() <= 0) {
            throw new SQLException("Invalid supply ID");
        }
        
        String sql = "UPDATE Supply SET type = ?, comments = ? WHERE supply_id = ?";
        try (PreparedStatement ps = ui.getDbConnection().getConnection().prepareStatement(sql)) {
            ps.setString(1, getType() != null ? getType() : "");
            ps.setString(2, getComments());
            ps.setInt(3, getId());
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new SQLException("Supply not found with ID: " + getId());
            }
        }
    }

    /**
     * Selects a person based on their ID and performs validation of input.
     *
     * <p>This method displays a prompt to the user via the given User Interface 
     * and requests input for the person ID. It validates the input to check 
     * if it corresponds to a valid person in the system. If the input is invalid 
     * or the ID does not match an existing person, -1 is returned.</p>
     *
     * @param prompt the text prompt displayed to the user
     * @param ui the User Interface object used to interact with the user
     * @return the valid person ID if found; otherwise, -1
     */
    public int selectPerson(String prompt, UserInterfaceImplied ui) {
        ui.showPrompt(ui.getLanguageSupport().getText(prompt));
        try {
            int personId = Integer.parseInt(ui.getInput());
            return ui.getPersons().containsKey(personId) ? personId : -1;
        } catch (NumberFormatException e) {
            ui.displayError(ui.getLanguageSupport().getText("invalid_input"));
            return -1;
        }
    }

    /**
     * Checks if a given supply allocation matches a person's location.
     *
     * <p>This method executes a SQL query to verify whether a specific supply 
     * allocation is associated with a person's location. It uses the provided 
     * database connection to prepare and execute the query securely with 
     * parameters.</p>
     *
     * @param supplyId the ID of the supply allocation
     * @param personId the ID of the person
     * @param db the DatabaseConnector instance for database operations
     * @return {@code true} if a matching record is found; {@code false} otherwise
     * @throws SQLException if a database access error occurs
     */    
    public boolean checkLocationMatch(int supplyId, int personId, DatabaseConnector db) throws SQLException {
        String sql = "SELECT COUNT(*) FROM PersonLocation pl " +
                    "JOIN SupplyAllocation sa ON pl.location_id = sa.location_id " +
                    "WHERE pl.person_id = ? AND sa.supply_id = ?";
        
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, personId);
            ps.setInt(2, supplyId);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }
    
    /**
     * Allocates a supply to a specific person in the database.
     *
     * <p>This method inserts a record into the SupplyAllocation table, linking a supply 
     * to a person. It validates the person and supply IDs before performing the operation 
     * and throws an exception if either ID is invalid.</p>
     *
     * @param supplyId the ID of the supply to be allocated
     * @param personId the ID of the person to whom the supply is allocated
     * @param db the DatabaseConnector instance for database operations
     * @throws SQLException if the person ID or supply ID is invalid, or if a database access error occurs
     */
    public void allocateSupplyToPerson(int supplyId, int personId, DatabaseConnector db) throws SQLException {
        String sql = "INSERT INTO SupplyAllocation (supply_id, person_id) VALUES (?, ?)";
        if (!db.getPersons().containsKey(personId)) {
            throw new SQLException("Invalid person ID: " + personId);
        }
        if (!db.getSupplies().containsKey(supplyId)) {
            throw new SQLException("Invalid supply ID: " + supplyId);
        }
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, supplyId);
            ps.setInt(2, personId);
            ps.executeUpdate();
        }
    }

    /**
     * Allocates a supply to a specific location in the database.
     *
     * <p>This method inserts a record into the SupplyAllocation table, linking a supply 
     * to a location. It validates the location and supply IDs before performing the operation 
     * and throws an exception if either ID is invalid.</p>
     *
     * @param supplyId the ID of the supply to be allocated
     * @param locationId the ID of the location to which the supply is allocated
     * @param db the DatabaseConnector instance for database operations
     * @throws SQLException if the location ID or supply ID is invalid, or if a database access error occurs
     */
    public void allocateSupplyToLocation(int supplyId, int locationId, DatabaseConnector db) throws SQLException {
        String sql = "INSERT INTO SupplyAllocation (supply_id, location_id) VALUES (?, ?)";
        if (!db.getLocations().containsKey(locationId)) {
            throw new SQLException("Invalid location ID: " + locationId);
        }
        if (!db.getSupplies().containsKey(supplyId)) {
            throw new SQLException("Invalid supply ID: " + supplyId);
        }
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, supplyId);
            ps.setInt(2, locationId);
            ps.executeUpdate();
        }
    }

    private int selectLocation(UserInterfaceImplied ui) {
        System.out.println(ui.getLanguageSupport().getText("available_locations"));
        ui.getLocations().forEach((id, loc) -> 
            System.out.println(id + ". " + loc.getName() + " (" + loc.getAddress() + ")"));
            
        String locationInput = ui.getValidatedInput(
            ui.getLanguageSupport().getText("select_location"),
            "^[0-9]+$",
            ui.getLanguageSupport().getText("invalid_location")
        );
        
        try {
            int locationId = Integer.parseInt(locationInput);
            return ui.getLocations().containsKey(locationId) ? locationId : 0;
        } catch (NumberFormatException e) {
            ui.displayError(ui.getLanguageSupport().getText("invalid_input"));
            return 0;
        }
    }
}
