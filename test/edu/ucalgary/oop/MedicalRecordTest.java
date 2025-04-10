package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MedicalRecordTest {
    private Location location;
    private MedicalRecord medicalRecord;
    private String validDate;
    private String validTreatment;
    private String maxLengthTreatment;
    private String maxDate;
    private String minDate;
    
    @Before
    public void setUp() {
        location = new Location("Hospital", "123 Health St");
        validDate = "2025-04-02";
        validTreatment = "Standard Treatment";
        maxLengthTreatment = "a".repeat(255);
        maxDate = "9999-12-31";
        minDate = "1900-01-01";
        medicalRecord = new MedicalRecord(location, validTreatment, validDate);
    }

    @Test
    public void testConstructorValidInput() {
        System.out.println("\n=== Test: Constructor with Valid Input ===");
        System.out.println("Expected treatment: " + validTreatment);
        System.out.println("Actual treatment: " + medicalRecord.getTreatmentDetails());
        assertEquals(validTreatment, medicalRecord.getTreatmentDetails());
    }

    @Test
    public void testSetAndGetLocation() {
        System.out.println("\n=== Test: Set and Get Location ===");
        Location newLocation = new Location("Clinic", "456 Care Ave");
        medicalRecord.setLocation(newLocation);
        System.out.println("Expected location: Clinic");
        System.out.println("Actual location: " + medicalRecord.getLocation().getName());
        assertEquals(newLocation, medicalRecord.getLocation());
    }

    @Test
    public void testSetAndGetTreatmentDetails() {
        System.out.println("\n=== Test: Set and Get Treatment Details ===");
        String newTreatment = "Updated Treatment";
        medicalRecord.setTreatmentDetails(newTreatment);
        System.out.println("Expected treatment: " + newTreatment);
        System.out.println("Actual treatment: " + medicalRecord.getTreatmentDetails());
        assertEquals(newTreatment, medicalRecord.getTreatmentDetails());
    }

    @Test
    public void testEmptyTreatmentDetails() {
        System.out.println("\n=== Test: Empty Treatment Details ===");
        MedicalRecord emptyRecord = new MedicalRecord(location, "", validDate);
        System.out.println("Expected: empty string");
        System.out.println("Actual: '" + emptyRecord.getTreatmentDetails() + "'");
        assertEquals("", emptyRecord.getTreatmentDetails());
    }

    @Test
    public void testValidDateFormat() {
        System.out.println("\n=== Test: Valid Date Format ===");
        System.out.println("Expected date: " + validDate);
        System.out.println("Actual date: " + medicalRecord.getDateOfTreatment());
        assertEquals(validDate, medicalRecord.getDateOfTreatment());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDateFormat() {
        System.out.println("\n=== Test: Invalid Date Format ===");
        String invalidDate = "2025/04/02";
        System.out.println("Testing invalid date: " + invalidDate);
        new MedicalRecord(location, validTreatment, invalidDate);
    }

    @Test
    public void testNullLocation() {
        System.out.println("\n=== Test: Null Location ===");
        medicalRecord.setLocation(null);
        System.out.println("Expected: null");
        System.out.println("Actual: " + medicalRecord.getLocation());
        assertNull(medicalRecord.getLocation());
    }

    @Test
    public void testSpecialCharactersTreatment() {
        System.out.println("\n=== Test: Special Characters in Treatment ===");
        String specialChars = "Treatment!@#$%^&*()";
        medicalRecord.setTreatmentDetails(specialChars);
        System.out.println("Expected: " + specialChars);
        System.out.println("Actual: " + medicalRecord.getTreatmentDetails());
        assertEquals(specialChars, medicalRecord.getTreatmentDetails());
    }

    @Test
    public void testMaxLengthTreatment() {
        System.out.println("\n=== Test: Max Length Treatment ===");
        MedicalRecord maxRecord = new MedicalRecord(location, maxLengthTreatment, validDate);
        System.out.println("Expected length: 255");
        System.out.println("Actual length: " + maxRecord.getTreatmentDetails().length());
        assertEquals(255, maxRecord.getTreatmentDetails().length());
    }

    @Test
    public void testMaxDateValue() {
        System.out.println("\n=== Test: Maximum Valid Date ===");
        MedicalRecord maxDateRecord = new MedicalRecord(location, validTreatment, maxDate);
        System.out.println("Expected date: " + maxDate);
        System.out.println("Actual date: " + maxDateRecord.getDateOfTreatment());
        assertEquals(maxDate, maxDateRecord.getDateOfTreatment());
    }

    @Test
    public void testMinDateValue() {
        System.out.println("\n=== Test: Minimum Valid Date ===");
        MedicalRecord minDateRecord = new MedicalRecord(location, validTreatment, minDate);
        System.out.println("Expected date: " + minDate);
        System.out.println("Actual date: " + minDateRecord.getDateOfTreatment());
        assertEquals(minDate, minDateRecord.getDateOfTreatment());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDateBeforeMinimum() {
        System.out.println("\n=== Test: Date Before Minimum ===");
        String invalidDate = "0000-12-31"; // This date is invalid as it might not follow valid ISO_DATE ranges.
        System.out.println("Testing invalid date: " + invalidDate);
        new MedicalRecord(location, validTreatment, invalidDate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDateAfterMaximum() {
        System.out.println("\n=== Test: Date After Maximum ===");
        String invalidDate = "10000-01-01";
        System.out.println("Testing invalid date: " + invalidDate);
        new MedicalRecord(location, validTreatment, invalidDate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFebruaryLeapYearInvalid() {
        System.out.println("\n=== Test: Invalid February 29 Non-Leap Year ===");
        String invalidDate = "2025-02-29";
        System.out.println("Testing invalid date: " + invalidDate);
        new MedicalRecord(location, validTreatment, invalidDate);
    }

    @Test
    public void testFebruaryLeapYearValid() {
        System.out.println("\n=== Test: Valid February 29 Leap Year ===");
        String leapYearDate = "2024-02-29";
        MedicalRecord leapYearRecord = new MedicalRecord(location, validTreatment, leapYearDate);
        System.out.println("Expected date: " + leapYearDate);
        System.out.println("Actual date: " + leapYearRecord.getDateOfTreatment());
        assertEquals(leapYearDate, leapYearRecord.getDateOfTreatment());
    }
}
