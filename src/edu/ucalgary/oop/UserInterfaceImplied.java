package edu.ucalgary.oop;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Implementation of the UserInterface for the Disaster Relief Management System.
 * This class handles user interactions through a command-line interface,
 * processes user inputs, and coordinates with the database and various system components.
 * 
 * @author 30208786
 * @version 1.0
 * @since 2025-04-08
 * @see UserInterface
 */
public class UserInterfaceImplied implements UserInterface {
    private Scanner scanner;
    private LanguageSupport languageSupport;
    private DatabaseConnector dbConnection;
    private static final int MAX_RETRIES = 3;
    private Map<Integer, Person> persons;
    private Map<Integer, Location> locations;
    private Map<Integer, Supply> supplies;
    private Map<Integer, Inquiry> inquiries;
    private Map<Integer, MedicalRecord> medicalRecords;
    private Map<Integer, FamilyGroup> familyGroups;

    /**
     * Constructs a new UserInterfaceImplied with default English (Canadian) language support.
     */
    public UserInterfaceImplied() {
        this(new LanguageSupport("en-CA"));
    }

    /**
     * Constructs a new UserInterfaceImplied with the specified language support.
     * 
     * @param languageSupport The language support implementation to use for text localization
     */
    public UserInterfaceImplied(LanguageSupport languageSupport) {
        this.scanner = new Scanner(System.in);
        this.languageSupport = languageSupport;
    }

    /**
     * Sets the database connection and initializes data structures from the database.
     * 
     * @param dbConnection The database connection to use for data operations
     */
    public void setDatabaseConnection(DatabaseConnector dbConnection) {
        this.dbConnection = dbConnection;
        this.persons = dbConnection.getPersons();
        this.locations = dbConnection.getLocations();
        this.supplies = dbConnection.getSupplies();
        this.inquiries = dbConnection.getInquiries();
        this.medicalRecords = dbConnection.getMedicalRecords();
        this.familyGroups = dbConnection.getFamilyGroups();
    }

    /**
     * Starts the user interface and runs the main application loop.
     * Displays the main menu, processes user choices, and handles errors.
     * The loop continues until the user chooses to exit or an unrecoverable error occurs.
     */
    public void run() {
        while (true) {
            try {
                displayMainMenu();
                String choice = getInput();
                if (processChoice(choice)) {
                    scanner.close();
                    break;
                }
            } catch (Exception e) {
                System.err.println("Runtime error: " + e.getMessage());
                e.printStackTrace();
                displayError(languageSupport.getText("error_unrecoverable"));
                scanner.close();
                System.exit(1);
            }
        }
    }
    
    /**
     * Processes the user's menu choice and executes the corresponding action.
     * 
     * @param choice The user's menu choice as a string
     * @return true if the application should exit, false otherwise
     * @throws SQLException If there's an error accessing the database
     */
    private boolean processChoice(String choice) throws SQLException {
        switch (choice) {
            case "1":
                System.out.println(getLanguageSupport().getText("adding_victim_info"));
                new DisasterVictim("", "", "", "Unknown").addDisasterVictim(this);
                break;
            case "2":
                System.out.println(getLanguageSupport().getText("viewing_victim_info"));
                viewDisasterVictimInfo();
                break;
            case "3":
                System.out.println(getLanguageSupport().getText("editing_victim_info"));
                new DisasterVictim("", "", "", "Unknown").editDisasterVictim(this);
                break;
            case "4":
                System.out.println(getLanguageSupport().getText("logging_inquiry"));
                Inquirer inquirer = new Inquirer().selectInquirer(this);
                if (inquirer == null) {
                    System.out.println(getLanguageSupport().getText("inquirer_not_found"));
                    break;
                }
                new Inquiry(inquirer, "").logInquiry(this);
                break;
            case "5":
                System.out.println(getLanguageSupport().getText("viewing_inquiry"));
                new Inquirer().displayInquirerList(this, getPersons());
                break;
            case "6":
                System.out.println(getLanguageSupport().getText("editing_inquiry"));
                Inquirer inquirerinput = new Inquirer().selectInquirer(this);
                new Inquiry(inquirerinput,"").editInquiry(this); 
                break;
            case "7":
                System.out.println(getLanguageSupport().getText("allocating_supplies"));
                new Supply().allocateSupplies(this);
                break;
            case "8":
                System.out.println(getLanguageSupport().getText("viewing_supplies"));
                new Supply().viewSupplies(this);
                break;
            case "9":   
                System.out.println(getLanguageSupport().getText("editing_supplies"));
                new Supply().editSupply(this);
                break;
            case "10":
                showSuccess(languageSupport.getText("exit_message"));
                return true;
            default:
                displayError(languageSupport.getText("invalid_choice"));
        }
        return false;
    }
    
