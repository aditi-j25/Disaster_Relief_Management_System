package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PersonTest {
    private Person person;
    private Supply supply;
    private String maxLengthString;
    private String specialCharsString;
    
    @Before
    public void setUp() {
        person = new Person("Mathew", "Stone", "403-123-4567");
        supply = new Supply("TestSupply", 1);
        maxLengthString = "a".repeat(255);
        specialCharsString = "O'Connor-Smith Jr.#$@";
    }

    @Test
    public void testConstructorFirstName() {
        System.out.println("\n=== Test: Constructor First Name ===");
        System.out.println("Expected output: Mathew");
        System.out.println("Actual output: " + person.getFirstName());
        assertEquals("Mathew", person.getFirstName());
    }

    @Test
    public void testConstructorLastName() {
        System.out.println("\n=== Test: Constructor Last Name ===");
        System.out.println("Expected output: Stone");
        System.out.println("Actual output: " + person.getLastName());
        assertEquals("Stone", person.getLastName());
    }

    @Test
    public void testSetAndGetId() {
        System.out.println("\n=== Test: Set and Get ID ===");
        person.setId(1);
        System.out.println("Expected output: 1");
        System.out.println("Actual output: " + person.getId());
        assertEquals(1, person.getId());
    }

    @Test
    public void testGetFullName() {
        System.out.println("\n=== Test: Get Full Name ===");
        System.out.println("Expected output: Mathew Stone");
        System.out.println("Actual output: " + person.getFullName());
        assertEquals("Mathew Stone", person.getFullName());
    }

    @Test
    public void testSetAndGetPhoneNumber() {
        System.out.println("\n=== Test: Set and Get Phone Number ===");
        person.setPhoneNumber("555-555-5555");
        System.out.println("Expected output: 555-555-5555");
        System.out.println("Actual output: " + person.getPhoneNumber());
        assertEquals("555-555-5555", person.getPhoneNumber());
    }

    @Test
    public void testAddAndGetAllocatedSupply() {
        System.out.println("\n=== Test: Add and Get Allocated Supply ===");
        person.addAllocatedSupply(supply);
        System.out.println("Expected output: 1 (size of allocated supplies)");
        System.out.println("Actual output: " + person.getAllocatedSupplies().size());
        assertEquals(1, person.getAllocatedSupplies().size());
    }

    @Test
    public void testSetAndGetGender() {
        System.out.println("\n=== Test: Set and Get Gender ===");
        person.setGender("Male");
        System.out.println("Expected output: Male");
        System.out.println("Actual output: " + person.getGender());
        assertEquals("Male", person.getGender());
    }

    @Test
    public void testToString() {
        System.out.println("\n=== Test: ToString ===");
        String expected = "Person{id=0, firstName='Mathew', lastName='Stone', phoneNumber='403-123-4567', gender='null'}";
        System.out.println("Expected output: " + expected);
        System.out.println("Actual output: " + person.toString());
        assertEquals(expected, person.toString());
    }

    @Test
    public void testEqualsWithSameValues() {
        System.out.println("\n=== Test: Equals With Same Values ===");
        Person otherPerson = new Person("Mathew", "Stone", "403-123-4567");
        System.out.println("Expected output: true");
        System.out.println("Actual output: " + person.equals(otherPerson));
        assertTrue(person.equals(otherPerson));
    }

    @Test
    public void testSetAndGetDateOfBirth_ValidFormat() {
        System.out.println("\n=== Test: Set and Get Date of Birth (Valid) ===");
        String validDate = "1990-01-01";
        person.setDateOfBirth(validDate);
        System.out.println("Expected output: " + validDate);
        System.out.println("Actual output: " + person.getDateOfBirth());
        assertEquals(validDate, person.getDateOfBirth());
    }

    @Test
    public void testSetAndGetDateOfBirth_InvalidFormat() {
        System.out.println("\n=== Test: Set and Get Date of Birth (Invalid) ===");
        String invalidDate = "01/01/1990";
        person.setDateOfBirth(invalidDate);
        System.out.println("Expected output: " + invalidDate);
        System.out.println("Actual output: " + person.getDateOfBirth());
        assertEquals(invalidDate, person.getDateOfBirth());
    }

    @Test
    public void testSetAndGetPhoneNumber_ValidFormat() {
        System.out.println("\n=== Test: Set and Get Phone Number (Valid) ===");
        String validPhone = "+1-403-123-4567";
        person.setPhoneNumber(validPhone);
        System.out.println("Expected output: " + validPhone);
        System.out.println("Actual output: " + person.getPhoneNumber());
        assertEquals(validPhone, person.getPhoneNumber());
    }

    @Test
    public void testSetAndGetPhoneNumber_InvalidFormat() {
        System.out.println("\n=== Test: Set and Get Phone Number (Invalid) ===");
        String invalidPhone = "123abc4567";
        person.setPhoneNumber(invalidPhone);
        System.out.println("Expected output: " + invalidPhone);
        System.out.println("Actual output: " + person.getPhoneNumber());
        assertEquals(invalidPhone, person.getPhoneNumber());
    }

    @Test
    public void testEquals_NullObject() {
        System.out.println("\n=== Test: Equals with Null Object ===");
        System.out.println("Expected output: false");
        System.out.println("Actual output: " + person.equals(null));
        assertFalse(person.equals(null));
    }

    @Test
    public void testEquals_DifferentClass() {
        System.out.println("\n=== Test: Equals with Different Class ===");
        Object differentObject = new Object();
        System.out.println("Expected output: false");
        System.out.println("Actual output: " + person.equals(differentObject));
        assertFalse(person.equals(differentObject));
    }

    @Test
    public void testEquals_SameReference() {
        System.out.println("\n=== Test: Equals with Same Reference ===");
        System.out.println("Expected output: true");
        System.out.println("Actual output: " + person.equals(person));
        assertTrue(person.equals(person));
    }

    @Test
    public void testConstructorWithNullValues() {
        System.out.println("\n=== Test: Constructor with Null Values ===");
        Person nullPerson = new Person(null, null, null);
        System.out.println("Expected output: null");
        System.out.println("Actual output: " + nullPerson.getFirstName());
        assertNull(nullPerson.getFirstName());
    }

    @Test
    public void testConstructorWithEmptyStrings() {
        System.out.println("\n=== Test: Constructor with Empty Strings ===");
        Person emptyPerson = new Person("", "", "");
        System.out.println("Expected output: empty string");
        System.out.println("Actual output: " + emptyPerson.getFirstName());
        assertEquals("", emptyPerson.getFirstName());
    }

    @Test
    public void testSetFirstNameMaxLength() {
        System.out.println("\n=== Test: Set First Name Max Length ===");
        person.setFirstName(maxLengthString);
        System.out.println("Expected output length: 255");
        System.out.println("Actual output length: " + person.getFirstName().length());
        assertEquals(255, person.getFirstName().length());
    }

    @Test
    public void testSetLastNameSpecialCharacters() {
        System.out.println("\n=== Test: Set Last Name Special Characters ===");
        person.setLastName(specialCharsString);
        System.out.println("Expected output: " + specialCharsString);
        System.out.println("Actual output: " + person.getLastName());
        assertEquals(specialCharsString, person.getLastName());
    }

    @Test
    public void testSetIdMaxInteger() {
        System.out.println("\n=== Test: Set ID Max Integer ===");
        person.setId(Integer.MAX_VALUE);
        System.out.println("Expected output: " + Integer.MAX_VALUE);
        System.out.println("Actual output: " + person.getId());
        assertEquals(Integer.MAX_VALUE, person.getId());
    }

    @Test
    public void testSetIdMinInteger() {
        System.out.println("\n=== Test: Set ID Min Integer ===");
        person.setId(Integer.MIN_VALUE);
        System.out.println("Expected output: " + Integer.MIN_VALUE);
        System.out.println("Actual output: " + person.getId());
        assertEquals(Integer.MIN_VALUE, person.getId());
    }

    @Test
    public void testPhoneNumberWithInternationalFormat() {
        System.out.println("\n=== Test: Phone Number International Format ===");
        String intlPhone = "+1-403-123-4567";
        person.setPhoneNumber(intlPhone);
        System.out.println("Expected output: " + intlPhone);
        System.out.println("Actual output: " + person.getPhoneNumber());
        assertEquals(intlPhone, person.getPhoneNumber());
    }
}