package edu.ucalgary.oop;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Represents an inquirer in the disaster management system, extending the Person class.
 * An inquirer is an individual who makes inquiries about possible victims or provides
 * information related to a disaster event.
 * 
 * @author 30208786
 * @version 6.0
 * @since 2025-04-08
 * @see Person
 */
public class Inquirer extends Person {
    private String inquiryMessage;
    private boolean isVictim;

    /**
     * Constructs a new Inquirer with the specified personal information.
     * 
     * @param firstName      the first name of the inquirer
     * @param lastName       the last name of the inquirer
     * @param phoneNumber    the phone number of the inquirer
     * @param inquiryMessage the message or inquiry details provided
     * @param isVictim       whether the inquirer is also a victim of the disaster
     */
    public Inquirer(String firstName, String lastName, String phoneNumber, String inquiryMessage, boolean isVictim) {
        super(firstName, lastName, phoneNumber);
        this.inquiryMessage = inquiryMessage;
        this.isVictim = isVictim;
    }

    /**
     * Default constructor for creating an empty Inquirer object.
     */
    public Inquirer() { } // Default constructor

    /**
     * Returns the inquiry message provided by this inquirer.
     * 
     * @return the inquiry message
     */
    public String getInquiryMessage() { 
        return inquiryMessage; 
    }

    /**
     * Sets the inquiry message for this inquirer.
     * 
     * @param inquiryMessage the new inquiry message
     */
    public void setInquiryMessage(String inquiryMessage) { 
        this.inquiryMessage = inquiryMessage; 
    }

    /**
     * Checks if this inquirer is also registered as a victim.
     * 
     * @return true if the inquirer is a victim, false otherwise
     */
    public boolean isVictim() { 
        return isVictim; 
    }

    /**
     * Sets the victim status of this inquirer.
     * 
     * @param isVictim the new victim status
     */
    public void setVictim(boolean isVictim) { 
        this.isVictim = isVictim; 
    }

    /**
     * Allows the user to select an existing inquirer from the system.
     * Displays a list of available inquirers and prompts for selection.
     * 
     * @param ui the user interface implementation used to interact with the user
     * @return the selected Inquirer object, or null if selection was cancelled or invalid
     */
    public Inquirer selectInquirer(UserInterfaceImplied ui) {
        Map<Integer, Person> persons = ui.getPersons();
        if (persons == null || persons.isEmpty()) {
            ui.displayError(ui.getLanguageSupport().getText("no_inquirers"));
            return createInquirer(ui);
        }
        displayInquirerList(ui, persons);
        String input = ui.getValidatedInput(
            ui.getLanguageSupport().getText("select_inquirer_id"),
            "\\d+",
            ui.getLanguageSupport().getText("invalid_input")
        );
        if (input == null) {
            ui.displayError(ui.getLanguageSupport().getText("returning_menu"));
            return null;
        }
        try {
            int id = Integer.parseInt(input);
            Person person = persons.get(id);
            if (person instanceof Inquirer) {
                return (Inquirer) person;
            }
            ui.displayError(ui.getLanguageSupport().getText("invalid_inquirer_id"));
        } catch (NumberFormatException e) {
            ui.displayError(ui.getLanguageSupport().getText("invalid_input"));
            logError(e);
        }
        return null;
    }

    /**
     * Displays a formatted table of all inquirers in the system.
     * 
     * @param ui the user interface implementation used to access text resources
     * @param persons the map of all persons in the system, keyed by their ID
     */
    public void displayInquirerList(UserInterfaceImplied ui, Map<Integer, Person> persons) {
        System.out.println("\n" + ui.getLanguageSupport().getText("inquirer_table_header"));
        System.out.println("+" + "-".repeat(20) + "+" + "-".repeat(30) + "+" + "-".repeat(40) + "+");
        System.out.printf("| %-18s | %-28s | %-38s |\n", 
            ui.getLanguageSupport().getText("inquirer_table_id"),
            ui.getLanguageSupport().getText("inquirer_table_name"),
            ui.getLanguageSupport().getText("inquirer_table_message")
        );
        System.out.println("+" + "-".repeat(20) + "+" + "-".repeat(30) + "+" + "-".repeat(40) + "+");
        persons.forEach((id, person) -> {
            if (person instanceof Inquirer) {
                Inquirer inquirer = (Inquirer) person;
                String message = inquirer.getInquiryMessage();
                if (message != null && message.length() > 35) {
                    message = message.substring(0, 35) + "...";
                }
                System.out.printf("| %-18d | %-28s | %-38s |\n", 
                    id, 
                    inquirer.getFullName(),
                    message != null ? message : ui.getLanguageSupport().getText("no_message")
                );
            }
        });
        System.out.println("+" + "-".repeat(20) + "+" + "-".repeat(30) + "+" + "-".repeat(40) + "+");
    }