    /**
     * Displays the main menu of the application with all available options.
     * The menu text is localized according to the current language settings.
     */
    @Override
    public void displayMainMenu() {
        System.out.println("\n" + languageSupport.getText("Welcome to the Disaster Relief Management System"));
        System.out.println("-------------------------------------------------------------");
        System.out.println(languageSupport.getText("Please select an option from the menu below:"));
        System.out.println("1. " + languageSupport.getText("Add a Disaster victim"));
        System.out.println("2. " + languageSupport.getText("View Disaster victim information"));
        System.out.println("3. " + languageSupport.getText("Edit Disaster victim information"));

        System.out.println("4. " + languageSupport.getText("Log an inquiry about a victim"));
        System.out.println("5. " + languageSupport.getText("View inquiries about victims"));
        System.out.println("6. " + languageSupport.getText("Edit inquiries about victims"));

        System.out.println("7. " + languageSupport.getText("Allocate available supplies"));
        System.out.println("8. " + languageSupport.getText("View current supply inventory and allocations"));
        System.out.println("9. " + languageSupport.getText("Edit supply inventory and allocations"));

        System.out.println("10. " + languageSupport.getText("Exit the system"));
        System.out.println("-------------------------------------------------------------");
        showPrompt(languageSupport.getText("menu_prompt"));
    }

    /**
     * Displays a formatted table of all disaster victims in the system.
     * The table includes ID, name, phone number, gender, date of birth, and family group information.
     */
    @Override
    public void viewDisasterVictimInfo() {
        System.out.println("\n" + languageSupport.getText("victim_list_header"));
        System.out.println("+" + "-".repeat(5) + "+" + "-".repeat(20) + "+" + "-".repeat(15) + "+" + "-".repeat(15) + "+" + "-".repeat(12) + "+" + "-".repeat(15) + "+");
        System.out.printf("| %-3s | %-18s | %-13s | %-13s | %-10s | %-13s |\n", 
                          languageSupport.getText("table_id"),
                          languageSupport.getText("table_name"),
                          languageSupport.getText("table_phone"),
                          languageSupport.getText("table_gender"),
                          languageSupport.getText("table_dob"),
                          languageSupport.getText("table_family_group"));
        System.out.println("+" + "-".repeat(5) + "+" + "-".repeat(20) + "+" + "-".repeat(15) + "+" + "-".repeat(15) + "+" + "-".repeat(12) + "+" + "-".repeat(15) + "+");
        
        persons.forEach((id, person) -> {
            if (person instanceof DisasterVictim) {
                // Find the family group
                String familyGroup = languageSupport.getText("no_family_group");
                for (Map.Entry<Integer, FamilyGroup> entry : familyGroups.entrySet()) {
                    if (entry.getValue().getFamilyMembers().contains(person)) {
                        familyGroup = "Group " + entry.getKey();
                        break;
                    }
                }
                
                System.out.printf("| %-3d | %-18s | %-13s | %-13s | %-10s | %-13s |\n", 
                                 id, 
                                 person.getFullName(),
                                 person.getPhoneNumber(),
                                 (person.getGender() != null ? person.getGender() : "Unknown"),
                                 (person.getDateOfBirth() != null ? person.getDateOfBirth() : "Unknown"),
                                 familyGroup);
            }
        });
        System.out.println("+" + "-".repeat(5) + "+" + "-".repeat(20) + "+" + "-".repeat(15) + "+" + "-".repeat(15) + "+" + "-".repeat(12) + "+" + "-".repeat(15) + "+");
    }

