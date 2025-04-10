package edu.ucalgary.oop;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
//Don't need the test file for this 
/**
 * Provides basic internationalization support by loading translations from
 * simple XML-like language files without using external libraries.
 * 
 * @author 30208786
 * @version 5.0
 * @since 2025-04-09
 */
public class LanguageSupport {
    private String languageCode = "en-CA";
    private final Map<String, String> translations = new HashMap<>();

    /**
     * Constructs a LanguageSupport object with the specified language code.
     * 
     * @param languageCode The language code (e.g., "en-CA", "fr-CA").
     */
    public LanguageSupport(String languageCode) {
        this.languageCode = languageCode;
        loadLanguageFile(false);
    }

    /**
     * Loads translation data from an XML-like file in the /data directory.
     * Falls back to "en-CA" if the selected file can't be loaded.
     * 
     * @param isFallback True if this is a fallback load attempt.
     */
    private void loadLanguageFile(boolean isFallback) {
        File file = new File("data/" + languageCode + ".xml");
        if (!file.exists()) {
            if (!isFallback) {
                System.out.println("Language file not found for code: " + languageCode + ". Using default en-CA.");
                this.languageCode = "en-CA";
                loadLanguageFile(true);
            } else {
                System.err.println("Default language file en-CA.xml is missing. Application may not work properly.");
            }
            return;
        }
        try (Scanner scanner = new Scanner(file)) {
            translations.clear();
            String key = null, value = null;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.startsWith("<key>") && line.endsWith("</key>")) {
                    key = line.replace("<key>", "").replace("</key>", "").trim();
                } else if (line.startsWith("<value>") && line.endsWith("</value>")) {
                    value = line.replace("<value>", "").replace("</value>", "").trim();
                }
                if (key != null && value != null) {
                    translations.put(key, value);
                    key = null;
                    value = null;
                }
            }
            System.out.println("Loaded language: " + languageCode);
        } catch (FileNotFoundException e) {
            System.err.println("Error reading language file: " + e.getMessage());
            if (!isFallback) {
                System.out.println("Falling back to default language (en-CA).");
                this.languageCode = "en-CA";
                loadLanguageFile(true);
            }
        }
    }

    /**
     * Gets a translated message for the given key.
     * 
     * @param key The message key.
     * @return The translated string or a placeholder message if not found.
     */
    public String getText(String key) { return translations.getOrDefault(key, "Missing translation for key: " + key);}

    /**
     * Changes the active language and reloads the appropriate file.
     * 
     * @param languageCode The new language code.
     */
    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
        loadLanguageFile(false);
    }

    /**
     * Gets the currently active language code.
     * 
     * @return The current language code.
     */
    public String getLanguageCode() { return languageCode;  }

    /**
     * Allows the user to choose a language from files in the /data folder.
     */
    public void chooseLanguage() {
        File dataDir = new File("data/");
        File[] files = dataDir.listFiles((dir, name) -> name.endsWith(".xml"));
        if (files == null || files.length == 0) {
            System.err.println("No language files found. Using default language.");
            return;
        }
        System.out.println("Available languages:");
        for (int i = 0; i < files.length; i++) {
            String name = files[i].getName().replace(".xml", "");
            System.out.println((i + 1) + ". " + name);
        }
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter the number corresponding to your language: ");
            String input = scanner.nextLine().trim();
            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= files.length) {
                    String selected = files[choice - 1].getName().replace(".xml", "");
                    setLanguageCode(selected);
                    System.out.println("Language set to: " + selected);
                    break;
                } else {
                    System.out.println("Invalid choice. Try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
}
