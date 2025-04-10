package edu.ucalgary.oop;

import java.sql.*;

/**
 * Represents an inquiry in the disaster management system.
 * An inquiry is a record of someone seeking information about a potential victim
 * or providing information related to a disaster event.
 * 
 * @author 308786
 * @version 9.0
 * @since 2025-04-08
 * @see Inquirer
 * @see Person
 */
public class Inquiry {
    private Inquirer inquirer;
    private String inquiryMessage;
    private int id, seekingId;

    /**
     * Constructs a new Inquiry with the specified inquirer and message.
     * 
     * @param inquirer       the person making the inquiry
     * @param inquiryMessage the detailed message or content of the inquiry
     * @throws NullPointerException if inquirer is null
     */
    public Inquiry(Inquirer inquirer, String inquiryMessage) {
        if (inquirer == null) throw new NullPointerException("Inquirer cannot be null");
        if (inquiryMessage == null) {
            throw new NullPointerException("Message cannot be null");
        }
        this.inquirer = inquirer;
        this.inquiryMessage = inquiryMessage;
    }

    /**
     * Sets the ID of this inquiry.
     * 
     * @param id the inquiry ID to set
     */
    public void setId(int id) { 
        this.id = id; 
    }
    
    /**
     * Returns the ID of this inquiry.
     * 
     * @return the inquiry ID
     */
    public int getId() { 
        return id; 
    }
    
    /**
     * Returns the inquirer who made this inquiry.
     * 
     * @return the inquirer object
     */
    public Inquirer getInquirer() { 
        return inquirer; 
    }
    
    /**
     * Returns the message content of this inquiry.
     * 
     * @return the inquiry message
     */
    public String getInquiryMessage() { 
        return inquiryMessage; 
    }
    
    /**
     * Sets the ID of the person being sought in this inquiry.
     * 
     * @param seekingId the ID of the person being sought
     */
    public void setSeekingId(int seekingId) { 
        this.seekingId = seekingId; 
    }
    
    /**
     * Returns the ID of the person being sought in this inquiry.
     * 
     * @return the ID of the person being sought
     */
    public int getSeekingId() { 
        return seekingId; 
    }

    /**
     * Logs a new inquiry into the system.
     * Collects necessary information about the inquirer and the person being sought,
     * and saves the inquiry to the database.
     * 
     * @param ui the user interface implementation used to collect input and access resources
     */
    public void logInquiry(UserInterfaceImplied ui) {
        try {
            ui.showPrompt(ui.getLanguageSupport().getText("inquiry_type"));
            System.out.println("1. External Inquirer");
            System.out.println("2. Disaster Victim");
            String type = ui.getValidatedInput("Enter your choice (1 or 2): ", "1|2", "Invalid choice. Please enter 1 or 2.");

            Inquirer inquirer = null;

            if ("2".equals(type)) {
                // Select a Disaster Victim as the inquirer.
                int personId = selectPerson("select_inquirer", ui);
                if (personId == -1) return;
                Person person = ui.getPersons().get(personId);
                if (person instanceof Inquirer) {
                    inquirer = (Inquirer) person;
                    inquirer.setVictim(true);
                } else {
                    ui.displayError("The selected person is not a valid inquirer.");
                    return;
                }
            } else {
                // Create a new External Inquirer
                String firstName = ui.getValidatedInput("Enter first name: ", "^[a-zA-Z\\s'-]+$", "Invalid first name.");
                if (firstName == null) return;
                String lastName = ui.getValidatedInput("Enter last name: ", "^[a-zA-Z\\s'-]+$", "Invalid last name.");
                if (lastName == null) return;
                String phone = ui.getValidatedInput("Enter phone number (e.g., 1234567890): ", "\\d{10}", "Invalid phone number.");
                if (phone == null) return;
                inquirer = new Inquirer(firstName, lastName, phone, "", false);
                int id = savePersonToDatabase(inquirer, null, 0, 0, ui.getDbConnection());
                inquirer.setId(id);
                ui.getPersons().put(id, inquirer);
            }

            // Select the person being inquired about
            int seekingId = selectPerson("select_subject", ui);
            if (seekingId == -1) return;

            // Get inquiry details.
            String details = ui.getValidatedInput("Enter inquiry details: ", ".+", "Inquiry details cannot be empty.");
            if (details == null) return;
            inquirer.setInquiryMessage(details);

            // Save the new inquiry.
            Inquiry inquiry = new Inquiry(inquirer, details);
            inquiry.setSeekingId(seekingId);
            int inquiryId = saveInquiryToDatabase(inquiry, ui.getDbConnection(), ui);
            inquiry.setId(inquiryId);
            ui.getInquiries().put(inquiryId, inquiry);
            ui.showSuccess(ui.getLanguageSupport().getText("inquiry_logged"));
        } catch (SQLException e) {
            ui.displayError(ui.getLanguageSupport().getText("error_logging_inquiry"));
        }
    }

