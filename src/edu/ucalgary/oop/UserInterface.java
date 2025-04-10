package edu.ucalgary.oop;

/**
 * UserInterface defines the structure for Command-Line Interface (CLI) user interactions
 * in the disaster relief management system.
 * This interface outlines methods for displaying menus, prompts, errors, and handling user input.
 * It also includes workflows for managing disaster victims, inquiries, and supplies.
 * 
 * <p>Classes implementing this interface must provide concrete implementations 
 * of the defined methods for user interaction.</p>
 * 
 * @author 30208786
 * @version 3.0
 * @since 2025-04-08
 */
public interface UserInterface {

    /**
     * Displays the main menu to the user, presenting options for further actions.
     * This method should show the list of available options for the user to select from.
     */
    void displayMainMenu();

    /**
     * Displays an error message to the user.
     * 
     * @param message the error message to display
     *        This message is typically used to inform the user of invalid inputs, system errors, or any issues.
     */
    void displayError(String message);

    /**
     * Displays a success message to the user.
     * 
     * @param message the message to display
     *        This message is typically used to notify the user of a successful action or operation.
     */
    void showSuccess(String message);

    /**
     * Displays a prompt message to the user, typically used for user input requests.
     * 
     * @param message the message to prompt
     *        This message instructs the user on what action or input is required from them.
     */
    void showPrompt(String message);

    /**
     * Gets user input from the Command-Line Interface (CLI).
     * This method allows the user to provide a single line of input when prompted.
     * 
     * @return the user’s input as a string
     *         This will be the input provided by the user, trimmed of leading and trailing whitespaces.
     */
    String getInput();

    /**
     * Displays detailed information about disaster victims in the system.
     * This method should provide a formatted view of all the disaster victims' details, such as name, gender, phone number,
     * and family group information.
     */
    void viewDisasterVictimInfo();

}
