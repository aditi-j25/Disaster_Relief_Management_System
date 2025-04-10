package edu.ucalgary.oop;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.Assert.*;

public class LanguageSupportTest {
    private LanguageSupport languageSupport;
    private File dataDir;
    private File enFile;
    private File frFile;

    @Before
    public void setUp() throws IOException {
        // Ensure the data directory exists
        dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }
        // Create test language files in the data directory
        createEnglishFile();
        createFrenchFile();

        // Initialize LanguageSupport with the test language
        languageSupport = new LanguageSupport("new_en-CA");
    }
    private void createEnglishFile() throws IOException {
        enFile = new File(dataDir, "new_en-CA.xml");
        String content =
            "<key>GENDER_PROMPT</key>\n" +
            "<value>Please select your gender (man/woman/non-binary):</value>\n" +
            "<key>man</key>\n" +
            "<value>man</value>\n" +
            "<key>woman</key>\n" +
            "<value>woman</value>\n";
        writeFile(enFile, content);
    }

    private void createFrenchFile() throws IOException {
        frFile = new File(dataDir, "new_fr-CA.xml");
        String content =
            "<key>GENDER_PROMPT</key>\n" +
            "<value>Veuillez sélectionner votre genre (homme/femme/non-binaire):</value>\n" +
            "<key>man</key>\n" +
            "<value>homme</value>\n" +
            "<key>woman</key>\n" +
            "<value>femme</value>\n";
        writeFile(frFile, content);
    }

    private void writeFile(File file, String content) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
    }

    @After
    public void tearDown() {
        if (enFile != null && enFile.exists()) {
            enFile.delete();
        }
        if (frFile != null && frFile.exists()) {
            frFile.delete();
        }
        // Don't delete dataDir in case other language files exist
    }

    @Test
    public void testDefaultLanguage() {
        assertEquals("Default language should be new_en-CA", "new_en-CA", languageSupport.getLanguageCode());
    }

    @Test
    public void testLanguageChange() {
        languageSupport.setLanguageCode("new_fr-CA");
        assertEquals("Language should change to new_fr-CA", "new_fr-CA", languageSupport.getLanguageCode());
    }

    @Test
    public void testEnglishTranslationForGenderPrompt() {
        assertEquals("Translation for 'GENDER_PROMPT' in English failed",
                "Please select your gender (man/woman/non-binary):",
                languageSupport.getText("GENDER_PROMPT"));
    }

    @Test
    public void testFrenchTranslationForGenderPrompt() {
        languageSupport.setLanguageCode("new_fr-CA");
        assertEquals("Translation for 'GENDER_PROMPT' in French failed",
                "Veuillez sélectionner votre genre (homme/femme/non-binaire):",
                languageSupport.getText("GENDER_PROMPT"));
    }

    @Test
    public void testFallbackToDefaultLanguage() {
        languageSupport.setLanguageCode("invalid-CODE");
        assertEquals("Fallback language should be en-CA", "en-CA", languageSupport.getLanguageCode()); 
        //according to the code file is en_CA
    }

    @Test
    public void testMissingTranslation() {
        String result = languageSupport.getText("NON_EXISTENT_KEY");
        assertTrue("Should return missing translation message for a nonexistent key",
                result.startsWith("Missing translation for key:"));
    }

    @Test
    public void testPartialTranslationInFrench() {
        languageSupport.setLanguageCode("new_fr-CA");
        assertEquals("French translation for 'man' failed", "homme", languageSupport.getText("man"));
        assertEquals("French translation for 'woman' failed", "femme", languageSupport.getText("woman"));
    }

    @Test
    public void testPartialTranslationInEnglish() {
        assertEquals("English translation for 'man' failed", "man", languageSupport.getText("man"));
        assertEquals("English translation for 'woman' failed", "woman", languageSupport.getText("woman"));
    }
}
