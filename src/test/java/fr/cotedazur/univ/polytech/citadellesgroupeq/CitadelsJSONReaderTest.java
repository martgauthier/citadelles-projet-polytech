package fr.cotedazur.univ.polytech.citadellesgroupeq;

import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.InvalidPropertiesFormatException;

import static org.junit.jupiter.api.Assertions.*;

class CitadelsJSONReaderTest {
    public static final String CORRECT_PATH=CitadelsJSONReader.DEFAULT_PATH;
    public static final String INCORRECT_PATH="jkflksdnhfkjsdfls";
    public static final String NOT_JSON_FORMATTED_FILE="src/main/resources/test_json_file/totally_incorrect_json.json";

    public static final String JSON_MISSING_KEYS_FILE="src/main/resources/test_json_file/file_missing_keys.json";

    @Test
    void testCorrectFile() {
        CitadelsJSONReader reader;
        try {
            reader = new CitadelsJSONReader();
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
        reader.getCitadelsListDescription();
    }

    @Test
    void testIncorrectPath() {
        assertThrows(ParseException.class, () -> new CitadelsJSONReader(INCORRECT_PATH));
    }

    @Test
    void testNotJsonFile() {
        assertThrows(ParseException.class, () -> new CitadelsJSONReader(NOT_JSON_FORMATTED_FILE));
    }

    @Test
    void testMissingKeysJsonFile() {
        assertThrows(ParseException.class, () -> new CitadelsJSONReader(JSON_MISSING_KEYS_FILE));
    }
}