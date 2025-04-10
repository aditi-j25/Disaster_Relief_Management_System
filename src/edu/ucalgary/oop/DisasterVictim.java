package edu.ucalgary.oop;

import java.sql.*;

/**
 * Represents a victim of a disaster in the disaster management system.
 * This class extends the Person class and adds disaster-specific information
 * and functionality to track and manage disaster victims.
 * 
 * @author 30208786
 * @version 13.0
 * @since 2025-04-08
 * @see Person
 */
public class DisasterVictim extends Person {
    private String disasterType;

    /**
     * Constructs a new DisasterVictim with the specified personal information and disaster type.
     *
     * @param firstName    The first name of the disaster victim
     * @param lastName     The last name of the disaster victim
     * @param phoneNumber  The phone number of the disaster victim
     * @param disasterType The type of disaster that affected this victim
     */
    public DisasterVictim(String firstName, String lastName, String phoneNumber, String disasterType) {
        super(firstName, lastName, phoneNumber);
        this.disasterType = disasterType;
    }

    /**
     * Returns the type of disaster that affected this victim.
     * 
     * @return The disaster type as a String
     */
    public String getDisasterType() { return disasterType; }
    
    /**
     * Sets the type of disaster that affected this victim.
     * 
     * @param disasterType The new disaster type to set
     */
    public void setDisasterType(String disasterType) { this.disasterType = disasterType; }

    /**
     * Adds a new disaster victim to the system.
     * Collects and validates all required information from the user:
     * - Personal details (first name, last name, phone number)
     * - Gender
     * - Date of birth
     * - Location
     * - Family group
     * - Comments about the victim's situation
     * 
     * Data is saved to the database and added to the application's data structures.
     * 
     * @param ui The user interface implementation to use for input/output
     */
    public void addDisasterVictim(UserInterfaceImplied ui) {
        try {
            String firstName = ui.getValidatedInput(
                ui.getLanguageSupport().getText("input_firstname"),
                "^[A-Za-z\\s-]{2,}$",
                ui.getLanguageSupport().getText("invalid_input")
            );
            if (firstName == null) return;

            String lastName = ui.getValidatedInput(
                ui.getLanguageSupport().getText("input_lastname"),
                "^[A-Za-z\\s-]{2,}$",
                ui.getLanguageSupport().getText("invalid_input")
            );
            if (lastName == null) return;

            String phone = ui.getValidatedInput(
                ui.getLanguageSupport().getText("input_phone"),
                "^\\(?(\\d{3})\\)?[-.\\s]?(\\d{3})[-.\\s]?(\\d{4})$",
                ui.getLanguageSupport().getText("invalid_phone")
            );
            if (phone == null) return;

            String dateOfBirth = ui.getValidatedInput(
                ui.getLanguageSupport().getText("input_dob"),
                "^\\d{4}-\\d{2}-\\d{2}$",
                ui.getLanguageSupport().getText("invalid_dob")
            );
            if (dateOfBirth == null) return;

            String gender = selectGender(ui);
            if (gender == null) return;

            int locationId = selectLocation(ui);
            if (locationId == 0) return;

            int familyGroupId = selectFamilyGroup(ui);

            String comments = ui.getMultiLineInput(
                ui.getLanguageSupport().getText("input_comments")
            );

            DisasterVictim victim = new DisasterVictim(firstName, lastName, phone, "Unknown");
            victim.setDateOfBirth(dateOfBirth);
            victim.setGender(gender);
            
            int personId = savePersonToDatabase(victim, gender, familyGroupId, locationId, comments, ui.getDbConnection());
            victim.setId(personId);
            ui.getPersons().put(personId, victim);
            ui.getLocations().get(locationId).addOccupant(victim);
            if (familyGroupId != 0) {
                ui.getFamilyGroups().computeIfAbsent(familyGroupId, k -> new FamilyGroup("Group", String.valueOf(k), null))
                    .addFamilyMember(victim);
            }
            
            printVictimTable(victim, locationId, familyGroupId, comments, ui);
            
            ui.showSuccess(String.format(ui.getLanguageSupport().getText("victim_added"), firstName, lastName));
        } catch (SQLException e) {
            ui.displayError(ui.getLanguageSupport().getText("error_adding_victim"));
            e.printStackTrace();
        }
    }

