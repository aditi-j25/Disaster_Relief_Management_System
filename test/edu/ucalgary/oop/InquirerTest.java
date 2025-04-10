package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class InquirerTest {
    private Inquirer inquirer;
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
        inquirer = new Inquirer("Robert", "Smith", "403-123-4567", "Test Message", false);
    }

    @Test
    public void testConstructorValues() {
        System.out.println("\n=== Test: Constructor Values ===");
        System.out.println("Expected name: Robert Smith");
        System.out.println("Actual name: " + inquirer.getFullName());
        assertEquals("Robert Smith", inquirer.getFullName());
    }

    @Test
    public void testSetAndGetInquiryMessage() {
        System.out.println("\n=== Test: Set and Get Inquiry Message ===");
        String message = "New test message";
        inquirer.setInquiryMessage(message);
        System.out.println("Expected message: " + message);
        System.out.println("Actual message: " + inquirer.getInquiryMessage());
        assertEquals(message, inquirer.getInquiryMessage());
    }

    @Test
    public void testMaxLengthName() {
        System.out.println("\n=== Test: Max Length Name ===");
        inquirer.setFirstName(maxLengthString);
        System.out.println("Expected length: 255");
        System.out.println("Actual length: " + inquirer.getFirstName().length());
        assertEquals(255, inquirer.getFirstName().length());
    }

    @Test
    public void testSpecialCharactersName() {
        System.out.println("\n=== Test: Special Characters Name ===");
        inquirer.setFirstName(specialCharsString);
        System.out.println("Expected: " + specialCharsString);
        System.out.println("Actual: " + inquirer.getFirstName());
        assertEquals(specialCharsString, inquirer.getFirstName());
    }

    @Test
    public void testSetAndGetVictimStatus() {
        System.out.println("\n=== Test: Set and Get Victim Status ===");
        inquirer.setVictim(true);
        System.out.println("Expected: true");
        System.out.println("Actual: " + inquirer.isVictim());
        assertTrue(inquirer.isVictim());
    }

    @Test
    public void testEmptyInquiryMessage() {
        System.out.println("\n=== Test: Empty Inquiry Message ===");
        inquirer.setInquiryMessage("");
        System.out.println("Expected: empty string");
        System.out.println("Actual: '" + inquirer.getInquiryMessage() + "'");
        assertEquals("", inquirer.getInquiryMessage());
    }

    @Test
    public void testNullInquiryMessage() {
        System.out.println("\n=== Test: Null Inquiry Message ===");
        inquirer.setInquiryMessage(null);
        System.out.println("Expected: null");
        System.out.println("Actual: " + inquirer.getInquiryMessage());
        assertNull(inquirer.getInquiryMessage());
    }

    @Test
    public void testToString() {
        System.out.println("\n=== Test: ToString ===");
        String expected = String.format("Inquirer [ID=%d, Name=%s %s, Phone=%s, Message=%s, IsVictim=%b]",
            inquirer.getId(), "Robert", "Smith", "403-123-4567", "Test Message", false);
        System.out.println("Expected: " + expected);
        System.out.println("Actual: " + inquirer.toString());
        assertEquals(expected, inquirer.toString());
    }

    @Test
    public void testExtremelyLongInquiryMessage() {
        System.out.println("\n=== Test: Extremely Long Inquiry Message ===");
        String longMessage = "a".repeat(1000);
        inquirer.setInquiryMessage(longMessage);
        System.out.println("Expected length: 1000");
        System.out.println("Actual length: " + inquirer.getInquiryMessage().length());
        assertEquals(1000, inquirer.getInquiryMessage().length());
    }

    @Test
    public void testWhitespaceOnlyMessage() {
        System.out.println("\n=== Test: Whitespace Only Message ===");
        String whitespaceMessage = "    ";
        inquirer.setInquiryMessage(whitespaceMessage);
        System.out.println("Expected: '" + whitespaceMessage + "'");
        System.out.println("Actual: '" + inquirer.getInquiryMessage() + "'");
        assertEquals(whitespaceMessage, inquirer.getInquiryMessage());
    }

    @Test
    public void testNewlineInMessage() {
        System.out.println("\n=== Test: Newline in Message ===");
        String multilineMessage = "Line1\nLine2\rLine3\r\nLine4";
        inquirer.setInquiryMessage(multilineMessage);
        System.out.println("Expected: " + multilineMessage);
        System.out.println("Actual: " + inquirer.getInquiryMessage());
        assertEquals(multilineMessage, inquirer.getInquiryMessage());
    }

    @Test
    public void testSpecialCharactersInPhoneNumber() {
        System.out.println("\n=== Test: Special Characters in Phone Number ===");
        String specialPhone = "+1-(403)-123-4567";
        inquirer.setPhoneNumber(specialPhone);
        System.out.println("Expected: " + specialPhone);
        System.out.println("Actual: " + inquirer.getPhoneNumber());
        assertEquals(specialPhone, inquirer.getPhoneNumber());
    }

    @Test
    public void testConstructorWithEmptyStrings() {
        System.out.println("\n=== Test: Constructor with Empty Strings ===");
        Inquirer emptyInquirer = new Inquirer("", "", "", "", false);
        System.out.println("Expected name: ' '");
        System.out.println("Actual name: '" + emptyInquirer.getFullName() + "'");
        assertEquals(" ", emptyInquirer.getFullName());
    }
}
