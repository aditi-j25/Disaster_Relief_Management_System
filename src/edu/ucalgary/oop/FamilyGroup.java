package edu.ucalgary.oop;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a family group in the system, extending the Person class.
 * A family group contains a list of family members and provides methods
 * to manage these members.
 * 
 * @author 30208786
 * @version 1.0
 * @since 2025-04-08
 * @see Person
 */
public class FamilyGroup extends Person {
    private List<Person> familyMembers;
    
    /**
     * Constructs a new FamilyGroup with the specified personal information.
     * Initializes an empty list of family members.
     * 
     * @param firstName   the first name of the family group's primary contact
     * @param lastName    the last name of the family group's primary contact
     * @param phoneNumber the phone number of the family group's primary contact
     */
    public FamilyGroup(String firstName, String lastName, String phoneNumber) {
        super(firstName, lastName, phoneNumber);
        this.familyMembers = new ArrayList<>();
    }
    
    /**
     * Adds a person to this family group's list of members.
     * 
     * @param member the person to add to the family group
     */
    public void addFamilyMember(Person member) {
        if (member == null) {
            System.out.println("Cannot add null member!");
            return; // Exit the method without adding
        }
        familyMembers.add(member);

    }
    
    /**
     * Creates a new family group in the system by collecting user input.
     * Validates input for first name, last name, and phone number.
     * Generates a unique group ID and creates the family group in the database.
     * 
     * @param ui the user interface implementation used to collect input and access resources
     * @return the ID of the newly created family group, or 0 if creation was canceled
     * @throws SQLException if a database access error occurs
     */
    public static int createNewFamilyGroup(UserInterfaceImplied ui) throws SQLException {
        String firstName = ui.getValidatedInput(
            ui.getLanguageSupport().getText("input_firstname"),
            "^[A-Za-z\\s-]{2,}$",
            ui.getLanguageSupport().getText("invalid_name_format")
        );
        if (firstName == null) return 0;
        
        String lastName = ui.getValidatedInput(
            ui.getLanguageSupport().getText("input_lastname"),
            "^[A-Za-z\\s-]{2,}$",
            ui.getLanguageSupport().getText("invalid_name_format")
        );
        if (lastName == null) return 0;
        
        String phone = ui.getValidatedInput(
            ui.getLanguageSupport().getText("input_phone"),
            "^\\(?(\\d{3})\\)?[-.\\s]?(\\d{3})[-.\\s]?(\\d{4})$",
            ui.getLanguageSupport().getText("invalid_phone_format")
        );
        if (phone == null) return 0;
        
        ResultSet rs = ui.getDbConnection().getConnection().createStatement()
            .executeQuery("SELECT COALESCE(MAX(family_group), 0) + 1 FROM Person");
        rs.next();
        int newGroupId = rs.getInt(1);
        
        FamilyGroup newGroup = new FamilyGroup(firstName, lastName, phone);
        ui.getFamilyGroups().put(newGroupId, newGroup);
                
        ui.showSuccess(String.format(
            ui.getLanguageSupport().getText("family_group_created"),
            firstName + " " + lastName,
            newGroupId
        ));
                
        return newGroupId;
    }
    
    /**
     * Returns a copy of the list of family members in this group.
     * 
     * @return a new ArrayList containing the family members
     */
    public List<Person> getFamilyMembers() {
        return new ArrayList<>(familyMembers);
    }
    
    /**
     * Removes a person from this family group's list of members.
     * 
     * @param member the person to remove from the family group
     */
    public void removeFamilyMember(Person member) {
        familyMembers.remove(member);
    }
    
    /**
     * Returns the number of members in this family group.
     * 
     * @return the size of the family group
     */
    public int getFamilySize() {
        return familyMembers.size();
    }
    
    /**
     * Returns a string representation of this family group.
     * 
     * @return a string containing the family members and primary contact information
     */
    @Override
    public String toString() {
        return "FamilyGroup{" +
                "familyMembers=" + familyMembers +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", phoneNumber='" + getPhoneNumber() + '\'' +
                '}';
    }
}