    /**
     * Allows editing of an existing inquiry.
     * Prompts the user for new inquiry details and updates the record in the database.
     * 
     * @param ui the user interface implementation used to collect input and access resources
     */
    public void editInquiry(UserInterfaceImplied ui) {
        try {
            // Ask the user for the new inquiry details.
            String newDetails = ui.getValidatedInput(
                    ui.getLanguageSupport().getText("input_edit_inquiry"),
                    ".+",
                    ui.getLanguageSupport().getText("invalid_inquiry")
            );
            if (newDetails == null) return;
            // Update the in-memory inquiry message.
            this.inquiryMessage = newDetails;
            this.inquirer.setInquiryMessage(newDetails);
            // Update the inquiry record in the database.
            updateDatabase(ui);
            ui.showSuccess(ui.getLanguageSupport().getText("update_successful"));
        } catch (SQLException e) {
            ui.displayError(ui.getLanguageSupport().getText("update_failed"));
            Person.logError(e);
        }
    }

    /**
     * Updates the inquiry details in the database.
     * 
     * @param ui the user interface implementation used to access the database connection
     * @throws SQLException if a database access error occurs
     */
    private void updateDatabase(UserInterfaceImplied ui) throws SQLException {
        String sql = "UPDATE Inquiry SET comments = ? WHERE inquiry_id = ?";
        try (PreparedStatement ps = ui.getDbConnection().getConnection().prepareStatement(sql)) {
            ps.setString(1, this.inquiryMessage);
            ps.setInt(2, this.id);
            ps.executeUpdate();
        }
    }

    /**
     * Saves a new inquiry to the database.
     * 
     * @param inquiry the inquiry object to save
     * @param db the database connection
     * @param ui the user interface implementation
     * @return the generated ID of the newly saved inquiry
     * @throws SQLException if a database access error occurs
     */
    private int saveInquiryToDatabase(Inquiry inquiry, DatabaseConnector db, UserInterfaceImplied ui) throws SQLException {
        PreparedStatement ps = db.getConnection().prepareStatement(
                "INSERT INTO Inquiry (inquirer_id, seeking_id, location_id, date_of_inquiry, comments) " +
                "VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?) RETURNING inquiry_id");
        if (inquiry.getInquirer() == null)
            ps.setNull(1, Types.INTEGER);
        else 
            ps.setInt(1, inquiry.getInquirer().getId());
        ps.setInt(2, inquiry.getSeekingId());
        ps.setInt(3, selectLocation(ui)); // Uses helper method to select a location.
        ps.setString(4, inquiry.getInquiryMessage());
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt("inquiry_id");
    }

