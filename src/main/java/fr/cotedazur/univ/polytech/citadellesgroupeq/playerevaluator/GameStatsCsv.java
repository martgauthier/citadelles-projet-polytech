package fr.cotedazur.univ.polytech.citadellesgroupeq.playerevaluator;

import com.opencsv.*;
import com.opencsv.exceptions.CsvException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GameStatsCsv {
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
        try (CSVWriter writer = new CSVWriter(new FileWriter(csvPath.toFile()))) {
            String[] header = {"Nom du joueur", "Nombre de parties gagnées", "Nombre de parties perdues",
                    "Nombre de parties jouées"
            };

            if (!Files.exists(detailsCsvPath) || Files.size(detailsCsvPath) == 0) {
                writer.writeNext(header);
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

    public void writeInCsvFile(String[] data) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(csvPath.toString(), false))) {
            if (!Files.exists(csvPath) || Files.size(csvPath) == 0) {
                createCsvFile();
            }
            List<String[]> updatedStatsLines = getUpdatedStatsLines(data);
            writer.writeAll(updatedStatsLines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public CSVReader getReaderOfResumeStatsCsv(){
        try {
            FileReader file=new FileReader(csvPath.toFile());
            int character=file.read();
            return new CSVReader(file);
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