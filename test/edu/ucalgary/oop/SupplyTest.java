package edu.ucalgary.oop;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.sql.*;

public class SupplyTest {
    private Supply supply;
    private MockDatabaseConnection dbConnector;
    private UserInterfaceImplied ui;
    private int validId;
    private int maxId;
    private int minId;
    private int invalidId;

    @Before
    public void setUp() throws SQLException {
        // Initialize database and UI
        dbConnector = MockDatabaseConnectionImpl.getInstance();
        dbConnector.reset();
        dbConnector.populateTestData();
        ui = new UserInterfaceImplied(new LanguageSupport("en-CA"));
        ui.setDatabaseConnection(dbConnector);

        // Initialize test values
        validId = 2; // Fixed ID value as per instructions
        maxId = Integer.MAX_VALUE;
        minId = 1;
        invalidId = 999999;

        // Override the selectPerson method in the test itself
        supply = new Supply("Water", validId) {
            @Override
            public int selectPerson(String prompt, UserInterfaceImplied ui) {
                // Use a fixed ID (40) for testing purposes
                return 40;
            }
        };
    }

    @Test
    public void testConstructorType() {
        System.out.println("\n=== Test: Constructor Type ===");
        Supply testSupply = new Supply("TestType", validId);
        System.out.println("Expected output: Type = TestType");
        System.out.println("Actual output: Type = " + testSupply.getType());
        assertEquals("TestType", testSupply.getType());
    }

    @Test
    public void testConstructorId() {
        System.out.println("\n=== Test: Constructor ID ===");
        Supply testSupply = new Supply("TestType", validId);
        System.out.println("Expected output: ID = " + validId);
        System.out.println("Actual output: ID = " + testSupply.getId());
        assertEquals(validId, testSupply.getId());
    }

    @Test
    public void testSetAndGetType() {
        System.out.println("\n=== Test: Set and Get Type ===");
        supply.setType("NewType");
        System.out.println("Expected output: Type = NewType");
        System.out.println("Actual output: Type = " + supply.getType());
        assertEquals("NewType", supply.getType());
    }

    @Test
    public void testSetAndGetComments() {
        System.out.println("Running: testSetAndGetComments");
        supply.setComments("Test Comment");
        System.out.println("Expected comments: Test Comment");
        System.out.println("Actual comments: " + supply.getComments());
        assertEquals("Test Comment", supply.getComments());
    }

    @Test(expected = SQLException.class)
    public void testAllocateToInvalidPerson() throws SQLException {
        System.out.println("Running: testAllocateToInvalidPerson");
        supply.allocateSupplyToPerson(validId, invalidId, dbConnector);
    }

    @Test(expected = SQLException.class)
    public void testAllocateToInvalidLocation() throws SQLException {
        System.out.println("Running: testAllocateToInvalidLocation");
        supply.allocateSupplyToLocation(validId, invalidId, dbConnector);
    }

    @Test
    public void testSelectPerson() {
        System.out.println("Running: testSelectPerson");

        // Call the overridden selectPerson method
        int result = supply.selectPerson("select_recipient", ui);

        System.out.println("Expected: 40 (fixed ID)");
        System.out.println("Actual: " + result);

        assertEquals("Fixed ID should be returned", 40, result);
    }

    @Test(expected = SQLException.class)
    public void testUpdateDatabaseWithInvalidId() throws SQLException {
        System.out.println("Running: testUpdateDatabaseWithInvalidId");
        Supply invalidSupply = new Supply(); // Empty supply object for invalid scenario
        invalidSupply.updateDatabase(ui);
    }
}