    /**
     * Prompts the user to select a person from the system.
     * 
     * @param promptKey the key for the prompt message in the language support
     * @param ui the user interface implementation
     * @return the ID of the selected person, or -1 if selection was cancelled or invalid
     */
    private int selectPerson(String promptKey, UserInterfaceImplied ui) {
        ui.viewDisasterVictimInfo();
        ui.showPrompt(ui.getLanguageSupport().getText(promptKey));
        try {
            int id = Integer.parseInt(ui.getInput());
            if (ui.getPersons().containsKey(id))
                return id;
            ui.displayError(ui.getLanguageSupport().getText("invalid_person"));
            return -1;
        } catch (NumberFormatException e) {
            ui.displayError(ui.getLanguageSupport().getText("invalid_input"));
            return -1;
        }
    }

    /**
     * Saves a person to the database.
     * 
     * @param person the person object to save
     * @param gender the gender of the person
     * @param familyGroupId the ID of the family group the person belongs to (0 if none)
     * @param locationId the ID of the person's location (0 if none)
     * @param db the database connection
     * @return the generated ID of the newly saved person
     * @throws SQLException if a database access error occurs
     */
    private int savePersonToDatabase(Person person, String gender, int familyGroupId, int locationId, DatabaseConnector db) throws SQLException {
        PreparedStatement ps = db.getConnection().prepareStatement(
                "INSERT INTO Person (first_name, last_name, gender, phone_number, family_group) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING person_id");
        ps.setString(1, person.getFirstName());
        ps.setString(2, person.getLastName());
        ps.setString(3, gender);
        ps.setString(4, person.getPhoneNumber());
        if (familyGroupId == 0)
            ps.setNull(5, Types.INTEGER);
        else 
            ps.setInt(5, familyGroupId);
        ResultSet rs = ps.executeQuery();
        rs.next();
        int personId = rs.getInt("person_id");

        if (locationId != 0) {
            ps = db.getConnection().prepareStatement(
                    "INSERT INTO PersonLocation (person_id, location_id) VALUES (?, ?)");
            ps.setInt(1, personId);
            ps.setInt(2, locationId);
            ps.executeUpdate();
        }
        return personId;
    }

    /**
     * Prompts the user to select a location from the system.
     * 
     * @param ui the user interface implementation
     * @return the ID of the selected location, or -1 if selection was cancelled or invalid
     */
    private int selectLocation(UserInterfaceImplied ui) {
        if (ui.getLocations() == null || ui.getLocations().isEmpty()) {
            ui.displayError("No locations available.");
            return -1;
        }
        int attempts = 0;
        final int MAX_RETRIES = 3;
        while (attempts < MAX_RETRIES) {
            ui.getLocations().forEach((id, loc) -> 
                    System.out.println(id + ". " + loc.getName() + " (" + loc.getAddress() + ")"));
            ui.showPrompt(ui.getLanguageSupport().getText("select_location"));
            try {
                int id = Integer.parseInt(ui.getInput());
                if (ui.getLocations().containsKey(id))
                    return id;
                else
                    ui.displayError(ui.getLanguageSupport().getText("invalid_location"));
            } catch (NumberFormatException e) {
                ui.displayError(ui.getLanguageSupport().getText("invalid_input"));
            }
            attempts++;
        }
        ui.displayError("Too many invalid attempts. Returning to the main menu.");
        return -1;
    }

    /**
     * Displays a list of all inquiries in the system.
     * 
     * @param ui the user interface implementation used to access system resources
     */
    public void viewInquiries(UserInterfaceImplied ui) {
        ui.getInquiries().forEach((id, inquiry) ->
            System.out.println(id + ". " + inquiry.getInquiryMessage() +
                " (By: " + (inquiry.getInquirer() != null ? inquiry.getInquirer().getFullName() : "External") +
                ", Seeking: " + ui.getPersons().get(inquiry.getSeekingId()).getFullName() + ")"));
    }

    /**
     * Returns a string representation of this inquiry.
     * 
     * @return a string containing the inquiry details
     */
    @Override
    public String toString() {
        return "Inquiry{" +
                "inquirer=" + inquirer +
                ", inquiryMessage='" + inquiryMessage + '\'' +
                ", seekingId=" + seekingId +
                '}';
    }
}