package edu.ucalgary.oop;

/**
 * Main entry point for the Disaster Relief Management System.
 * Handles application initialization and lifecycle.
 * 
 * Supports command-line language selection via --lang=xx-YY.
 * Falls back to interactive language menu if not provided.
 * 
 * Example: java Main --lang=fr-CA
 * 
 * @author 30208786
 * @version 2.1
 * @since 2024-03-30
 */
public class Main {
    /**
     * Default constructor.
     */
    public Main() { }

    /**
     * The main entry point of the application.
     * Initializes the system components in the following order:
     * 1. Language support with default Canadian English locale
     * 2. User interface with language support
     * 3. Database connection
     * 
     * Handles cleanup of resources and error logging.
     *
     * @param args Command line arguments (supports --lang=xx-YY)
     */
    public static void main(String[] args) {
        try {
            String languageCode = null;

            // Check for command-line argument --lang=xx-YY
            for (String arg : args) {
                if (arg.startsWith("--lang=")) {
                    languageCode = arg.substring("--lang=".length());
                    break;
                }
            }
            // Initialize language support
            LanguageSupport languageSupport;
            if (languageCode != null) {
                languageSupport = new LanguageSupport(languageCode);
            } else {
                languageSupport = new LanguageSupport("en-CA");
                languageSupport.chooseLanguage();
            }

            // Create the user interface with language support
            UserInterfaceImplied ui = new UserInterfaceImplied(languageSupport);

            // Get the singleton database connection instance and set it
            DatabaseConnector dbConnection = DatabaseConnection.getInstance();
            ui.setDatabaseConnection(dbConnection);

            // Run the application
            ui.run();

            // Cleanup: Close the database connection
            dbConnection.closeConnection();
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
