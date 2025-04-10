package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DisasterVictimTest {
    private DisasterVictim victim;
    private MockDatabaseConnection dbConnector;
    private UserInterfaceImplied ui;
    private String maxLengthString;
    private String specialCharsString;
    
    @Before
    public void setUp() {
        dbConnector = MockDatabaseConnectionImpl.getInstance();
        dbConnector.reset();
        dbConnector.populateTestData();
        ui = new UserInterfaceImplied(new LanguageSupport("en-CA"));
        ui.setDatabaseConnection(dbConnector);
        
        maxLengthString = "a".repeat(255);
        specialCharsString = "O'Connor-Smith Jr.#$@";
        victim = new DisasterVictim("Robert", "Smith", "403-123-4567", "Flood");
    }

    @Test
    public void testConstructorValues() {
        System.out.println("\n=== Test: Constructor Values ===");
        System.out.println("Expected name: Robert Smith");
        System.out.println("Actual name: " + victim.getFullName());
        assertEquals("Robert Smith", victim.getFullName());
    }

    @Test
    public void testSetAndGetDisasterType() {
        System.out.println("\n=== Test: Set and Get Disaster Type ===");
        String newType = "Earthquake";
        victim.setDisasterType(newType);
        System.out.println("Expected type: " + newType);
        System.out.println("Actual type: " + victim.getDisasterType());
        assertEquals(newType, victim.getDisasterType());
    }

    @Test
    public void testSetAndGetDateOfBirth() {
        System.out.println("\n=== Test: Set and Get Date of Birth ===");
        String dob = "1990-01-01";
        victim.setDateOfBirth(dob);
        System.out.println("Expected DOB: " + dob);
        System.out.println("Actual DOB: " + victim.getDateOfBirth());
        assertEquals(dob, victim.getDateOfBirth());
    }

    @Test
    public void testMaxLengthName() {
        System.out.println("\n=== Test: Max Length Name ===");
        victim.setFirstName(maxLengthString);
        System.out.println("Expected length: 255");
        System.out.println("Actual length: " + victim.getFirstName().length());
        assertEquals(255, victim.getFirstName().length());
    }

    @Test
    public void testSpecialCharactersName() {
        System.out.println("\n=== Test: Special Characters Name ===");
        victim.setFirstName(specialCharsString);
        System.out.println("Expected: " + specialCharsString);
        System.out.println("Actual: " + victim.getFirstName());
        assertEquals(specialCharsString, victim.getFirstName());
    }

    @Test
    public void testToString() {
        System.out.println("\n=== Test: ToString ===");
        victim.setDateOfBirth("1990-01-01");
        String expected = "DisasterVictim{disasterType='Flood', firstName='Robert', lastName='Smith', " +
                         "phoneNumber='403-123-4567', dateOfBirth='1990-01-01'}";
        System.out.println("Expected: " + expected);
        System.out.println("Actual: " + victim.toString());
        assertEquals(expected, victim.toString());
    }

    @Test
    public void testEmptyStrings() {
        System.out.println("\n=== Test: Empty Strings ===");
        DisasterVictim emptyVictim = new DisasterVictim("", "", "", "");
        System.out.println("Expected name: ' '");
        System.out.println("Actual name: '" + emptyVictim.getFullName() + "'");
        assertEquals(" ", emptyVictim.getFullName());
    }

    @Test
    public void testExtremelyLongDisasterType() {
        System.out.println("\n=== Test: Extremely Long Disaster Type ===");
        String longType = "a".repeat(1000);
        victim.setDisasterType(longType);
        System.out.println("Expected length: 1000");
        System.out.println("Actual length: " + victim.getDisasterType().length());
        assertEquals(1000, victim.getDisasterType().length());
    }

    @Test
    public void testInvalidDateFormat() {
        System.out.println("\n=== Test: Invalid Date Format ===");
        String invalidDate = "01/01/1990";
        victim.setDateOfBirth(invalidDate);
        System.out.println("Expected: " + invalidDate);
        System.out.println("Actual: " + victim.getDateOfBirth());
        assertEquals(invalidDate, victim.getDateOfBirth());
    }

    @Test
    public void testFutureDateOfBirth() {
        System.out.println("\n=== Test: Future Date of Birth ===");
        String futureDate = "2025-12-31";
        victim.setDateOfBirth(futureDate);
        System.out.println("Expected: " + futureDate);
        System.out.println("Actual: " + victim.getDateOfBirth());
        assertEquals(futureDate, victim.getDateOfBirth());
    }

    @Test
    public void testSpecialCharactersInPhoneNumber() {
        System.out.println("\n=== Test: Special Characters in Phone Number ===");
        String specialPhone = "+1-(403)-123-4567";
        victim.setPhoneNumber(specialPhone);
        System.out.println("Expected: " + specialPhone);
        System.out.println("Actual: " + victim.getPhoneNumber());
        assertEquals(specialPhone, victim.getPhoneNumber());
    }

    @Test
    public void testNullValues() {
        System.out.println("\n=== Test: Null Values ===");
        victim.setFirstName(null);
        victim.setLastName(null);
        victim.setPhoneNumber(null);
        victim.setDisasterType(null);
        System.out.println("Expected first name: null");
        System.out.println("Actual first name: " + victim.getFirstName());
        assertNull(victim.getFirstName());
    }

    @Test
    public void testMaxIntegerId() {
        System.out.println("\n=== Test: Maximum Integer ID ===");
        victim.setId(Integer.MAX_VALUE);
        System.out.println("Expected: " + Integer.MAX_VALUE);
        System.out.println("Actual: " + victim.getId());
        assertEquals(Integer.MAX_VALUE, victim.getId());
    }

    @Test
    public void testMinIntegerId() {
        System.out.println("\n=== Test: Minimum Integer ID ===");
        victim.setId(Integer.MIN_VALUE);
        System.out.println("Expected: " + Integer.MIN_VALUE);
        System.out.println("Actual: " + victim.getId());
        assertEquals(Integer.MIN_VALUE, victim.getId());
    }

    @Test
    public void testLeapYearDateOfBirth() {
        System.out.println("\n=== Test: Leap Year Date of Birth ===");
        String leapDate = "2024-02-29";
        victim.setDateOfBirth(leapDate);
        System.out.println("Expected: " + leapDate);
        System.out.println("Actual: " + victim.getDateOfBirth());
        assertEquals(leapDate, victim.getDateOfBirth());
    }

    @Test
    public void testWhitespaceOnlyNames() {
        System.out.println("\n=== Test: Whitespace Only Names ===");
        victim.setFirstName("   ");
        victim.setLastName("   ");
        System.out.println("Expected full name: '       '");
        System.out.println("Actual full name: '" + victim.getFullName() + "'");
        assertEquals("       ", victim.getFullName());
    }

    @Test
    public void testMultilineDisasterType() {
        System.out.println("\n=== Test: Multiline Disaster Type ===");
        String multiline = "Line1\nLine2\rLine3\r\nLine4";
        victim.setDisasterType(multiline);
        System.out.println("Expected: " + multiline);
        System.out.println("Actual: " + victim.getDisasterType());
        assertEquals(multiline, victim.getDisasterType());
    }

    @Test
    public void testSetAndGetGender() {
        System.out.println("\n=== Test: Set and Get Gender ===");
        String[] genders = {"man", "woman", "non-binary", ""};
        for (String gender : genders) {
            victim.setGender(gender);
            System.out.println("Expected: " + gender);
            System.out.println("Actual: " + victim.getGender());
            assertEquals(gender, victim.getGender());
        }
    }

    @Test
    public void testBoundaryPhoneNumber() {
        System.out.println("\n=== Test: Boundary Phone Number ===");
        String longPhone = "1".repeat(50);
        victim.setPhoneNumber(longPhone);
        System.out.println("Expected: " + longPhone);
        System.out.println("Actual: " + victim.getPhoneNumber());
        assertEquals(longPhone, victim.getPhoneNumber());
    }

    @Test
    public void testInternationalPhoneFormat() {
        System.out.println("\n=== Test: International Phone Format ===");
        String intlPhone = "+44-20-7123-4567";
        victim.setPhoneNumber(intlPhone);
        System.out.println("Expected: " + intlPhone);
        System.out.println("Actual: " + victim.getPhoneNumber());
        assertEquals(intlPhone, victim.getPhoneNumber());
    }

    @Test
    public void testMinimumNameLength() {
        System.out.println("\n=== Test: Minimum Name Length ===");
        String minName = "A";
        victim.setFirstName(minName);
        System.out.println("Expected: " + minName);
        System.out.println("Actual: " + victim.getFirstName());
        assertEquals(minName, victim.getFirstName());
    }

    @Test
    public void testDateOfBirthBoundaries() {
        System.out.println("\n=== Test: Date of Birth Boundaries ===");
        String[] testDates = {"0001-01-01", "9999-12-31"};
        for (String date : testDates) {
            victim.setDateOfBirth(date);
            System.out.println("Expected: " + date);
            System.out.println("Actual: " + victim.getDateOfBirth());
            assertEquals(date, victim.getDateOfBirth());
        }
    }

    @Test
    public void testSpecialCharactersDisasterType() {
        System.out.println("\n=== Test: Special Characters Disaster Type ===");
        String specialDisaster = "Earthquake-Tsunami (Category 5) @ Location!";
        victim.setDisasterType(specialDisaster);
        System.out.println("Expected: " + specialDisaster);
        System.out.println("Actual: " + victim.getDisasterType());
        assertEquals(specialDisaster, victim.getDisasterType());
    }

    @Test
    public void testNonAsciiCharacters() {
        System.out.println("\n=== Test: Non-ASCII Characters ===");
        String nonAsciiName = "John-André Doe";
        victim.setFirstName(nonAsciiName);
        System.out.println("Expected: " + nonAsciiName);
        System.out.println("Actual: " + victim.getFirstName());
        assertEquals(nonAsciiName, victim.getFirstName());
    }
}