package edu.ucalgary.oop;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Singleton class implementing RealDatabaseConnection for managing the database connection
 * and loading entity data for the Disaster Relief Management System.
 * <p>
 * This class connects to a PostgreSQL database, retrieves data from all relevant tables,
 * maps it to corresponding Java objects, and stores them in memory for application use.
 * It also handles cleanup tasks (e.g., expired water) and logs database-related errors.
 * </p>
 * 
 * @author 30208786
 * @version 6.0
 * @since 2025-04-09
 */
public class DatabaseConnection implements RealDatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    private static final String URL = "jdbc:postgresql://localhost:5432/ensf380project";
    private static final String USER = "oop";
    private static final String PASSWORD = "ucalgary";
    private boolean isClosed = false;

    private Map<Integer, Person> persons = new HashMap<>();
    private Map<Integer, Location> locations = new HashMap<>();
    private Map<Integer, Supply> supplies = new HashMap<>();
    private Map<Integer, Inquiry> inquiries = new HashMap<>();
    private Map<Integer, MedicalRecord> medicalRecords = new HashMap<>();
    private Map<Integer, FamilyGroup> familyGroups = new HashMap<>();

    /**
     * Private constructor that establishes a connection to the database 
     * and loads data from all required tables.
     */
    private DatabaseConnection() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            loadData();
        } catch (SQLException e) {
            logError("Database connection failed", e);
            throw new RuntimeException("Failed to establish database connection", e);
        }
    }

    /**
     * Retrieves the singleton instance of the {@code DatabaseConnection}.
     * Re-initializes the instance if it was previously closed.
     *
     * @return The active {@code DatabaseConnection} instance.
     */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null || instance.isClosed) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            isClosed = false;
            loadData();
        }
        return connection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
                isClosed = true;
            } catch (SQLException e) {
                logError("Failed to close database connection", e);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadData() {
        try {
            persons.clear();
            locations.clear();
            supplies.clear();
            inquiries.clear();
            medicalRecords.clear();
            familyGroups.clear();

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Person");
            while (rs.next()) {
                int id = rs.getInt("person_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                Date dob = rs.getDate("date_of_birth");
                String gender = rs.getString("gender");
                String phoneNumber = rs.getString("phone_number");
                int familyGroupId = rs.getInt("family_group");
                if (rs.wasNull()) familyGroupId = 0;

                PreparedStatement ps = connection.prepareStatement(
                    "SELECT COUNT(*) FROM Inquiry WHERE inquirer_id = ?");
                ps.setInt(1, id);
                ResultSet rs2 = ps.executeQuery();
                rs2.next();
                boolean isInquirer = rs2.getInt(1) > 0;

                Person person = isInquirer ? 
                    new Inquirer(firstName, lastName, phoneNumber, "", true) : 
                    new DisasterVictim(firstName, lastName, phoneNumber, "Unknown");
                person.setId(id);
                person.setGender(gender);
                persons.put(id, person);

                if (familyGroupId != 0) {
                    familyGroups.computeIfAbsent(familyGroupId, k -> new FamilyGroup("Group", String.valueOf(k), null))
                        .addFamilyMember(person);
                }
            }

            rs = stmt.executeQuery("SELECT * FROM Location");
            while (rs.next()) {
                locations.put(rs.getInt("location_id"), new Location(
                    rs.getString("name"),
                    rs.getString("address")
                ));
            }

            rs = stmt.executeQuery("SELECT * FROM Supply");
            while (rs.next()) {
                int id = rs.getInt("supply_id");
                String type = rs.getString("type");
                String comments = rs.getString("comments");
                Supply supply;
                switch (type.toLowerCase()) {
                    case "cot": supply = new Cot(comments); break;
                    case "blanket": supply = new Blanket(); break;
                    case "personal item": supply = new PersonalBelonging(comments); break;
                    case "water": supply = new Water(); break;
                    default: supply = new Supply(type, 1);
                }
                supply.setId(id);
                supplies.put(id, supply);
            }

            rs = stmt.executeQuery("SELECT * FROM Inquiry");
            while (rs.next()) {
                int id = rs.getInt("inquiry_id");
                Integer inquirerId = rs.getInt("inquirer_id");
                if (rs.wasNull()) inquirerId = null;
                int seekingId = rs.getInt("seeking_id");
                Inquiry inquiry = new Inquiry(
                    inquirerId != null ? (Inquirer) persons.get(inquirerId) : null,
                    rs.getString("comments")
                );
                inquiry.setId(id);
                inquiry.setSeekingId(seekingId);
                inquiries.put(id, inquiry);
            }

            rs = stmt.executeQuery("SELECT * FROM MedicalRecord");
            while (rs.next()) {
                int id = rs.getInt("medical_record_id");
                MedicalRecord record = new MedicalRecord(
                    locations.get(rs.getInt("location_id")),
                    rs.getString("treatment_details"),
                    rs.getTimestamp("date_of_treatment").toString().substring(0, 10)
                );
                medicalRecords.put(id, record);
            }

            loadAssociations();
            cleanupExpiredWater();
        } catch (SQLException e) {
            logError("Failed to load database data", e);
            throw new RuntimeException("Database loading failed", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadAssociations() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM PersonLocation");
        while (rs.next()) {
            int personId = rs.getInt("person_id");
            int locationId = rs.getInt("location_id");
            Person person = persons.get(personId);
            Location location = locations.get(locationId);
            if (person instanceof DisasterVictim && location != null) {
                location.addOccupant((DisasterVictim) person);
            }
        }

        rs = stmt.executeQuery("SELECT * FROM SupplyAllocation");
        while (rs.next()) {
            int supplyId = rs.getInt("supply_id");
            Integer personId = rs.getInt("person_id");
            Integer locationId = rs.getInt("location_id");
            Supply supply = supplies.get(supplyId);
            if (supply == null) continue;

            if (!rs.wasNull() && personId != null && persons.containsKey(personId)) {
                // Could add supply to person's inventory if such a method existed
            } else if (!rs.wasNull() && locationId != null && locations.containsKey(locationId)) {
                locations.get(locationId).addSupply(supply);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cleanupExpiredWater() { 
        Water.cleanupExpiredWater(this); 
    }

    /**
     * {@inheritDoc}
     */
    public static void logError(String message, Exception e) {
        try (FileWriter writer = new FileWriter("data/errorlog.txt", true)) {
            writer.write(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    + " - " + message + " - " + e.getMessage() + "\n");
        } catch (IOException ioException) {
            System.err.println("Error logging failed: " + ioException.getMessage());
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

    /**
     * {@inheritDoc}
     */
    @Override
    public String executeCustomQuery(String sqlQuery) throws SQLException {
        StringBuilder result = new StringBuilder();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sqlQuery)) {
            
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            // Add column headers
            for (int i = 1; i <= columnCount; i++) {
                result.append(metaData.getColumnName(i));
                if (i < columnCount) result.append(" | ");
            }
            result.append("\n");
            
            // Add data rows
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    result.append(rs.getString(i));
                    if (i < columnCount) result.append(" | ");
                }
                result.append("\n");
            }
            
            return result.toString();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean updateRecord(String tableName, int id, String columnName, Object newValue) throws SQLException {
        String idColumnName = tableName.toLowerCase() + "_id";
        String sql = "UPDATE " + tableName + " SET " + columnName + " = ? WHERE " + idColumnName + " = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            if (newValue instanceof String) {
                pstmt.setString(1, (String) newValue);
            } else if (newValue instanceof Integer) {
                pstmt.setInt(1, (Integer) newValue);
            } else if (newValue instanceof Date) {
                pstmt.setDate(1, (Date) newValue);
            } else {
                pstmt.setObject(1, newValue);
            }
            
            pstmt.setInt(2, id);
            int rowsAffected = pstmt.executeUpdate();
            
            return rowsAffected > 0;
        }
    }
}