    /**
     * Gets multi-line input from the user until they enter a blank line.
     * 
     * @param prompt The prompt to display to the user
     * @return The multi-line input as a string
     */
    public String getMultiLineInput(String prompt) {
        System.out.println(prompt);
        System.out.println(languageSupport.getText("multiline_instruction"));
        
        StringBuilder input = new StringBuilder();
        String line;
        
        while (true) {
            line = scanner.nextLine();
            if (line.trim().isEmpty()) {
                break;
            }
            input.append(line).append("\n");
        }
        
        return input.toString().trim();
    }

    /**
     * Gets input from the user and validates it against a regular expression pattern.
     * The method will retry up to MAX_RETRIES times if the input is invalid.
     * 
     * @param prompt The prompt to display to the user
     * @param regex The regular expression to validate against
     * @param errorMessage The error message to display if validation fails
     * @return The validated input string, or null if validation repeatedly fails
     */
    public String getValidatedInput(String prompt, String regex, String errorMessage) {
        int attempts = 0;
        while (attempts < MAX_RETRIES) {
            System.out.print(prompt);
            String input = getInput();
            if (input.matches(regex)) {
                return input; // Valid input
            } else {
                System.out.println(errorMessage);
                attempts++;
            }
        }
        System.out.println("Too many invalid attempts. Returning to the main menu.");
        return null; // Return null after exceeding attempts
    }

    /**
     * Gets a single line of input from the user.
     * 
     * @return The user input as a trimmed string
     */
    public String getInput() { return scanner.nextLine().trim(); }
    
    /**
     * Displays a prompt message to the user without a newline.
     * 
     * @param message The prompt message to display
     */
    public void showPrompt(String message) { System.out.print(message); }
    
    /**
     * Displays a success message to the user.
     * 
     * @param message The success message to display
     */
    public void showSuccess(String message) { System.out.println(message); }
    
    /**
     * Displays an error message to the user.
     * 
     * @param message The error message to display
     */
    public void displayError(String message) { System.out.println("Error: " + message); }
    
    /**
     * Logs an error message with an exception to the system log.
     * 
     * @param message The error message to log
     * @param e The exception associated with the error
     */
    public void logError(String message, Exception e) { DatabaseConnector.logError(message, e); }
    
    /**
     * Gets the language support implementation used by this interface.
     * 
     * @return The language support object
     */
    public LanguageSupport getLanguageSupport() { return languageSupport; }
    
    /**
     * Gets the database connection used by this interface.
     * 
     * @return The database connection object
     */
    public DatabaseConnector getDbConnection() { return dbConnection; }
    
    /**
     * Gets the map of person objects indexed by their IDs.
     * 
     * @return The persons map
     */
    public Map<Integer, Person> getPersons() { return persons; }
    
    /**
     * Gets the map of location objects indexed by their IDs.
     * 
     * @return The locations map
     */
    public Map<Integer, Location> getLocations() { return locations; }
    
    /**
     * Gets the map of supply objects indexed by their IDs.
     * 
     * @return The supplies map
     */
    public Map<Integer, Supply> getSupplies() { return supplies; }
    
    /**
     * Gets the map of inquiry objects indexed by their IDs.
     * 
     * @return The inquiries map
     */
    public Map<Integer, Inquiry> getInquiries() { return inquiries; }
    
    /**
     * Gets the map of medical record objects indexed by their IDs.
     * 
     * @return The medical records map
     */
    public Map<Integer, MedicalRecord> getMedicalRecords() { return medicalRecords; }
    
    /**
     * Gets the map of family group objects indexed by their IDs.
     * 
     * @return The family groups map
     */
    public Map<Integer, FamilyGroup> getFamilyGroups() { return familyGroups; }
    
    /**
     * Gets the maximum number of retry attempts for input validation.
     * 
     * @return The maximum number of retries
     */
    public int getMaxRetries() { return MAX_RETRIES; }

}