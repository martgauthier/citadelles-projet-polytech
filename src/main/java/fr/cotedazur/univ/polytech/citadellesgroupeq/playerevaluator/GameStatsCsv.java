package fr.cotedazur.univ.polytech.citadellesgroupeq.playerevaluator;

import com.opencsv.*;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GameStatsCsv {
    private final Path csvPath = Paths.get("stats", "gamestat.csv");
    public static void main(String[] args) {
        GameStatsCsv gameStatsCsv = new GameStatsCsv();

        gameStatsCsv.createCsvFile();

        gameStatsCsv.readCsvFile();
    }

    public void createCsvFile(){

        try (CSVWriter writer = new CSVWriter(new FileWriter(csvPath.toFile()))) {
            String[] header = {"Nom du joueur","zeub"};
            writer.writeNext(header);

            // Écrire des lignes de données
            String[] player = {"Joueur1", "20"};

            writer.writeNext(player);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void readCsvFile() {
        try (CSVReader reader = new CSVReader(new FileReader(csvPath.toString()))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                for (String line : nextLine) {
                    System.out.print(line + "\t");
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }

}