    /**
     * Prints victim information in a formatted table.
     * 
     * @param victim        The disaster victim to display
     * @param locationId    The ID of the victim's location
     * @param familyGroupId The ID of the victim's family group
     * @param comments      Additional comments about the victim
     * @param ui           The user interface implementation to use for input/output
     */
    private void printVictimTable(DisasterVictim victim, int locationId, int familyGroupId, String comments, UserInterfaceImplied ui) {
        System.out.println("\n" + ui.getLanguageSupport().getText("victim_details_header"));
        System.out.println("+" + "-".repeat(20) + "+" + "-".repeat(40) + "+");
        printTableRow(ui.getLanguageSupport().getText("table_id"), String.valueOf(victim.getId()), ui);
        printTableRow(ui.getLanguageSupport().getText("table_name"), victim.getFullName(), ui);
        printTableRow(ui.getLanguageSupport().getText("table_phone"), victim.getPhoneNumber(), ui);
        printTableRow(ui.getLanguageSupport().getText("table_dob"), victim.getDateOfBirth(), ui);
        printTableRow(ui.getLanguageSupport().getText("table_gender"), victim.getGender(), ui);
        
        String locationName = "Unknown";
        if (ui.getLocations().containsKey(locationId)) {
            locationName = ui.getLocations().get(locationId).getName();
        }
        printTableRow(ui.getLanguageSupport().getText("table_location"), locationName, ui);
        
        String familyGroupName = ui.getLanguageSupport().getText("no_family_group");
        if (familyGroupId != 0 && ui.getFamilyGroups().containsKey(familyGroupId)) {
            familyGroupName = "Group " + familyGroupId;
        }
        printTableRow(ui.getLanguageSupport().getText("table_family_group"), familyGroupName, ui);
        
        System.out.println("+" + "-".repeat(20) + "+" + "-".repeat(40) + "+");
        System.out.println(ui.getLanguageSupport().getText("table_comments_header"));
        System.out.println("+" + "-".repeat(62) + "+");
        
        if (comments != null && !comments.isEmpty()) {
            String[] commentLines = comments.split("\n");
            for (String line : commentLines) {
                for (int i = 0; i < line.length(); i += 60) {
                    int end = Math.min(i + 60, line.length());
                    System.out.println("| " + line.substring(i, end) + " ".repeat(Math.max(0, 60 - (end - i))) + " |");
                }
            }
        } else {
            System.out.println("| " + ui.getLanguageSupport().getText("no_comments") + " ".repeat(49) + " |");
        }
        
        System.out.println("+" + "-".repeat(62) + "+");
    }
    
    /**
     * Prints a single row in the victim details table.
     * 
     * @param title The title/label for the table row
     * @param value The value to display in the row
     * @param ui    The user interface implementation to use for input/output
     */
    private void printTableRow(String title, String value, UserInterfaceImplied ui) {
        System.out.println("| " + title + " ".repeat(Math.max(0, 19 - title.length())) + 
                          "| " + value + " ".repeat(Math.max(0, 39 - value.length())) + "|");
    }

    /**
     * Prompts the user to select a gender from the available options.
     * 
     * @param ui The user interface implementation to use for input/output
     * @return The selected gender as a string, or null if selection failed
     */
    private String selectGender(UserInterfaceImplied ui) {
        for (int retries = ui.getMaxRetries(); retries > 0; retries--) {
            System.out.println("1. " + ui.getLanguageSupport().getText("gender_man"));
            System.out.println("2. " + ui.getLanguageSupport().getText("gender_woman"));
            System.out.println("3. " + ui.getLanguageSupport().getText("gender_nb"));
            ui.showPrompt(ui.getLanguageSupport().getText("select_gender"));
            String choice = ui.getInput();
            switch (choice) {
                case "1": return "man";
                case "2": return "woman";
                case "3": return "non-binary";
                default: ui.displayError(ui.getLanguageSupport().getText("invalid_gender"));
            }
        }
        ui.displayError(ui.getLanguageSupport().getText("max_retries_exceeded"));
        return null;
    }
    
