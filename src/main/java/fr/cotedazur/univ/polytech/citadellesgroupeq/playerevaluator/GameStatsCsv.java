package fr.cotedazur.univ.polytech.citadellesgroupeq.playerevaluator;

import com.opencsv.*;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GameStatsCsv {

    public GameStatsCsv() {
        //do nothing
    }
    private final Path detailsCsvPath = Paths.get("stats", "gamestatdetail.csv");
    private final Path csvPath = Paths.get("stats", "gamestat.csv");

    public void createCsvDetailsFile(){
        try (CSVWriter writer = new CSVWriter(new FileWriter(detailsCsvPath.toFile()))) {
            String[] header = {"Id partie", "Nom du joueur","Prix moyen des citadelles achetées", "Rôle préféré",
                    "Nombre de fois où il assassine", "Nombre de fois où il s'est fait assassiner", "Score", "Victoire"
            };
            writer.writeNext(header);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createCsvFile(){
        try {
            String[] header = {"Nom du joueur", "Nombre de parties gagnées", "Nombre de parties perdues",
                    "Nombre de parties jouées"
            };



            if (!Files.exists(csvPath.toAbsolutePath()) || Files.size(csvPath.toAbsolutePath()) == 0) {
                CSVWriter writer = new CSVWriter(new FileWriter(csvPath.toString()));
                writer.writeNext(header);
                String[][] playerData = {

                        {"MattPlayer", "0", "0", "0"},
                        {"ThomasPlayer", "0", "0", "0"},
                        {"AlwaysSpendPlayer", "0", "0", "0"},
                        {"RandomPlayer", "0", "0", "0"}
                };
                for (String[] data : playerData) {
                    writer.writeNext(data);
                }
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeInCsvDetailsFile(String[] data) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(detailsCsvPath.toString(), true))) {
            if (!Files.exists(detailsCsvPath) || Files.size(detailsCsvPath) == 0) {
                createCsvDetailsFile();
            }
            writer.writeNext(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeInCsvFile(List<String[]> data) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(csvPath.toString(), false))) {
            if (!Files.exists(csvPath)) {
                System.out.println("impossible");
            }

            writer.writeAll(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public CSVReader getReaderOfResumeStatsCsv(){
        try {
            return new CSVReader(new FileReader(csvPath.toString()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String[]> getUpdatedStatsLines(String[] data) {
        List<String[]> updatedLines = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(csvPath.toString()))) {
            List<String[]> lines = reader.readAll();
            // Parcourir les lignes et mettre à jour la statistique pour le joueur donné
            for (int i = 0; i < lines.size(); i++) {
                String[] currentLine = lines.get(i);
                // Si on est à la ligne concernée par l'update
                if (currentLine[0].equals(data[0])) {
                    updatedLines.add(data);
                } else {
                    updatedLines.add(currentLine);
                }
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
            // Gérer l'exception selon la logique métier (renvoyer une liste vide, null, ou lancer l'exception)
        }
        return updatedLines;
    }
}