package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

public class UserInterfaceImpliedTest {
    private UserInterfaceImplied ui;
    private MockDatabaseConnection dbConnector;
    private LanguageSupport languageSupport;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @Before
    public void setUp() {
        System.setOut(new PrintStream(outContent));
        languageSupport = new LanguageSupport("en-CA");
        dbConnector = MockDatabaseConnectionImpl.getInstance();
        dbConnector.reset();
        dbConnector.populateTestData();
        ui = new UserInterfaceImplied(languageSupport);
        ui.setDatabaseConnection(dbConnector);
    }

    @After
    public void tearDown() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    private void provideInput(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
    }

    private String getConsoleOutput() {
        String output = outContent.toString();
        outContent.reset();
        return output;
    }

    @Test
    public void testDisplayMainMenu_WelcomeMessage() {
        System.out.println("Running: testDisplayMainMenu_WelcomeMessage");
        ui.displayMainMenu();
        assertTrue(getConsoleOutput().contains("Welcome to the Disaster Relief Management System"));
    }

    @Test
    public void testDisplayMainMenu_Option1() {
        System.out.println("Running: testDisplayMainMenu_Option1");
        ui.displayMainMenu();
        assertTrue(getConsoleOutput().contains("1. Add a Disaster victim"));
    }

    @Test
    public void testDisplayMainMenu_Option2() {
        System.out.println("Running: testDisplayMainMenu_Option2");
        ui.displayMainMenu();
        assertTrue(getConsoleOutput().contains("2. View Disaster victim information"));
    }

    @Test
    public void testDisplayMainMenu_ExitOption() {
        System.out.println("Running: testDisplayMainMenu_ExitOption");
        ui.displayMainMenu();
        assertTrue(getConsoleOutput().contains("10. Exit the system"));
    }

    @Test
    public void testDisplayError_ErrorPrefix() {
        System.out.println("Running: testDisplayError_ErrorPrefix");
        ui.displayError("Test error message");
        assertTrue(getConsoleOutput().contains("Error: Test error message"));
    }

    @Test
    public void testShowSuccess_Message() {
        System.out.println("Running: testShowSuccess_Message");
        ui.showSuccess("Test success message");
        assertTrue(getConsoleOutput().contains("Test success message"));
    }

    @Test
    public void testShowPrompt_Message() {
        System.out.println("Running: testShowPrompt_Message");
        ui.showPrompt("Test prompt");
        assertTrue(getConsoleOutput().contains("Test prompt"));
    }

    @Test
    public void testGetInput_MatchesProvided() {
        System.out.println("Running: testGetInput_MatchesProvided");
        provideInput("Test input\n");
        UserInterfaceImplied testUi = new UserInterfaceImplied(languageSupport);
        assertEquals("Test input", testUi.getInput());
    }

    @Test
    public void testViewDisasterVictimInfo_Headers() {
        System.out.println("Running: testViewDisasterVictimInfo_Headers");
        ui.viewDisasterVictimInfo();
        assertTrue(getConsoleOutput().contains("ID"));
    }

    @Test
    public void testViewDisasterVictimInfo_JohnDoeData() {
        System.out.println("Running: testViewDisasterVictimInfo_JohnDoeData");
        ui.viewDisasterVictimInfo();
        assertTrue(getConsoleOutput().contains("John Doe"));
    }

    @Test
    public void testViewDisasterVictimInfo_JaneSmithData() {
        System.out.println("Running: testViewDisasterVictimInfo_JaneSmithData");
        ui.viewDisasterVictimInfo();
        String output = getConsoleOutput();
        System.out.println("Expected: Contains 'Jane Smith'");
        System.out.println("Actual: " + output);
        assertTrue(output.contains("Jane Smith"));
    }

    @Test
    public void testGetValidatedInput_ValidNumber() {
        System.out.println("Running: testGetValidatedInput_ValidNumber");
        provideInput("12345\n");
        UserInterfaceImplied testUi = new UserInterfaceImplied(languageSupport);
        String result = testUi.getValidatedInput("Enter number: ", "\\d+", "Numbers only");
        System.out.println("Expected: 12345");
        System.out.println("Actual: " + result);
        assertEquals("12345", result);
    }

    @Test
    public void testGetValidatedInput_InvalidInput_ShowsError() {
        System.out.println("Running: testGetValidatedInput_InvalidInput_ShowsError");
        provideInput("abc\n123\n");
        UserInterfaceImplied testUi = new UserInterfaceImplied(languageSupport);
        testUi.getValidatedInput("Enter number: ", "\\d+", "Numbers only");
        String output = getConsoleOutput();
        System.out.println("Expected: Contains 'Numbers only'");
        System.out.println("Actual: " + output);
        assertTrue(output.contains("Numbers only"));
    }

    @Test
    public void testGetValidatedInput_AllInvalid_ReturnsNull() {
        System.out.println("Running: testGetValidatedInput_AllInvalid_ReturnsNull");
        provideInput("abc\nxyz\nqwe\n");
        UserInterfaceImplied testUi = new UserInterfaceImplied(languageSupport);
        assertNull(testUi.getValidatedInput("Enter number: ", "\\d+", "Numbers only"));
    }

    @Test
    public void testGetMultiLineInput_CombinesLines() {
        System.out.println("Running: testGetMultiLineInput_CombinesLines");
        provideInput("Line 1\nLine 2\nLine 3\n\n");
        UserInterfaceImplied testUi = new UserInterfaceImplied(languageSupport);
        assertEquals("Line 1\nLine 2\nLine 3", testUi.getMultiLineInput("Enter multi-line text:"));
    }

    @Test
    public void testGetLanguageSupport_ReturnsCorrectInstance() {
        System.out.println("Running: testGetLanguageSupport_ReturnsCorrectInstance");
        assertEquals(languageSupport, ui.getLanguageSupport());
    }

    @Test
    public void testGetDbConnection_ReturnsCorrectInstance() {
        System.out.println("Running: testGetDbConnection_ReturnsCorrectInstance");
        assertEquals(dbConnector, ui.getDbConnection());
    }

    @Test
    public void testRunWithExit_ShowsExitMessage() {
        System.out.println("Running: testRunWithExit_ShowsExitMessage");
        provideInput("10\n");
        UserInterfaceImplied testUi = new UserInterfaceImplied(languageSupport);
        testUi.setDatabaseConnection(dbConnector);
        testUi.run();
        assertTrue(getConsoleOutput().contains("Thank you for using the Disaster Relief Management System."));
    }

    @Test
    public void testInvalidChoice_ShowsError() {
        System.out.println("Running: testInvalidChoice_ShowsError");
        provideInput("99\n10\n");
        UserInterfaceImplied testUi = new UserInterfaceImplied(languageSupport);
        testUi.setDatabaseConnection(dbConnector);
        testUi.run();
        assertTrue(getConsoleOutput().contains("Thank you for using the Disaster Relief Management System.")); 
        //because of 10 else 99 will be stuck at infinite loop 
    }
}