import java.sql.*;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;

public class DisasterRelief {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/ensf380project";
    private static final String USER = "aditijainin25";  // Change this
    private static final String PASSWORD = "Seokj!n7";  // Change this
    private static Map<String, String> translations = new HashMap<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        loadLanguage("en-CA");  // Default to English, can be changed

        while (true) {
            System.out.println("\n" + translations.get("menu_title"));
            System.out.println("1. " + translations.get("view_victims"));
            System.out.println("2. " + translations.get("search_victim"));
            System.out.println("3. " + translations.get("log_inquiry"));
            System.out.println("4. " + translations.get("exit"));
            System.out.print("> " );

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1 -> viewVictims();
                case 2 -> searchVictim();
                case 3 -> logInquiry();
                case 4 -> {
                    System.out.println(translations.get("goodbye"));
                    return;
                }
                default -> System.out.println(translations.get("invalid_option"));
            }
        }
    }

    private static void viewVictims() {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Person")) {

            System.out.println("\n" + translations.get("list_victims"));
            while (rs.next()) {
                System.out.printf("%d. %s %s (%s)%n", rs.getInt("person_id"),
                        rs.getString("first_name"), rs.getString("last_name"),
                        rs.getString("gender"));
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

    private static void searchVictim() {
        System.out.print(translations.get("input_firstname") + " ");
        String firstName = scanner.nextLine();
        System.out.print(translations.get("input_lastname") + " ");
        String lastName = scanner.nextLine();

        String sql = "SELECT * FROM Person WHERE first_name ILIKE ? AND last_name ILIKE ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + firstName + "%");
            stmt.setString(2, "%" + lastName + "%");

            ResultSet rs = stmt.executeQuery();
            if (!rs.isBeforeFirst()) {
                System.out.println(translations.get("no_results"));
                return;
            }

            while (rs.next()) {
                System.out.printf("%d. %s %s (%s)%n", rs.getInt("person_id"),
                        rs.getString("first_name"), rs.getString("last_name"),
                        rs.getString("gender"));
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

    private static void logInquiry() {
        System.out.print(translations.get("input_inquirer_id") + " ");
        int inquirerId = scanner.nextInt();
        System.out.print(translations.get("input_seeking_id") + " ");
        int seekingId = scanner.nextInt();
        System.out.print(translations.get("input_location_id") + " ");
        int locationId = scanner.nextInt();
        scanner.nextLine();  // Consume newline
        System.out.print(translations.get("input_comments") + " ");
        String comments = scanner.nextLine();

        String sql = "INSERT INTO Inquiry (inquirer_id, seeking_id, location_id, date_of_inquiry, comments) " +
                     "VALUES (?, ?, ?, NOW(), ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, inquirerId);
            stmt.setInt(2, seekingId);
            stmt.setInt(3, locationId);
            stmt.setString(4, comments);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println(translations.get("inquiry_logged"));
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

    private static void loadLanguage(String languageCode) {
        try {
            File file = new File("data/" + languageCode + ".xml");
            if (!file.exists()) {
                System.err.println("Language file not found, defaulting to English.");
                file = new File("data/en-CA.xml");
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList list = doc.getElementsByTagName("translation");
            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String key = element.getElementsByTagName("key").item(0).getTextContent();
                    String value = element.getElementsByTagName("value").item(0).getTextContent();
                    translations.put(key, value);
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading language file: " + e.getMessage());
        }
    }
}
