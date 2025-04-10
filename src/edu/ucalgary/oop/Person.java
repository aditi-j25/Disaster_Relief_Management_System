package edu.ucalgary.oop;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * The Person class represents an individual with basic information, including their name, phone number, gender, 
 * date of birth, and a list of allocated supplies. The class provides methods to manage and retrieve personal 
 * information, allocate supplies, and log errors.
 * 
 * @author 30208786
 * @version 2.0
 * @since 2025-04-04
 */
public class Person {
    private int id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String gender;
    private String dateOfBirth;
    private ArrayList<Supply> allocatedSupplies = new ArrayList<>();

    /**
     * Constructs a new Person with the specified first name, last name, and phone number.
     * 
     * @param firstName the first name of the person
     * @param lastName the last name of the person
     * @param phoneNumber the phone number of the person
     */
    public Person(String firstName, String lastName, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    /**
     * Default constructor for creating a Person without initializing values.
     */
    public Person() {}

    /**
     * Gets the ID of the person.
     * 
     * @return the ID of the person
     */
    public int getId() { 
        return id; 
    }

    /**
     * Sets the ID of the person.
     * 
     * @param id the new ID of the person
     */
    public void setId(int id) { 
        this.id = id; 
    }

    /**
     * Gets the first name of the person.
     * 
     * @return the first name of the person
     */
    public String getFirstName() { 
        return firstName; 
    }

    /**
     * Sets the first name of the person.
     * 
     * @param firstName the new first name of the person
     */
    public void setFirstName(String firstName) { 
        this.firstName = firstName; 
    }

    /**
     * Gets the last name of the person.
     * 
     * @return the last name of the person
     */
    public String getLastName() { 
        return lastName; 
    }

    /**
     * Sets the last name of the person.
     * 
     * @param lastName the new last name of the person
     */
    public void setLastName(String lastName) { 
        this.lastName = lastName; 
    }

    /**
     * Gets the phone number of the person.
     * 
     * @return the phone number of the person
     */
    public String getPhoneNumber() { 
        return phoneNumber; 
    }

    /**
     * Sets the phone number of the person.
     * 
     * @param phoneNumber the new phone number of the person
     */
    public void setPhoneNumber(String phoneNumber) { 
        this.phoneNumber = phoneNumber; 
    }

    /**
     * Gets the full name of the person (first name + last name).
     * 
     * @return the full name of the person
     */
    public String getFullName() { 
        return firstName + " " + lastName; 
    }

    /**
     * Gets the gender of the person.
     * 
     * @return the gender of the person
     */
    public String getGender() { 
        return gender; 
    }

    /**
     * Sets the gender of the person.
     * 
     * @param gender the new gender of the person
     */
    public void setGender(String gender) { 
        this.gender = gender; 
    }

    /**
     * Gets the date of birth of the person.
     * 
     * @return the date of birth of the person
     */
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets the date of birth of the person.
     * 
     * @param dateOfBirth the new date of birth of the person
     */
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Adds a supply to the list of allocated supplies for the person.
     * 
     * @param supply the supply to add
     */
    public void addAllocatedSupply(Supply supply) { 
        allocatedSupplies.add(supply); 
    }

    /**
     * Gets the list of supplies allocated to the person.
     * 
     * @return a list of allocated supplies
     */
    public ArrayList<Supply> getAllocatedSupplies() {
        return allocatedSupplies;
    }

    /**
     * Logs an unrecoverable error to the error log file and exits the program.
     * 
     * @param e the exception to log
     */
    public static void logError(Exception e) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String errorMessage = "[" + timestamp + "] ERROR: " + e.toString();
        try (FileWriter fw = new FileWriter("data/errorlog.txt", true);
             PrintWriter pw = new PrintWriter(fw)) {
             pw.println(errorMessage);
             e.printStackTrace(pw);
        } catch (IOException ioe) {
             System.err.println("Failed to write to error log file: " + ioe.toString());
        }
        System.err.println("An unrecoverable error occurred. Please check errorlog.txt for details.");
    }

    /**
     * Returns a string representation of the Person object.
     * 
     * @return a string representation of the Person object
     */
    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }

    /**
     * Compares this Person object with another object for equality.
     * 
     * @param obj the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Person person = (Person) obj;
        return id == person.id &&
               firstName.equals(person.firstName) &&
               lastName.equals(person.lastName) &&
               phoneNumber.equals(person.phoneNumber);
    }
}
