package fr.cotedazur.univ.polytech.citadellesgroupeq.playerevaluator;

import com.opencsv.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GameStatsCsv {
    private final Path csvPath = Paths.get("stats", "gamestat.csv");

    public void createCsvFile(){
        try (CSVWriter writer = new CSVWriter(new FileWriter(csvPath.toFile()))) {
            String[] header = {"Id partie", "Nom du joueur","Prix moyen des citadelles achetées", "Rôle préféré",
                    "Nombre de fois où il assassine", "Nombre de fois où il s'est fait assassiner"
            };
            writer.writeNext(header);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void writeInCsvFile(String[] data) {

        try (CSVWriter writer = new CSVWriter(new FileWriter(csvPath.toString(), true))) {
            if (!Files.exists(csvPath) || Files.size(csvPath) == 0) {
                createCsvFile();
            }
            writer.writeNext(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