    /**
     * Creates a new inquirer by collecting required information from the user.
     * The created inquirer is saved to the database and added to the system.
     * 
     * @param ui the user interface implementation used to collect input and access resources
     * @return the newly created Inquirer object, or null if creation was cancelled
     */
    public Inquirer createInquirer(UserInterfaceImplied ui) {
        String firstName = ui.getValidatedInput(
            ui.getLanguageSupport().getText("inquirer_firstname"),
            "^[a-zA-Z]+$",
            ui.getLanguageSupport().getText("invalid_firstname")
        );
        if (firstName == null) return null;

        String lastName = ui.getValidatedInput(
            ui.getLanguageSupport().getText("inquirer_lastname"),
            "^[a-zA-Z]+$",
            ui.getLanguageSupport().getText("invalid_lastname")
        );
        if (lastName == null) return null;

        String phoneNumber = ui.getValidatedInput(
            ui.getLanguageSupport().getText("inquirer_phone"),
            "\\d{10}",
            ui.getLanguageSupport().getText("invalid_phone_format")
        );
        if (phoneNumber == null) return null;

        String inquiryMessage = ui.getValidatedInput(
            ui.getLanguageSupport().getText("inquirer_message"),
            ".+",
            ui.getLanguageSupport().getText("empty_message")
        );
        if (inquiryMessage == null) return null;

        String isVictimInput = ui.getValidatedInput(
            ui.getLanguageSupport().getText("inquirer_is_victim"),
            "yes|no",
            ui.getLanguageSupport().getText("invalid_yes_no")
        );
        if (isVictimInput == null) return null;
        boolean isVictim = isVictimInput.equalsIgnoreCase("yes");

        Inquirer inquirer = new Inquirer(firstName, lastName, phoneNumber, inquiryMessage, isVictim);
        try {
            int id = saveInquirerToDatabase(ui, inquirer);
            inquirer.setId(id);
            ui.getPersons().put(id, inquirer);
            ui.showSuccess(String.format(
                ui.getLanguageSupport().getText("inquirer_created"),
                id
            ));
        } catch (SQLException e) {
            ui.displayError(String.format(
                ui.getLanguageSupport().getText("error_save_inquirer"),
                e.getMessage()
            ));
            logError(e);
        }
        return inquirer;
    }

    /**
     * Saves the inquirer information to the database.
     * 
     * @param ui the user interface implementation used to access the database connection
     * @param inquirer the inquirer object to save
     * @return the generated ID of the newly saved inquirer
     * @throws SQLException if a database access error occurs
     */
    private int saveInquirerToDatabase(UserInterfaceImplied ui, Inquirer inquirer) throws SQLException {
        String sql = "INSERT INTO Person (first_name, last_name, phone_number, comments, is_victim) " +
                     "VALUES (?, ?, ?, ?, ?) RETURNING person_id";
        try (PreparedStatement ps = ui.getDbConnection().getConnection().prepareStatement(sql)) {
            ps.setString(1, inquirer.getFirstName());
            ps.setString(2, inquirer.getLastName());
            ps.setString(3, inquirer.getPhoneNumber());
            ps.setString(4, inquirer.getInquiryMessage());
            ps.setBoolean(5, inquirer.isVictim());
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("person_id");
            }
            throw new SQLException("Failed to retrieve generated ID");
        }
    }

    /**
     * Returns a string representation of this inquirer.
     * 
     * @return a string containing the inquirer's details
     */
    @Override
    public String toString() {
        return String.format("Inquirer [ID=%d, Name=%s %s, Phone=%s, Message=%s, IsVictim=%b]",
            getId(),
            getFirstName(),
            getLastName(),
            getPhoneNumber(),
            inquiryMessage,
            isVictim
        );
    }
}