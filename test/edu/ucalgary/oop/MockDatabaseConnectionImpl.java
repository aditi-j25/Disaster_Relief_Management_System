package edu.ucalgary.oop;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Mock implementation of the DatabaseConnector interface for testing purposes.
 * <p>
 * This class provides a testing environment without requiring a real database connection.
 * It maintains in-memory data structures for all entity types and simulates database operations.
 * </p>
 * 
 * @author 30208786
 * @version 3.0
 * @since 2025-04-09
 */
public class MockDatabaseConnectionImpl implements MockDatabaseConnection {
    private static MockDatabaseConnectionImpl instance;
    private boolean isClosed = false;
    
    private Map<Integer, Person> persons = new HashMap<>();
    private Map<Integer, Location> locations = new HashMap<>();
    private Map<Integer, Supply> supplies = new HashMap<>();
    private Map<Integer, Inquiry> inquiries = new HashMap<>();
    private Map<Integer, MedicalRecord> medicalRecords = new HashMap<>();
    private Map<Integer, FamilyGroup> familyGroups = new HashMap<>();
    
    /**
     * Private constructor that initializes the mock database with test data.
     */
    private MockDatabaseConnectionImpl() {
        populateTestData();
    }
    
    /**
     * Retrieves the singleton instance of the {@code MockDatabaseConnectionImpl}.
     *
     * @return The active {@code MockDatabaseConnectionImpl} instance.
     */
    public static synchronized MockDatabaseConnectionImpl getInstance() {
        if (instance == null || instance.isClosed) {
            instance = new MockDatabaseConnectionImpl();
        }
        return instance;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Connection getConnection() throws SQLException {
        // This is a mock, so we don't return a real connection
        return null;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void closeConnection() {
        isClosed = true;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() {
        persons.clear();
        locations.clear();
        supplies.clear();
        inquiries.clear();
        medicalRecords.clear();
        familyGroups.clear();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void populateTestData() {
        reset();
        
        // Create test persons
        DisasterVictim victim1 = new DisasterVictim("John", "Doe", "555-1234", "Unknown");
        victim1.setId(1);
        victim1.setGender("Male");
        persons.put(1, victim1);
        
        DisasterVictim victim2 = new DisasterVictim("Jane", "Smith", "555-5678", "Unknown");
        victim2.setId(2);
        victim2.setGender("Female");
        persons.put(2, victim2);
        
        Inquirer inquirer1 = new Inquirer("Bob", "Johnson", "555-9012", "", true);
        inquirer1.setId(3);
        inquirer1.setGender("Male");
        persons.put(3, inquirer1);
        
        // Create test locations
        Location shelter1 = new Location("Main Shelter", "123 Main St");
        locations.put(1, shelter1);
        
        Location hospital = new Location("City Hospital", "456 Medical Dr");
        locations.put(2, hospital);
        
        // Create test supplies
        Cot cot1 = new Cot("Standard issue");
        cot1.setId(1);
        supplies.put(1, cot1);
        
        Blanket blanket1 = new Blanket();
        blanket1.setId(2);
        supplies.put(2, blanket1);
        
        Water water1 = new Water();
        water1.setId(3);
        supplies.put(3, water1);
        
        // Create test inquiries
        Inquiry inquiry1 = new Inquiry(inquirer1, "Looking for family member");
        inquiry1.setId(1);
        inquiry1.setSeekingId(1); // Looking for John
        inquiries.put(1, inquiry1);
        
        // Create test medical records
        MedicalRecord record1 = new MedicalRecord(hospital, "First aid treatment", "2025-04-01");
        medicalRecords.put(1, record1);
        
        // Create test family groups
        FamilyGroup family1 = new FamilyGroup("Family", "1", null);
        family1.addFamilyMember(victim1);
        family1.addFamilyMember(victim2);
        familyGroups.put(1, family1);
        
        // Link victims to locations
        shelter1.addOccupant(victim1);
        hospital.addOccupant(victim2);
        
        // Link supplies to locations
        shelter1.addSupply(cot1);
        shelter1.addSupply(blanket1);
        hospital.addSupply(water1);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void loadData() {
        // For the mock, loading data just means making sure test data is populated
        if (persons.isEmpty()) {
            populateTestData();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void simulateFailure(String failureType) {
        switch (failureType.toLowerCase()) {
            case "connection":
                isClosed = true;
                break;
            case "data":
                reset();
                break;
            case "partial":
                persons.clear();
                break;
            case "exception":
                throw new RuntimeException("Simulated database failure");
            default:
                break;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validateDataConsistency() {
        boolean isConsistent = true;
        
        // Check person-location consistency
        for (Location location : locations.values()) {
            for (DisasterVictim occupant : location.getOccupants()) {
                if (!persons.containsValue(occupant)) {
                    isConsistent = false;
                    break;
                }
            }
        }
        
        // Check family group consistency
        for (FamilyGroup group : familyGroups.values()) {
            for (Person member : group.getFamilyMembers()) {
                if (!persons.containsValue(member)) {
                    isConsistent = false;
                    break;
                }
            }
        }
        
        // Check inquiry consistency
        for (Inquiry inquiry : inquiries.values()) {
            if (inquiry.getInquirer() != null && !persons.containsValue(inquiry.getInquirer())) {
                isConsistent = false;
                break;
            }
            
            if (inquiry.getSeekingId() > 0 && !persons.containsKey(inquiry.getSeekingId())) {
                isConsistent = false;
                break;
            }
        }
        
        return isConsistent;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void cleanupExpiredWater() {
        // Mock implementation of water cleanup
        supplies.entrySet().removeIf(entry -> 
            entry.getValue() instanceof Water && ((Water) entry.getValue()).isExpired());
    }
    
    /**
     * {@inheritDoc}
     */
    public static void logError(String message, Exception e) {
        try (FileWriter writer = new FileWriter("data/mock_errorlog.txt", true)) {
            writer.write(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    + " - MOCK - " + message + " - " + e.getMessage() + "\n");
        } catch (IOException ioException) {
            System.err.println("Mock error logging failed: " + ioException.getMessage());
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Integer, Person> getPersons() {
        return new HashMap<>(persons);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Integer, Location> getLocations() {
        return new HashMap<>(locations);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Integer, Supply> getSupplies() {
        return new HashMap<>(supplies);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Integer, Inquiry> getInquiries() {
        return new HashMap<>(inquiries);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Integer, MedicalRecord> getMedicalRecords() {
        return new HashMap<>(medicalRecords);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Integer, FamilyGroup> getFamilyGroups() {
        return new HashMap<>(familyGroups);
    }
}