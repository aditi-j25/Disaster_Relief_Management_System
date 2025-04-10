package edu.ucalgary.oop;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.sql.*;
import java.time.LocalDateTime;

public class WaterTest {
    private Water water;
    private MockDatabaseConnection db;
    private LocalDateTime currentTime;
    private LocalDateTime expiredTime;
    private LocalDateTime almostExpiredTime;
    private LocalDateTime exactlyExpiredTime;

    @Before
    public void setUp() throws SQLException {
        db = MockDatabaseConnectionImpl.getInstance();
        currentTime = LocalDateTime.now();
        expiredTime = currentTime.minusDays(2);
        almostExpiredTime = currentTime.minusHours(23);
        exactlyExpiredTime = currentTime.minusHours(25);

        water = new Water();
        water.setId(1);
    }

    @Test
    public void testConstructor() {
        System.out.println("Running: testConstructor");
        System.out.println("Expected: water");
        System.out.println("Actual: " + water.getType());
        assertEquals("water", water.getType());
    }

    @Test
    public void testAllocationDateSetterGetter() {
        System.out.println("Running: testAllocationDateSetterGetter");
        LocalDateTime testTime = LocalDateTime.now();
        water.setAllocationDate(testTime);
        System.out.println("Expected: " + testTime);
        System.out.println("Actual: " + water.getAllocationDate());
        assertEquals(testTime, water.getAllocationDate());
    }

    @Test
    public void testIsExpired_NotAllocated() {
        System.out.println("Running: testIsExpired_NotAllocated");
        System.out.println("Expected: false");
        System.out.println("Actual: " + water.isExpired());
        assertFalse(water.isExpired());
    }

    @Test
    public void testIsExpired_Fresh() {
        System.out.println("Running: testIsExpired_Fresh");
        water.setAllocationDate(currentTime);
        System.out.println("Expected: false");
        System.out.println("Actual: " + water.isExpired());
        assertFalse(water.isExpired());
    }

    @Test
    public void testIsExpired_Expired() {
        System.out.println("Running: testIsExpired_Expired");
        water.setAllocationDate(expiredTime);
        System.out.println("Expected: true");
        System.out.println("Actual: " + water.isExpired());
        assertTrue(water.isExpired());
    }

    @Test
    public void testIsExpired_AlmostExpired() {
        System.out.println("Running: testIsExpired_AlmostExpired");
        water.setAllocationDate(almostExpiredTime);
        System.out.println("Expected: false");
        System.out.println("Actual: " + water.isExpired());
        assertFalse(water.isExpired());
    }

    @Test
    public void testIsExpired_ExactlyExpired() {
        System.out.println("Running: testIsExpired_ExactlyExpired");
        water.setAllocationDate(exactlyExpiredTime);
        System.out.println("Expected: true");
        System.out.println("Actual: " + water.isExpired());
        assertTrue(water.isExpired());
    }

    @Test(expected = SQLException.class)
    public void testAllocateToPersonWithExpiry_InvalidPerson() throws SQLException {
        System.out.println("Running: testAllocateToPersonWithExpiry_InvalidPerson");
        System.out.println("Expected: SQLException");
        water.allocateToPersonWithExpiry(999, db);
    }

    @Test(expected = SQLException.class)
    public void testAllocateToPersonWithExpiry_NoWaterId() throws SQLException {
        System.out.println("Running: testAllocateToPersonWithExpiry_NoWaterId");
        System.out.println("Expected: SQLException");
        Water newWater = new Water();
        newWater.allocateToPersonWithExpiry(1, db);
    }

    @Test(expected = SQLException.class)
    public void testAllocateToLocation_InvalidLocation() throws SQLException {
        System.out.println("Running: testAllocateToLocation_InvalidLocation");
        System.out.println("Expected: SQLException");
        water.allocateToLocation(999, db);
    }

    @Test(expected = SQLException.class)
    public void testAllocateToLocation_NoWaterId() throws SQLException {
        System.out.println("Running: testAllocateToLocation_NoWaterId");
        System.out.println("Expected: SQLException");
        Water newWater = new Water();
        newWater.allocateToLocation(1, db);
    }
}