package fr.cotedazur.univ.polytech.citadellesgroupeq.evaluator;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import fr.cotedazur.univ.polytech.citadellesgroupeq.playerevaluator.GameStatsCsv;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameStatsCsvTest {
    @Test
    void testThrowsIllegalArg() {
        GameStatsCsv csv=new GameStatsCsv();
        List<String[]> incorrectData=new ArrayList<>();
        incorrectData.add(new String[] {"hehhe", "haha"});
        assertThrows(IllegalArgumentException.class, () -> csv.writeInCsvFile(incorrectData));//throws because not of the right size
    }

    @Test
    void testDetailsThrowsIllegalArg() {
        GameStatsCsv csv=new GameStatsCsv();
        assertThrows(IllegalArgumentException.class, () -> csv.writeInCsvDetailsFile(new String[] {"hehhe", "haha"}));//throws because not of the right size
    }

    @Test
    void testCanReadInFile() throws IOException, CsvValidationException {
        GameStatsCsv csv=new GameStatsCsv();

        if(!Files.exists(GameStatsCsv.CSV_PATH) || Files.size(GameStatsCsv.CSV_PATH) == 0) {
            assertThrows(IllegalStateException.class, csv::getReaderOfResumeStatsCsv);
        }
        else {
            CSVReader reader=csv.getReaderOfResumeStatsCsv();
            assertNotNull(reader.readNext());
        }
    }
}
