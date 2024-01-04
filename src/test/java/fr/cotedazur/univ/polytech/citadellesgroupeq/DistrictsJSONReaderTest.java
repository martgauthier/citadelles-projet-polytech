package fr.cotedazur.univ.polytech.citadellesgroupeq;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DistrictsJSONReaderTest {
    public static final String CORRECT_PATH= DistrictsJSONReader.DEFAULT_PATH;
    public static final String INCORRECT_PATH="jkflksdnhfkjsdfls";
    public static final String NOT_JSON_FORMATTED_FILE="src/main/resources/test_json_file/totally_incorrect_json.json";

    public static final String JSON_MISSING_KEYS_FILE="src/main/resources/test_json_file/file_missing_keys.json";

    @Test
    void testCorrectFile() {
        DistrictsJSONReader reader;
        try {
            reader = new DistrictsJSONReader();
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
        reader.getDistrictsListDescription();
    }

    @Test
    void testIncorrectPath() {
        assertThrows(DistrictsJSONReader.BadlyInitializedReader.class, () -> new DistrictsJSONReader(INCORRECT_PATH));
    }

    @Test
    void testNotJsonFile() {
        assertThrows(DistrictsJSONReader.BadlyInitializedReader.class, () -> new DistrictsJSONReader(NOT_JSON_FORMATTED_FILE));
    }

    @Test
    void testMissingKeysJsonFile() {
        assertThrows(DistrictsJSONReader.BadlyInitializedReader.class, () -> new DistrictsJSONReader(JSON_MISSING_KEYS_FILE));
    }
}