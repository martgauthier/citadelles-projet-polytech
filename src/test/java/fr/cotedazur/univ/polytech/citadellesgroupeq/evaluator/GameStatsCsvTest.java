package fr.cotedazur.univ.polytech.citadellesgroupeq.evaluator;

import fr.cotedazur.univ.polytech.citadellesgroupeq.playerevaluator.GameStatsCsv;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameStatsCsvTest {
    GameStatsCsv csv;

    @BeforeEach
    void setup() {
        csv=new GameStatsCsv();
    }

    void deleteFileIfPresent() {
        File file = new File(GameStatsCsv.CSV_PATH.toString());
        if (file.exists()) {
            file.delete();
        }
    }

    void deleteDetailsFileIfPresent() {
        File file = new File(GameStatsCsv.DETAILS_CSV_PATH.toString());
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void testCreateCsvFile() {
        deleteFileIfPresent();
        csv.createCsvFile(List.of());
        assertTrue(Files.exists(GameStatsCsv.CSV_PATH));
        deleteFileIfPresent();
    }

    @Test
    void testCreateCsvDetailsFile() {
        deleteDetailsFileIfPresent();
        csv.createCsvDetailsFile();
        assertTrue(Files.exists(GameStatsCsv.DETAILS_CSV_PATH));
        deleteDetailsFileIfPresent();
    }

    @Test
    void testWriteInCsvDetailsThrows() {
        deleteDetailsFileIfPresent();
        csv.createCsvDetailsFile();
        List<String> stringList = new ArrayList<>(List.of("1", "2", "3", "4", "5", "6", "7", "8"));
        String[] stringArray=(stringList.toArray(new String[0]));
        assertThrows(IllegalArgumentException.class, () -> csv.writeInCsvDetailsFile(stringArray));
        deleteDetailsFileIfPresent();

        csv.createCsvDetailsFile();
        stringList.add("9");
        csv.writeInCsvDetailsFile(stringList.toArray(new String[0]));//assert that it doesn't throw

        deleteDetailsFileIfPresent();
    }

    @Test
    void testWriteInCsvThrows() {
        deleteFileIfPresent();
        csv.createCsvFile(List.of());

        List<String[]> data=new ArrayList<>();
        for(int i=0; i<8; i++) {
            data.add(new String[0]);
        }

        assertThrows(IllegalArgumentException.class, () -> csv.writeInCsvFile(data));//not of the right size
        deleteFileIfPresent();

        csv.createCsvFile(List.of());

        //set all index of data to be a string array of size 8
        for(int i=0; i<8; i++) {
            data.set(i, new String[8]);
        }

        csv.writeInCsvFile(data);//assert that it doesn't throw

        deleteFileIfPresent();
    }
}