    /**
     * Prompts the user to select an existing family group or create a new one.
     * 
     * @param ui The user interface implementation to use for input/output
     * @return The ID of the selected family group, or 0 if no group was selected
     * @throws SQLException If there's an error accessing the database
     */
    private int selectFamilyGroup(UserInterfaceImplied ui) throws SQLException {
        for (int retries = ui.getMaxRetries(); retries > 0; retries--) {
            String choice = ui.getValidatedInput(
                ui.getLanguageSupport().getText("join_family_group"),
                "^[YyNn]$",
                ui.getLanguageSupport().getText("invalid_y_n")
            );
            
            if (choice == null) {
                ui.displayError(ui.getLanguageSupport().getText("max_retries_exceeded"));
                return 0;
            }

            if ("y".equals(choice.toLowerCase())) {
                if (ui.getFamilyGroups().isEmpty()) {
                    ui.showPrompt(ui.getLanguageSupport().getText("no_existing_groups"));
                    int newGroupId = FamilyGroup.createNewFamilyGroup(ui);
                    return newGroupId;
                }
                
                displayFamilyGroups(ui);
                String familyInput = ui.getValidatedInput(
                    ui.getLanguageSupport().getText("select_family_group"),
                    "^[0-9]+$",
                    ui.getLanguageSupport().getText("invalid_family_group")
                );
                
                if (familyInput == null) return 0;
                
                try {
                    int familyId = Integer.parseInt(familyInput);
                    if (ui.getFamilyGroups().containsKey(familyId)) {
                        return familyId;
                    } else {
                        String createNew = ui.getValidatedInput(
                            ui.getLanguageSupport().getText("create_new_group"),
                            "^[YyNn]$",
                            ui.getLanguageSupport().getText("invalid_y_n")
                        );
                        
                        if (createNew != null && "y".equals(createNew.toLowerCase())) {
                            int newGroupId = FamilyGroup.createNewFamilyGroup(ui);
                            return newGroupId;
                        }
                    }
                    ui.displayError(ui.getLanguageSupport().getText("invalid_family_group"));
                } catch (NumberFormatException e) {
                    ui.displayError(ui.getLanguageSupport().getText("invalid_input"));
                }
            } else if ("n".equals(choice.toLowerCase())) {
                return 0;
            }
        }
        ui.displayError(ui.getLanguageSupport().getText("max_retries_exceeded"));
        return 0;
    }

    /**
     * Displays all available family groups in the system.
     * 
     * @param ui The user interface implementation to use for input/output
     */
    private void displayFamilyGroups(UserInterfaceImplied ui) {
        if (ui.getFamilyGroups().isEmpty()) {
            System.out.println(ui.getLanguageSupport().getText("no_family_groups"));
            return;
        }
        
        System.out.println(ui.getLanguageSupport().getText("available_family_groups"));
        ui.getFamilyGroups().forEach((id, group) -> 
            System.out.println(id + ". Family Group " + id + " (" + group.getFamilySize() + " members)"));
    }

    /**
     * Prompts the user to select a location for the disaster victim.
     * 
     * @param ui The user interface implementation to use for input/output
     * @return The ID of the selected location, or 0 if selection failed
     */
    public int selectLocation(UserInterfaceImplied ui) {
        displayLocations(ui);
        for (int retries = ui.getMaxRetries(); retries > 0; retries--) {
            String locationInput = ui.getValidatedInput(
                ui.getLanguageSupport().getText("select_location"),
                "^[0-9]+$",
                ui.getLanguageSupport().getText("invalid_location")
            );
            
            if (locationInput == null) {
                ui.displayError(ui.getLanguageSupport().getText("max_retries_exceeded"));
                return 0;
            }
            
            try {
                int locationId = Integer.parseInt(locationInput);
                if (ui.getLocations().containsKey(locationId)) {
                    return locationId;
                }
                ui.displayError(ui.getLanguageSupport().getText("invalid_location"));
            } catch (NumberFormatException e) {
                ui.displayError(ui.getLanguageSupport().getText("invalid_input"));
            }
        }
        ui.displayError(ui.getLanguageSupport().getText("max_retries_exceeded"));
        return 0;
    }

    /**
     * Displays all available locations in the system.
     * 
     * @param ui The user interface implementation to use for input/output
     */
    private void displayLocations(UserInterfaceImplied ui) {
        System.out.println(ui.getLanguageSupport().getText("available_locations"));
        ui.getLocations().forEach((id, loc) -> 
            System.out.println(id + ". " + loc.getName() + " (" + loc.getAddress() + ")"));
    }

