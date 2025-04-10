package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocationTest {
    private Location location;
    private DisasterVictim victim;
    private Supply supply;
    private String maxLengthString;
    private String specialCharsString;
    
    @Before
    public void setUp() {
        location = new Location("Emergency Center", "123 Main Street");
        victim = new DisasterVictim("Robert", "Smith", "2023-01-01", "Earthquake");
        supply = new Supply("Medicine", 1);
        maxLengthString = "a".repeat(255);
        specialCharsString = "Test-Location#1@Emergency_Zone";
    }

    @Test
    public void testConstructorName() {
        System.out.println("\n=== Test: Constructor Name ===");
        System.out.println("Expected output: Emergency Center");
        System.out.println("Actual output: " + location.getName());
        assertEquals("Emergency Center", location.getName());
    }

    @Test
    public void testConstructorAddress() {
        System.out.println("\n=== Test: Constructor Address ===");
        System.out.println("Expected output: 123 Main Street");
        System.out.println("Actual output: " + location.getAddress());
        assertEquals("123 Main Street", location.getAddress());
    }

    @Test
    public void testSetAndGetName() {
        System.out.println("\n=== Test: Set and Get Name ===");
        location.setName("New Center");
        System.out.println("Expected output: New Center");
        System.out.println("Actual output: " + location.getName());
        assertEquals("New Center", location.getName());
    }

    @Test
    public void testAddAndGetOccupant() {
        System.out.println("\n=== Test: Add and Get Occupant ===");
        location.addOccupant(victim);
        System.out.println("Expected size: 1");
        System.out.println("Actual size: " + location.getOccupants().size());
        assertEquals(1, location.getOccupants().size());
    }

    @Test
    public void testAddAndGetSupply() {
        System.out.println("\n=== Test: Add and Get Supply ===");
        location.addSupply(supply);
        System.out.println("Expected size: 1");
        System.out.println("Actual size: " + location.getSupplies().size());
        assertEquals(1, location.getSupplies().size());
    }

    @Test
    public void testMaxLengthName() {
        System.out.println("\n=== Test: Max Length Name ===");
        Location maxLengthLocation = new Location(maxLengthString, "Test Address");
        System.out.println("Expected length: 255");
        System.out.println("Actual length: " + maxLengthLocation.getName().length());
        assertEquals(255, maxLengthLocation.getName().length());
    }

    @Test
    public void testSpecialCharactersName() {
        System.out.println("\n=== Test: Special Characters in Name ===");
        location.setName(specialCharsString);
        System.out.println("Expected output: " + specialCharsString);
        System.out.println("Actual output: " + location.getName());
        assertEquals(specialCharsString, location.getName());
    }

    @Test
    public void testEmptyStrings() {
        System.out.println("\n=== Test: Empty Strings ===");
        Location emptyLocation = new Location("", "");
        System.out.println("Expected name: empty string");
        System.out.println("Actual name: '" + emptyLocation.getName() + "'");
        assertEquals("", emptyLocation.getName());
    }

    @Test
    public void testNullOccupant() {
        System.out.println("\n=== Test: Null Occupant ===");
        location.addOccupant(null);
        System.out.println("Expected size: 0");
        System.out.println("Actual size: " + location.getOccupants().size());
        assertEquals(0, location.getOccupants().size());
    }

    @Test
    public void testRemoveOccupant() {
        System.out.println("\n=== Test: Remove Occupant ===");
        location.addOccupant(victim);
        location.removeOccupant(victim);
        System.out.println("Expected size after removal: 0");
        System.out.println("Actual size after removal: " + location.getOccupants().size());
        assertEquals(0, location.getOccupants().size());
    }

    @Test
    public void testMaxOccupants() {
        System.out.println("\n=== Test: Maximum Number of Occupants ===");
        for (int i = 0; i < 1000; i++) {
            location.addOccupant(new DisasterVictim("Test", String.valueOf(i), "2023-01-01", "Earthquake"));
        }
        System.out.println("Expected size: 1000");
        System.out.println("Actual size: " + location.getOccupants().size());
        assertEquals(1000, location.getOccupants().size());
    }

    @Test
    public void testDefensiveCopyOccupants() {
        System.out.println("\n=== Test: Defensive Copy of Occupants ===");
        location.addOccupant(victim);
        var occupants = location.getOccupants();
        occupants.clear();
        System.out.println("Expected size after clear: 1");
        System.out.println("Actual size: " + location.getOccupants().size());
        assertEquals(1, location.getOccupants().size());
    }

    @Test
    public void testDefensiveCopySupplies() {
        System.out.println("\n=== Test: Defensive Copy of Supplies ===");
        location.addSupply(supply);
        var supplies = location.getSupplies();
        supplies.clear();
        System.out.println("Expected size after clear: 1");
        System.out.println("Actual size: " + location.getSupplies().size());
        assertEquals(1, location.getSupplies().size());
    }

    @Test
    public void testExtremelyLongAddress() {
        System.out.println("\n=== Test: Extremely Long Address ===");
        String longAddress = "a".repeat(1000);
        location.setAddress(longAddress);
        System.out.println("Expected: address accepted");
        System.out.println("Actual length: " + location.getAddress().length());
        assertEquals(longAddress, location.getAddress());
    }

    @Test
    public void testSpecialCharactersAddress() {
        System.out.println("\n=== Test: Special Characters in Address ===");
        String specialAddress = "123/A-B, Floor#4, Zone@2!";
        location.setAddress(specialAddress);
        System.out.println("Expected: " + specialAddress);
        System.out.println("Actual: " + location.getAddress());
        assertEquals(specialAddress, location.getAddress());
    }

    @Test
    public void testDuplicateOccupant() {
        System.out.println("\n=== Test: Add Duplicate Occupant ===");
        location.addOccupant(victim);
        location.addOccupant(victim);
        System.out.println("Expected size: 2");
        System.out.println("Actual size: " + location.getOccupants().size());
        assertEquals(2, location.getOccupants().size());
    }
}
