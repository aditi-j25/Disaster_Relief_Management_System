package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CotTest {
    private Cot cot;
    private MockDatabaseConnection dbConnector;
    private UserInterfaceImplied ui;
    private String standardLocation;
    private String maxLengthLocation;
    private String specialCharsLocation;
    
    @Before
    public void setUp() {
        dbConnector = MockDatabaseConnectionImpl.getInstance();
        dbConnector.reset();
        dbConnector.populateTestData();
        ui = new UserInterfaceImplied(new LanguageSupport("en-CA"));
        ui.setDatabaseConnection(dbConnector);
        
        standardLocation = "410 G16";
        maxLengthLocation = "a".repeat(255);
        specialCharsLocation = "Room-101#A5@Wing-2";
        cot = new Cot(standardLocation);
    }

    @Test
    public void testConstructorType() {
        System.out.println("\n=== Test: Constructor Type ===");
        System.out.println("Expected output: cot");
        System.out.println("Actual output: " + cot.getType());
        assertEquals("cot", cot.getType());
    }

    @Test
    public void testConstructorLocation() {
        System.out.println("\n=== Test: Constructor Location ===");
        System.out.println("Expected output: " + standardLocation);
        System.out.println("Actual output: " + cot.getComments());
        assertEquals(standardLocation, cot.getComments());
    }

    @Test
    public void testMaxLengthLocation() {
        System.out.println("\n=== Test: Max Length Location ===");
        Cot maxLengthCot = new Cot(maxLengthLocation);
        System.out.println("Expected length: 255");
        System.out.println("Actual length: " + maxLengthCot.getComments().length());
        assertEquals(255, maxLengthCot.getComments().length());
    }

    @Test
    public void testSpecialCharactersLocation() {
        System.out.println("\n=== Test: Special Characters Location ===");
        Cot specialCharsCot = new Cot(specialCharsLocation);
        System.out.println("Expected output: " + specialCharsLocation);
        System.out.println("Actual output: " + specialCharsCot.getComments());
        assertEquals(specialCharsLocation, specialCharsCot.getComments());
    }

    @Test
    public void testEmptyLocation() {
        System.out.println("\n=== Test: Empty Location ===");
        Cot emptyCot = new Cot("");
        System.out.println("Expected output: empty string");
        System.out.println("Actual output: '" + emptyCot.getComments() + "'");
        assertEquals("", emptyCot.getComments());
    }

    @Test
    public void testNullLocation() {
        System.out.println("\n=== Test: Null Location ===");
        Cot nullCot = new Cot(null);
        System.out.println("Expected output: null");
        System.out.println("Actual output: " + nullCot.getComments());
        assertNull(nullCot.getComments());
    }

    @Test
    public void testMultilineLocation() {
        System.out.println("\n=== Test: Multiline Location ===");
        String multiline = "Floor1\nRoom2\rBed3\r\nSection4";
        Cot multilineCot = new Cot(multiline);
        System.out.println("Expected output: " + multiline);
        System.out.println("Actual output: " + multilineCot.getComments());
        assertEquals(multiline, multilineCot.getComments());
    }

    @Test
    public void testWhitespaceOnlyLocation() {
        System.out.println("\n=== Test: Whitespace Only Location ===");
        String whitespace = "   ";
        Cot whitespaceCot = new Cot(whitespace);
        System.out.println("Expected output: '" + whitespace + "'");
        System.out.println("Actual output: '" + whitespaceCot.getComments() + "'");
        assertEquals(whitespace, whitespaceCot.getComments());
    }

    @Test
    public void testExtremelyLongLocation() {
        System.out.println("\n=== Test: Extremely Long Location ===");
        String longLocation = "a".repeat(1000);
        Cot longCot = new Cot(longLocation);
        System.out.println("Expected output length: 1000");
        System.out.println("Actual output length: " + longCot.getComments().length());
        assertEquals(1000, longCot.getComments().length());
    }

    @Test
    public void testNumericLocation() {
        System.out.println("\n=== Test: Numeric Location ===");
        String numericLoc = "123456789";
        Cot numericCot = new Cot(numericLoc);
        System.out.println("Expected output: " + numericLoc);
        System.out.println("Actual output: " + numericCot.getComments());
        assertEquals(numericLoc, numericCot.getComments());
    }

    @Test
    public void testUnicodeCharactersLocation() {
        System.out.println("\n=== Test: Unicode Characters Location ===");
        String unicodeLoc = "Room-A⌘";
        Cot unicodeCot = new Cot(unicodeLoc);
        System.out.println("Expected output: " + unicodeLoc);
        System.out.println("Actual output: " + unicodeCot.getComments());
        assertEquals(unicodeLoc, unicodeCot.getComments());
    }
}