    /**
     * Saves a person's information to the database.
     * 
     * @param person        The person to save
     * @param gender        The gender of the person
     * @param familyGroupId The ID of the person's family group
     * @param locationId    The ID of the person's location
     * @param comments      Additional comments about the person
     * @param db           The database connection to use
     * @return The ID of the saved person record
     * @throws SQLException If there's an error accessing the database
     */
    private int savePersonToDatabase(Person person, String gender, int familyGroupId, int locationId, String comments, DatabaseConnector db) throws SQLException {
        boolean hasCommentsColumn = false;
        ResultSet columns = db.getConnection().getMetaData().getColumns(null, null, "Person", "comments");
        hasCommentsColumn = columns.next();
        columns.close();
        
        String sql;
        if (hasCommentsColumn) {
            sql = "INSERT INTO Person (first_name, last_name, gender, phone_number, date_of_birth, family_group, comments) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING person_id";
        } else {
            sql = "INSERT INTO Person (first_name, last_name, gender, phone_number, date_of_birth, family_group) " +
                "VALUES (?, ?, ?, ?, ?, ?) RETURNING person_id";
        }
        
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setString(1, person.getFirstName());
            ps.setString(2, person.getLastName());
            ps.setString(3, gender);
            ps.setString(4, person.getPhoneNumber());
            
            try {
                Date sqlDate = Date.valueOf(person.getDateOfBirth());
                ps.setDate(5, sqlDate);
            } catch (IllegalArgumentException e) {
                ps.setNull(5, Types.DATE);
            }
            
            if (familyGroupId == 0) {
                ps.setNull(6, Types.INTEGER);
            } else {
                ps.setInt(6, familyGroupId);
            }
            
            if (hasCommentsColumn) {
                ps.setString(7, comments);
            }
            
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt("person_id");
        }
    }

    /**
     * Returns a string representation of this DisasterVictim object.
     * 
     * @return A string containing the disaster victim's details
     */
    @Override
    public String toString() {
        return "DisasterVictim{" +
                "disasterType='" + disasterType + '\'' +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", phoneNumber='" + getPhoneNumber() + '\'' +
                ", dateOfBirth='" + getDateOfBirth() + '\'' +
                '}';
    }

    /**
     * Static method that prompts the user for which DisasterVictim to edit.
     * This method locates the victim by ID and calls the instance method to
     * perform the specific edits.
     * 
     * @param ui The user interface implementation to use for input/output
     */
    public static void editDisasterVictim(UserInterfaceImplied ui) {
        ui.showPrompt(ui.getLanguageSupport().getText("enter_disaster_victim_id") + " ");
        String idInput = ui.getInput();
        try {
            int id = Integer.parseInt(idInput);
            Person person = ui.getPersons().get(id);
            if (person instanceof DisasterVictim) {
                DisasterVictim victim = (DisasterVictim) person;
                victim.performEdit(ui);
            } else {
                ui.displayError(ui.getLanguageSupport().getText("invalid_disaster_victim_id"));
            }
        } catch (NumberFormatException e) {
            ui.displayError(ui.getLanguageSupport().getText("invalid_input"));
        }
    }

    /**
     * Instance method that presents an edit menu to the user and processes their selection.
     * This method allows editing of specific fields like name, phone, date of birth, etc.
     * 
     * @param ui The user interface implementation to use for input/output
     */
    public void performEdit(UserInterfaceImplied ui) {
        try {
            System.out.println("\n" + ui.getLanguageSupport().getText("edit_victim_menu"));
            System.out.println("1. " + ui.getLanguageSupport().getText("edit_name"));
            System.out.println("2. " + ui.getLanguageSupport().getText("edit_phone"));
            System.out.println("3. " + ui.getLanguageSupport().getText("edit_dob"));
            System.out.println("4. " + ui.getLanguageSupport().getText("edit_gender"));
            System.out.println("5. " + ui.getLanguageSupport().getText("edit_location"));
            System.out.println("6. " + ui.getLanguageSupport().getText("edit_family"));
            System.out.println("0. " + ui.getLanguageSupport().getText("return_main_menu"));

            String choice = ui.getValidatedInput(
                ui.getLanguageSupport().getText("select_edit_field"),
                "^[0-6]$",
                ui.getLanguageSupport().getText("invalid_option")
            );
            if (choice == null || choice.equals("0"))
                return;
            switch (choice) {
                case "1":
                    updateName(ui);
                    break;
                case "2":
                    updatePhone(ui);
                    break;
                case "3":
                    updateDateOfBirth(ui);
                    break;
                case "4":
                    updateGender(ui);
                    break;
                case "5":
                    updateLocation(ui);
                    break;
                case "6":
                    updateFamilyGroup(ui);
                    break;
                default:
                    ui.displayError(ui.getLanguageSupport().getText("invalid_option"));
                    return;
            }
            updateDatabase(ui);
            ui.showSuccess(ui.getLanguageSupport().getText("update_successful"));
        } catch (SQLException e) {
            ui.displayError(ui.getLanguageSupport().getText("update_failed"));
            Person.logError(e);
        }
    }

    /**
     * Updates the name information of this disaster victim.
     * 
     * @param ui The user interface implementation to use for input/output
     */
    private void updateName(UserInterfaceImplied ui) {
        String firstName = ui.getValidatedInput(
            ui.getLanguageSupport().getText("input_firstname"),
            "^[A-Za-z\\s-]{2,}$",
            ui.getLanguageSupport().getText("invalid_input")
        );
        if (firstName != null) {
            setFirstName(firstName);
        }
        String lastName = ui.getValidatedInput(
            ui.getLanguageSupport().getText("input_lastname"),
            "^[A-Za-z\\s-]{2,}$",
            ui.getLanguageSupport().getText("invalid_input")
        );
        if (lastName != null) {
            setLastName(lastName);
        }
    }

    /**
     * Updates the phone number of this disaster victim.
     * 
     * @param ui The user interface implementation to use for input/output
     */
    private void updatePhone(UserInterfaceImplied ui) {
        String phone = ui.getValidatedInput(
            ui.getLanguageSupport().getText("input_phone"),
            "^\\(?(\\d{3})\\)?[-.\\s]?(\\d{3})[-.\\s]?(\\d{4})$",
            ui.getLanguageSupport().getText("invalid_phone")
        );
        if (phone != null) {
            setPhoneNumber(phone);
        }
    }

    /**
     * Updates the date of birth of this disaster victim.
     * 
     * @param ui The user interface implementation to use for input/output
     */
    private void updateDateOfBirth(UserInterfaceImplied ui) {
        String dob = ui.getValidatedInput(
            ui.getLanguageSupport().getText("input_dob"),
            "^\\d{4}-\\d{2}-\\d{2}$",
            ui.getLanguageSupport().getText("invalid_dob")
        );
        if (dob != null) {
            setDateOfBirth(dob);
        }
    }

    /**
     * Updates the gender of this disaster victim.
     * 
     * @param ui The user interface implementation to use for input/output
     */
    private void updateGender(UserInterfaceImplied ui) {
        String gender = selectGender(ui);
        if (gender != null) {
            setGender(gender);
        }
    }

    /**
     * Updates the location of this disaster victim.
     * Removes the victim from their current location and adds them to the selected location.
     * 
     * @param ui The user interface implementation to use for input/output
     */
    private void updateLocation(UserInterfaceImplied ui) {
        int locationId = selectLocation(ui);
        if (locationId != 0) {
            ui.getLocations().values().forEach(loc -> loc.removeOccupant(this));
            ui.getLocations().get(locationId).addOccupant(this);
        }
    }

    /**
     * Updates the family group of this disaster victim.
     * Removes the victim from their current family group and adds them to the selected group.
     * 
     * @param ui The user interface implementation to use for input/output
     * @throws SQLException If there's an error accessing the database
     */
    private void updateFamilyGroup(UserInterfaceImplied ui) throws SQLException {
        int familyGroupId = selectFamilyGroup(ui);
        if (familyGroupId != 0) {
            ui.getFamilyGroups().values().forEach(group -> group.removeFamilyMember(this));
            ui.getFamilyGroups().get(familyGroupId).addFamilyMember(this);
        }
    }

    /**
     * Updates the disaster victim's information in the database.
     * 
     * @param ui The user interface implementation to use for input/output
     * @throws SQLException If there's an error accessing the database
     */
    private void updateDatabase(UserInterfaceImplied ui) throws SQLException {
        String sql = "UPDATE Person SET first_name=?, last_name=?, phone_number=?, gender=?, date_of_birth=? WHERE person_id=?";
        
        try (PreparedStatement ps = ui.getDbConnection().getConnection().prepareStatement(sql)) {
            ps.setString(1, getFirstName());
            ps.setString(2, getLastName());
            ps.setString(3, getPhoneNumber());
            ps.setString(4, getGender());
            try {
                Date sqlDate = Date.valueOf(getDateOfBirth());
                ps.setDate(5, sqlDate);
            } catch (IllegalArgumentException e) {
                ps.setNull(5, Types.DATE);
            }
            ps.setInt(6, getId());
            
            ps.executeUpdate();
        }
    }
}