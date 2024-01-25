package fr.cotedazur.univ.polytech.citadellesgroupeq;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardDeckTest {
    public static final String CORRECT_PATH= CardDeck.DEFAULT_PATH;
    public static final String INCORRECT_PATH="jkflksdnhfkjsdfls";
    public static final String NOT_JSON_FORMATTED_FILE="src/main/resources/test_json_file/totally_incorrect_json.json";

    public static final String JSON_MISSING_KEYS_FILE="src/main/resources/test_json_file/file_missing_keys.json";

    @Test
    @SuppressWarnings("java:S2699")//add assertion to this case
    void testCorrectFile() {
        CardDeck reader;
        try {
            reader = new CardDeck();
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
        reader.getDistrictsListDescription();
    }

    @Test
    void testIncorrectPath() {
        assertThrows(CardDeck.BadlyInitializedReader.class, () -> new CardDeck(INCORRECT_PATH));
    }

    @Test
    void testNotJsonFile() {
        assertThrows(CardDeck.BadlyInitializedReader.class, () -> new CardDeck(NOT_JSON_FORMATTED_FILE));
    }

    @Test
    void testMissingKeysJsonFile() {
        assertThrows(CardDeck.BadlyInitializedReader.class, () -> new CardDeck(JSON_MISSING_KEYS_FILE));
    }
}