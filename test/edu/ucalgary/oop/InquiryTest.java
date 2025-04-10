package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class InquiryTest {
    private Inquiry inquiry;
    private Inquirer inquirer;
    private MockDatabaseConnection dbConnector;
    private UserInterfaceImplied ui;
    private String testMessage;
    private String maxLengthMessage;
    private String specialCharsMessage;
    
    @Before
    public void setUp() {
        dbConnector = MockDatabaseConnectionImpl.getInstance();
        dbConnector.reset();
        dbConnector.populateTestData();
        ui = new UserInterfaceImplied(new LanguageSupport("en-CA"));
        ui.setDatabaseConnection(dbConnector);
        
        inquirer = new Inquirer("Robert", "Smith", "403-123-4567", "Test Comments", false);
        testMessage = "Test inquiry message";
        maxLengthMessage = "a".repeat(255);
        specialCharsMessage = "Test-Message#1@Emergency_Zone!";
        
        inquiry = new Inquiry(inquirer, testMessage);
    }

    @Test
    public void testConstructorMessage() {
        System.out.println("\n=== Test: Constructor Message ===");
        System.out.println("Expected output: " + testMessage);
        System.out.println("Actual output: " + inquiry.getInquiryMessage());
        assertEquals(testMessage, inquiry.getInquiryMessage());
    }

    @Test
    public void testSetAndGetId() {
        System.out.println("\n=== Test: Set and Get ID ===");
        inquiry.setId(1);
        System.out.println("Expected output: 1");
        System.out.println("Actual output: " + inquiry.getId());
        assertEquals(1, inquiry.getId());
    }

    @Test
    public void testSetAndGetSeekingId() {
        System.out.println("\n=== Test: Set and Get Seeking ID ===");
        inquiry.setSeekingId(2);
        System.out.println("Expected output: 2");
        System.out.println("Actual output: " + inquiry.getSeekingId());
        assertEquals(2, inquiry.getSeekingId());
    }

    @Test
    public void testMaxLengthMessage() {
        System.out.println("\n=== Test: Max Length Message ===");
        Inquiry maxInquiry = new Inquiry(inquirer, maxLengthMessage);
        System.out.println("Expected length: 255");
        System.out.println("Actual length: " + maxInquiry.getInquiryMessage().length());
        assertEquals(255, maxInquiry.getInquiryMessage().length());
    }

    @Test
    public void testSpecialCharactersMessage() {
        System.out.println("\n=== Test: Special Characters Message ===");
        Inquiry specialInquiry = new Inquiry(inquirer, specialCharsMessage);
        System.out.println("Expected output: " + specialCharsMessage);
        System.out.println("Actual output: " + specialInquiry.getInquiryMessage());
        assertEquals(specialCharsMessage, specialInquiry.getInquiryMessage());
    }

    @Test(expected = NullPointerException.class)
    public void testNullInquirer() {
        System.out.println("\n=== Test: Null Inquirer ===");
        System.out.println("Expected: NullPointerException");
        new Inquiry(null, testMessage);
    }

    @Test
    public void testToString() {
        System.out.println("\n=== Test: ToString ===");
        String expectedString = "Inquiry{inquirer=" + inquirer + 
                              ", inquiryMessage='" + testMessage + 
                              "', seekingId=0}";
        System.out.println("Expected output: " + expectedString);
        System.out.println("Actual output: " + inquiry.toString());
        assertEquals(expectedString, inquiry.toString());
    }

    @Test
    public void testSetIdMaxValue() {
        System.out.println("\n=== Test: Set ID Maximum Value ===");
        inquiry.setId(Integer.MAX_VALUE);
        System.out.println("Expected output: " + Integer.MAX_VALUE);
        System.out.println("Actual output: " + inquiry.getId());
        assertEquals(Integer.MAX_VALUE, inquiry.getId());
    }

    @Test
    public void testSetIdMinValue() {
        System.out.println("\n=== Test: Set ID Minimum Value ===");
        inquiry.setId(Integer.MIN_VALUE);
        System.out.println("Expected output: " + Integer.MIN_VALUE);
        System.out.println("Actual output: " + inquiry.getId());
        assertEquals(Integer.MIN_VALUE, inquiry.getId());
    }

    @Test
    public void testEmptyMessage() {
        System.out.println("\n=== Test: Empty Message ===");
        Inquiry emptyInquiry = new Inquiry(inquirer, "");
        System.out.println("Expected output: empty string");
        System.out.println("Actual output: '" + emptyInquiry.getInquiryMessage() + "'");
        assertEquals("", emptyInquiry.getInquiryMessage());
    }

    @Test(expected = NullPointerException.class)
    public void testNullMessage() {
        System.out.println("\n=== Test: Null Message ===");
        System.out.println("Expected: NullPointerException");
        new Inquiry(inquirer, null);
    }

    @Test
    public void testExtremelyLongMessage() {
        System.out.println("\n=== Test: Extremely Long Message ===");
        String longMessage = "a".repeat(1000);
        Inquiry longInquiry = new Inquiry(inquirer, longMessage);
        System.out.println("Expected: message accepted");
        System.out.println("Actual length: " + longInquiry.getInquiryMessage().length());
        assertEquals(1000, longInquiry.getInquiryMessage().length());
    }

    @Test
    public void testWhitespaceOnlyMessage() {
        System.out.println("\n=== Test: Whitespace Only Message ===");
        String whitespaceMessage = "   ";
        Inquiry whitespaceInquiry = new Inquiry(inquirer, whitespaceMessage);
        System.out.println("Expected output: '   '");
        System.out.println("Actual output: '" + whitespaceInquiry.getInquiryMessage() + "'");
        assertEquals(whitespaceMessage, whitespaceInquiry.getInquiryMessage());
    }
}
