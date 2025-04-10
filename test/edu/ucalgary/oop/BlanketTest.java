package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BlanketTest {
    private Blanket blanket;
    private MockDatabaseConnection dbConnector;
    private UserInterfaceImplied ui;
    
    @Before
    public void setUp() {
        dbConnector = MockDatabaseConnectionImpl.getInstance();
        dbConnector.reset();
        dbConnector.populateTestData();
        ui = new UserInterfaceImplied(new LanguageSupport("en-CA"));
        ui.setDatabaseConnection(dbConnector);
        blanket = new Blanket();
    }

    @Test
    public void testConstructorType() {
        System.out.println("\n=== Test: Constructor Type ===");
        System.out.println("Expected output: blanket");
        System.out.println("Actual output: " + blanket.getType());
        assertEquals("blanket", blanket.getType());
    }

    @Test
    public void testSetAndGetComments() {
        System.out.println("\n=== Test: Set and Get Comments ===");
        String comment = "New blanket";
        blanket.setComments(comment);
        System.out.println("Expected output: " + comment);
        System.out.println("Actual output: " + blanket.getComments());
        assertEquals(comment, blanket.getComments());
    }

    @Test
    public void testNullComments() {
        System.out.println("\n=== Test: Null Comments ===");
        blanket.setComments(null);
        System.out.println("Expected output: null");
        System.out.println("Actual output: " + blanket.getComments());
        assertNull(blanket.getComments());
    }

    @Test
    public void testEmptyComments() {
        System.out.println("\n=== Test: Empty Comments ===");
        blanket.setComments("");
        System.out.println("Expected output: empty string");
        System.out.println("Actual output: '" + blanket.getComments() + "'");
        assertEquals("", blanket.getComments());
    }

    @Test
    public void testMaxLengthComments() {
        System.out.println("\n=== Test: Max Length Comments ===");
        String maxLength = "a".repeat(255);
        blanket.setComments(maxLength);
        System.out.println("Expected length: 255");
        System.out.println("Actual length: " + blanket.getComments().length());
        assertEquals(255, blanket.getComments().length());
    }

    @Test
    public void testSpecialCharactersComments() {
        System.out.println("\n=== Test: Special Characters Comments ===");
        String specialChars = "Blanket#123!@$%^&*()";
        blanket.setComments(specialChars);
        System.out.println("Expected output: " + specialChars);
        System.out.println("Actual output: " + blanket.getComments());
        assertEquals(specialChars, blanket.getComments());
    }

    @Test
    public void testMultilineComments() {
        System.out.println("\n=== Test: Multiline Comments ===");
        String multiline = "Line1\nLine2\rLine3\r\nLine4";
        blanket.setComments(multiline);
        System.out.println("Expected output: " + multiline);
        System.out.println("Actual output: " + blanket.getComments());
        assertEquals(multiline, blanket.getComments());
    }

    @Test
    public void testWhitespaceOnlyComments() {
        System.out.println("\n=== Test: Whitespace Only Comments ===");
        String whitespace = "   ";
        blanket.setComments(whitespace);
        System.out.println("Expected output: '" + whitespace + "'");
        System.out.println("Actual output: '" + blanket.getComments() + "'");
        assertEquals(whitespace, blanket.getComments());
    }

    @Test
    public void testExtremelyLongComments() {
        System.out.println("\n=== Test: Extremely Long Comments ===");
        String longComment = "a".repeat(1000);
        blanket.setComments(longComment);
        System.out.println("Expected length: 1000");
        System.out.println("Actual length: " + blanket.getComments().length());
        assertEquals(1000, blanket.getComments().length());
    }

    @Test
    public void testNumericOnlyComments() {
        System.out.println("\n=== Test: Numeric Only Comments ===");
        String numericComment = "12345678901234567890";
        blanket.setComments(numericComment);
        System.out.println("Expected output: " + numericComment);
        System.out.println("Actual output: " + blanket.getComments());
        assertEquals(numericComment, blanket.getComments());
    }

    @Test
    public void testSetAndGetId() {
        System.out.println("\n=== Test: Set and Get ID ===");
        int testId = Integer.MAX_VALUE;
        blanket.setId(testId);
        System.out.println("Expected ID: " + testId);
        System.out.println("Actual ID: " + blanket.getId());
        assertEquals(testId, blanket.getId());
    }
}