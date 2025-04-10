/**
 * The MedicalRecord class represents medical treatment information for patients.
 * It stores location, treatment details, and the date when treatment was provided.
 * The class ensures that date formats are valid (YYYY-MM-DD).
 *
 * @author 30208786
 * @version 2.0
 * @since 2025-04-02
 */
package edu.ucalgary.oop;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a medical record containing treatment information for disaster victims.
 * Each record includes the location where treatment was provided, details of the treatment,
 * and the date of treatment. The class ensures proper date formatting and validation.
 */
public class MedicalRecord {
    private Location location;
    private String treatmentDetails;
    private String dateOfTreatment;

    /**
     * Constructs a MedicalRecord with the specified parameters.
     *
     * @param location The location where treatment was provided
     * @param treatmentDetails Details about the medical treatment
     * @param dateOfTreatment Date when treatment was provided (format: YYYY-MM-DD)
     * @throws IllegalArgumentException If the date format is invalid
     */
    public MedicalRecord(Location location, String treatmentDetails, String dateOfTreatment) throws IllegalArgumentException {
        setLocation(location);
        this.treatmentDetails = treatmentDetails;
        if (!isValidDateFormat(dateOfTreatment)) {
            throw new IllegalArgumentException("Invalid date format for treatment details. Expected format: YYYY-MM-DD");
        }
        else if (LocalDate.parse(dateOfTreatment).getYear() < 1900) { // Example constraint
            throw new IllegalArgumentException("Date cannot be before the year 1900.");
    }

        this.dateOfTreatment = dateOfTreatment;
    }

    /**
     * Gets the location where treatment was provided.
     *
     * @return The treatment location
     */
    public Location getLocation() { return location; }

    /**
     * Sets the location where treatment was provided.
     *
     * @param location The treatment location
     */
    public void setLocation(Location location) { this.location = location; }

    /**
     * Gets the treatment details.
     *
     * @return The treatment details
     */
    public String getTreatmentDetails() { return treatmentDetails; }

    /**
     * Sets the treatment details.
     *
     * @param treatmentDetails The treatment details
     */
    public void setTreatmentDetails(String treatmentDetails) { this.treatmentDetails = treatmentDetails; }

    /**
     * Gets the date when treatment was provided.
     *
     * @return The date of treatment (format: YYYY-MM-DD)
     */
    public String getDateOfTreatment() { return dateOfTreatment; }

    /**
     * Sets the date when treatment was provided.
     *
     * @param dateOfTreatment The date of treatment (format: YYYY-MM-DD)
     * @throws IllegalArgumentException If the date format is invalid
     */
    public void setDateOfTreatment(String dateOfTreatment) throws IllegalArgumentException {
        if (!isValidDateFormat(dateOfTreatment)) {
            throw new IllegalArgumentException("Invalid date format. Expected format: YYYY-MM-DD");
        }
        this.dateOfTreatment = dateOfTreatment;
    }

    /**
     * Validates that a given string is in the correct date format (YYYY-MM-DD).
     *
     * @param date The date string to validate
     * @return true if the format is valid, false otherwise
     */
    private boolean isValidDateFormat(String date) {
        try {
            LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}