package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PersonalBelongingTest {
    private PersonalBelonging belonging;
    private String testDescription;
    private String maxLengthDescription;
    private String specialCharsDescription;
    private String extremelyLongDescription;
    
    @Before
    public void setUp() {
        testDescription = "Test Item";
        maxLengthDescription = "a".repeat(255);
        specialCharsDescription = "Test-Item#1@Emergency_Zone";
        extremelyLongDescription = "a".repeat(1000);
        belonging = new PersonalBelonging(testDescription);
    }

    @Test
    public void testConstructorType() {
        System.out.println("\n=== Test: Constructor Sets Correct Type ===");
        System.out.println("Expected output: personal item");
        System.out.println("Actual output: " + belonging.getType());
        assertEquals("personal item", belonging.getType());
    }

    @Test
    public void testConstructorDescription() {
        System.out.println("\n=== Test: Constructor Sets Description ===");
        System.out.println("Expected output: " + testDescription);
        System.out.println("Actual output: " + belonging.getComments());
        assertEquals(testDescription, belonging.getComments());
    }

    @Test
    public void testSetAndGetComments() {
        System.out.println("\n=== Test: Set and Get Comments ===");
        String newDescription = "Updated Description";
        belonging.setComments(newDescription);
        System.out.println("Expected output: " + newDescription);
        System.out.println("Actual output: " + belonging.getComments());
        assertEquals(newDescription, belonging.getComments());
    }

    @Test
    public void testMaxLengthDescription() {
        System.out.println("\n=== Test: Max Length Description ===");
        PersonalBelonging maxLengthBelonging = new PersonalBelonging(maxLengthDescription);
        System.out.println("Expected length: 255");
        System.out.println("Actual length: " + maxLengthBelonging.getComments().length());
        assertEquals(255, maxLengthBelonging.getComments().length());
    }

    @Test
    public void testSpecialCharactersDescription() {
        System.out.println("\n=== Test: Special Characters in Description ===");
        PersonalBelonging specialCharsBelonging = new PersonalBelonging(specialCharsDescription);
        System.out.println("Expected output: " + specialCharsDescription);
        System.out.println("Actual output: " + specialCharsBelonging.getComments());
        assertEquals(specialCharsDescription, specialCharsBelonging.getComments());
    }

    @Test
    public void testNullDescription() {
        System.out.println("\n=== Test: Null Description ===");
        PersonalBelonging nullBelonging = new PersonalBelonging(null);
        System.out.println("Expected output: null");
        System.out.println("Actual output: " + nullBelonging.getComments());
        assertNull(nullBelonging.getComments());
    }

    @Test
    public void testEmptyDescription() {
        System.out.println("\n=== Test: Empty Description ===");
        PersonalBelonging emptyBelonging = new PersonalBelonging("");
        System.out.println("Expected output: empty string");
        System.out.println("Actual output: " + emptyBelonging.getComments());
        assertEquals("", emptyBelonging.getComments());
    }

    @Test
    public void testNumbersOnlyDescription() {
        System.out.println("\n=== Test: Numbers Only Description ===");
        PersonalBelonging numbersBelonging = new PersonalBelonging("12345678901234567890");
        System.out.println("Expected output: 12345678901234567890");
        System.out.println("Actual output: " + numbersBelonging.getComments());
        assertEquals("12345678901234567890", numbersBelonging.getComments());
    }

    @Test
    public void testExtremelyLongDescription() {
        System.out.println("\n=== Test: Extremely Long Description ===");
        PersonalBelonging longBelonging = new PersonalBelonging(extremelyLongDescription);
        System.out.println("Expected max length: 1000");
        System.out.println("Actual length: " + longBelonging.getComments().length());
        assertTrue(longBelonging.getComments().length() <= 1000);
    }

    @Test
    public void testWhitespaceOnlyDescription() {
        System.out.println("\n=== Test: Whitespace Only Description ===");
        PersonalBelonging whitespaceBelonging = new PersonalBelonging("   ");
        System.out.println("Expected output: '   '");
        System.out.println("Actual output: '" + whitespaceBelonging.getComments() + "'");
        assertEquals("   ", whitespaceBelonging.getComments());
    }

    @Test
    public void testNewlineDescription() {
        System.out.println("\n=== Test: Newline in Description ===");
        String newlineDesc = "Line1\nLine2\rLine3\r\nLine4";
        PersonalBelonging newlineBelonging = new PersonalBelonging(newlineDesc);
        System.out.println("Expected output: " + newlineDesc);
        System.out.println("Actual output: " + newlineBelonging.getComments());
        assertEquals(newlineDesc, newlineBelonging.getComments());
    }
}
