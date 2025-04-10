classDiagram
    class Person {
        -int id
        -String firstName
        -String lastName
        -String phoneNumber
        -String gender
        -String dateOfBirth
        -ArrayList~Supply~ allocatedSupplies
        +Person(String firstName, String lastName, String phoneNumber)
        +Person()
        +getId() int
        +setId(int id) void
        +getFirstName() String
        +setFirstName(String firstName) void
        +getLastName() String
        +setLastName(String lastName) void
        +getPhoneNumber() String
        +setPhoneNumber(String phoneNumber) void
        +getFullName() String
        +getGender() String
        +setGender(String gender) void
        +getDateOfBirth() String
        +setDateOfBirth(String dateOfBirth) void
        +addAllocatedSupply(Supply supply) void
        +getAllocatedSupplies() ArrayList~Supply~
        +logError(Exception e) static void
        +toString() String
        +equals(Object obj) boolean
    }

    class DisasterVictim {
        -String status
        -String disasterType
        +DisasterVictim(firstName: String, lastName: String, phoneNumber: String, disasterType: String)
        +getDisasterType(): String
        +setDisasterType(disasterType: String): void
        +toString(): String
        +DisasterVictim(String firstName, String lastName, String phoneNumber, String status)
        +getStatus() String
        +setStatus(String status) void
        +addDisasterVictim(UserInterfaceImplied ui) void
        +editDisasterVictim(UserInterfaceImplied ui) void
    }

    class Location {
        -String name
        -String address
        -ArrayList~DisasterVictim~ occupants
        -ArrayList~Supply~ supplies
        +Location(String name, String address)
        +getName() String
        +setName(String name) void
        +getAddress() String
        +setAddress(String address) void
        +getOccupants() ArrayList~DisasterVictim~
        +setOccupants(ArrayList~DisasterVictim~ occupants) void
        +getSupplies() ArrayList~Supply~
        +setSupplies(ArrayList~Supply~ supplies) void
        +addOccupant(DisasterVictim occupant) void
        +removeOccupant(DisasterVictim occupant) void
        +addSupply(Supply supply) void
        +removeSupply(Supply supply) void
    }

    class Supply {
        -int id
        -String type
        -String comments
        +Supply(String type, int id)
        +Supply(String type)
        +Supply()
        +Supply(int id)
        +setId(int id) void
        +getId() int
        +getType() String
        +setType(String type) void
        +getComments() String
        +setComments(String comments) void
        +allocateSupplies(UserInterfaceImplied ui) void
        +viewSupplies(UserInterfaceImplied ui) void
        +editSupply(UserInterfaceImplied ui) static void
        +updateDatabase(UserInterfaceImplied ui) void
        +selectPerson(String prompt, UserInterfaceImplied ui) int
        +checkLocationMatch(int supplyId, int personId, DatabaseConnector db) boolean
        +allocateSupplyToPerson(int supplyId, int personId, DatabaseConnector db) void
        +allocateSupplyToLocation(int supplyId, int locationId, DatabaseConnector db) void
        -selectLocation(UserInterfaceImplied ui) int
    }

    class PersonalBelonging {
        +PersonalBelonging(String description)
        +setComments(String comments) void
    }

      class Blanket {
        +Blanket()
        +getType(): String
    }

    class Cot {
        +Cot(location: String)
        +getType(): String
    }
      
    class Water {
        -LocalDateTime allocationDate
        +Water()
        +setAllocationDate(LocalDateTime date) void
        +getAllocationDate() LocalDateTime
        +isExpired() boolean
        +allocateToPersonWithExpiry(int personId, DatabaseConnector db) void
        +allocateToLocation(int locationId, DatabaseConnector db) void
        +cleanupExpiredWater(DatabaseConnector db) static int
    }

    class MedicalRecord {
        -Location location
        -String treatmentDetails
        -String dateOfTreatment
        +MedicalRecord(Location location, String treatmentDetails, String dateOfTreatment)
        +getLocation() Location
        +setLocation(Location location) void
        +getTreatmentDetails() String
        +setTreatmentDetails(String treatmentDetails) void
        +getDateOfTreatment() String
        +setDateOfTreatment(String dateOfTreatment) void
        -isValidDateFormat(String date) boolean
    }

    class LanguageSupport {
        -String languageCode
        -Map~String, String~ translations
        +LanguageSupport(String languageCode)
        -loadLanguageFile(boolean isFallback) void
        +getText(String key) String
        +setLanguageCode(String languageCode) void
        +getLanguageCode() String
        +chooseLanguage() void
    }

    class Main {
        +Main()
        +main(String[] args) static void
    }

    class UserInterface {
        <<interface>>
        +displayMainMenu() void
        +displayError(String message) void
        +showSuccess(String message) void
        +showPrompt(String message) void
        +getInput() String
        +viewDisasterVictimInfo() void
    }

    class UserInterfaceImplied {
        -Scanner scanner
        -LanguageSupport languageSupport
        -DatabaseConnector dbConnection
        -Map~Integer, Person~ persons
        -Map~Integer, Location~ locations
        -Map~Integer, Supply~ supplies
        -Map~Integer, Inquiry~ inquiries
        -Map~Integer, MedicalRecord~ medicalRecords
        -Map~Integer, FamilyGroup~ familyGroups
        +UserInterfaceImplied()
        +UserInterfaceImplied(LanguageSupport languageSupport)
        +setDatabaseConnection(DatabaseConnector dbConnection) void
        +run() void
        -processChoice(String choice) boolean
        +getMultiLineInput(String prompt) String
        +getValidatedInput(String prompt, String regex, String errorMessage) String
        +logError(String message, Exception e) void
        +getLanguageSupport() LanguageSupport
        +getDbConnection() DatabaseConnector
        +getPersons() Map~Integer, Person~
        +getLocations() Map~Integer, Location~
        +getSupplies() Map~Integer, Supply~
        +getInquiries() Map~Integer, Inquiry~
        +getMedicalRecords() Map~Integer, MedicalRecord~
        +getFamilyGroups() Map~Integer, FamilyGroup~
        +getMaxRetries() int
    }

    class DatabaseConnector {
        <<interface>>
        +getConnection() Connection
        +closeConnection() void
        +getPersons() Map~Integer, Person~
        +getLocations() Map~Integer, Location~
        +getSupplies() Map~Integer, Supply~
        +getInquiries() Map~Integer, Inquiry~
        +getMedicalRecords() Map~Integer, MedicalRecord~
        +getFamilyGroups() Map~Integer, FamilyGroup~
        +logError(String message, Exception e) static void
    }

    class RealDatabaseConnection {
        <<interface>>
        +executeCustomQuery(String sqlQuery) String
        +updateRecord(String tableName, int id, String columnName, Object newValue) boolean
        +loadAssociations() void
    }

    class DatabaseConnection {
        <<implied>>
        +getInstance() static DatabaseConnection
    }

   class MockDatabaseConnection {
          <<interface>>
          +reset(): void
          +populateTestData(): void
      }
  
      class MockDatabaseConnectionImpl {
          <<implied>>
          -static MockDatabaseConnectionImpl instance
          +static getInstance(): MockDatabaseConnectionImpl
          +reset(): void
          +populateTestData(): void
      }

    
    class Inquirer {
        <<implied>>
        +selectInquirer(UserInterfaceImplied ui) Inquirer
        +displayInquirerList(UserInterfaceImplied ui, Map~Integer, Person~ persons) void
        -String inquiryMessage
        -boolean isVictim
        +Inquirer(firstName: String, lastName: String, phoneNumber: String, inquiryMessage: String, isVictim: boolean)
        +getInquiryMessage(): String
        +setInquiryMessage(inquiryMessage: String): void
        +isVictim(): boolean
        +setVictim(victim: boolean): void
        +toString(): String
    }
    
    class Inquiry {
        <<implied>>
        -Inquirer inquirer
        -String inquiryDetails
        +Inquiry(Inquirer inquirer, String inquiryDetails)
        +logInquiry(UserInterfaceImplied ui) void
        +editInquiry(UserInterfaceImplied ui) void
    }
    
    class FamilyGroup {
        <<implied>>
        -List~DisasterVictim~ familyMembers
        +FamilyGroup(firstName: String, lastName: String, phoneNumber: String)
        +addFamilyMember(member: DisasterVictim): void
        +removeFamilyMember(member: DisasterVictim): void
        +getFamilyMembers(): List~DisasterVictim~
        +getFamilySize(): int
        +getFamilyMembers() List~Person~
    }

    Person <|-- DisasterVictim
    Supply <|-- PersonalBelonging
    Supply <|-- Water
    UserInterface <|.. UserInterfaceImplied
    DatabaseConnector <|-- RealDatabaseConnection

    Person "1" -- "0..*" Supply : has allocated >
    Location "1" -- "0..*" DisasterVictim : houses >
    Location "1" -- "0..*" Supply : contains >
    Location "1" -- "1" MedicalRecord : provided at >
    UserInterfaceImplied "1" -- "1" LanguageSupport : uses >
    UserInterfaceImplied "1" -- "1" DatabaseConnector : connects to >
    Inquiry "0..*" -- "1" Inquirer : made by >
    DisasterVictim "0..*" -- "0..1" FamilyGroup : member of >
        
    Person <|-- DisasterVictim
    Person <|-- FamilyGroup
    Person <|-- Inquirer
    Supply <|-- Blanket
    Supply <|-- Cot
    Inquirer --* Inquiry
    DisasterVictim --o FamilyGroup
    Supply --o Person
    MockDatabaseConnectionImpl ..|> MockDatabaseConnection
    MockDatabaseConnection ..|> DatabaseConnection
    UserInterfaceImplied --> DatabaseConnection
    UserInterfaceImplied --> LanguageSupport
