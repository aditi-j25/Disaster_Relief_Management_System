package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FamilyGroupTest {
    private FamilyGroup familyGroup;
    private DisasterVictim member1;
    private DisasterVictim member2;
    private String maxLengthName;
    private String specialCharsName;

    @Before
    public void setUp() {
        maxLengthName = "a".repeat(255);
        specialCharsName = "O'Connor-Smith Jr.";
        familyGroup = new FamilyGroup("Smith", "Family", "403-123-4567");
        member1 = new DisasterVictim("Robert", "Smith", "403-555-1111", "Status1");
        member2 = new DisasterVictim("Mary", "Smith", "403-555-2222", "Status2");
    }

    @Test
    public void testConstructorValues() {
        System.out.println("\n=== Test: Constructor Values ===");
        System.out.println("Expected name: Smith");
        System.out.println("Actual name: " + familyGroup.getFirstName());
        assertEquals("Smith", familyGroup.getFirstName());
    }

    @Test
    public void testAddFamilyMember() {
        System.out.println("\n=== Test: Add Family Member ===");
        familyGroup.addFamilyMember(member1);
        System.out.println("Expected size: 1");
        System.out.println("Actual size: " + familyGroup.getFamilySize());
        assertEquals(1, familyGroup.getFamilySize());
    }

    @Test
    public void testRemoveFamilyMember() {
        System.out.println("\n=== Test: Remove Family Member ===");
        familyGroup.addFamilyMember(member1);
        familyGroup.removeFamilyMember(member1);
        System.out.println("Expected size: 0");
        System.out.println("Actual size: " + familyGroup.getFamilySize());
        assertEquals(0, familyGroup.getFamilySize());
    }

    @Test
    public void testMaxLengthFamilyName() {
        System.out.println("\n=== Test: Max Length Family Name ===");
        FamilyGroup maxLengthFamilyGroup = new FamilyGroup(maxLengthName, "Test", "403-123-4567");
        System.out.println("Expected length: 255");
        System.out.println("Actual length: " + maxLengthFamilyGroup.getFirstName().length());
        assertEquals(255, maxLengthFamilyGroup.getFirstName().length());
    }

    @Test
    public void testSpecialCharactersFamilyName() {
        System.out.println("\n=== Test: Special Characters in Family Name ===");
        FamilyGroup specialCharsFamilyGroup = new FamilyGroup(specialCharsName, "Test", "403-123-4567");
        System.out.println("Expected name: " + specialCharsName);
        System.out.println("Actual name: " + specialCharsFamilyGroup.getFirstName());
        assertEquals(specialCharsName, specialCharsFamilyGroup.getFirstName());
    }

    @Test
    public void testGetFamilyMembers() {
        System.out.println("\n=== Test: Get Family Members ===");
        familyGroup.addFamilyMember(member1);
        familyGroup.addFamilyMember(member2);
        System.out.println("Expected size: 2");
        System.out.println("Actual size: " + familyGroup.getFamilyMembers().size());
        assertEquals(2, familyGroup.getFamilyMembers().size());
    }

    @Test
    public void testDefensiveCopyOfMembers() {
        System.out.println("\n=== Test: Defensive Copy of Members ===");
        familyGroup.addFamilyMember(member1);
        var members = familyGroup.getFamilyMembers();
        members.clear();
        System.out.println("Expected size after clear: 1");
        System.out.println("Actual size: " + familyGroup.getFamilySize());
        assertEquals(1, familyGroup.getFamilySize());
    }

    @Test
    public void testEmptyFamilyGroup() {
        System.out.println("\n=== Test: Empty Family Group ===");
        FamilyGroup emptyGroup = new FamilyGroup("", "", "");
        System.out.println("Expected name: empty string");
        System.out.println("Actual name: '" + emptyGroup.getFirstName() + "'");
        assertEquals("", emptyGroup.getFirstName());
    }

    @Test
    public void testLargeFamilyGroup() {
        System.out.println("\n=== Test: Large Family Group ===");
        for (int i = 0; i < 1000; i++) {
            familyGroup.addFamilyMember(new DisasterVictim("Member" + i, "Family", "123-456-7890", "Status"));
        }
        System.out.println("Expected size: 1000");
        System.out.println("Actual size: " + familyGroup.getFamilySize());
        assertEquals(1000, familyGroup.getFamilySize());
    }

    @Test
    public void testAddDuplicateMember() {
        System.out.println("\n=== Test: Add Duplicate Member ===");
        familyGroup.addFamilyMember(member1);
        familyGroup.addFamilyMember(member1);
        System.out.println("Expected size: 2");
        System.out.println("Actual size: " + familyGroup.getFamilySize());
        assertEquals(2, familyGroup.getFamilySize());
    }

    @Test
    public void testExtremelyLongPhoneNumber() {
        System.out.println("\n=== Test: Extremely Long Phone Number ===");
        String longPhone = "1".repeat(50);
        FamilyGroup longPhoneGroup = new FamilyGroup("Test", "Family", longPhone);
        System.out.println("Expected phone: " + longPhone);
        System.out.println("Actual phone: " + longPhoneGroup.getPhoneNumber());
        assertEquals(longPhone, longPhoneGroup.getPhoneNumber());
    }

    @Test
    public void testRemoveNonExistentMember() {
        System.out.println("\n=== Test: Remove Non-existent Member ===");
        int initialSize = familyGroup.getFamilySize();
        familyGroup.removeFamilyMember(member1);
        System.out.println("Expected size: " + initialSize);
        System.out.println("Actual size: " + familyGroup.getFamilySize());
        assertEquals(initialSize, familyGroup.getFamilySize());
    }

    @Test
    public void testAddNullMember() {
        System.out.println("\n=== Test: Add Null Member ===");
        int initialSize = familyGroup.getFamilySize();
        familyGroup.addFamilyMember(null);
        System.out.println("Expected size: " + initialSize);
        System.out.println("Actual size: " + familyGroup.getFamilySize());
        assertEquals(initialSize, familyGroup.getFamilySize());
    }
}