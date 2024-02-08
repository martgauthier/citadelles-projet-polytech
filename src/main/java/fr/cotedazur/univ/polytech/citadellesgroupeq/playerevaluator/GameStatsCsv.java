package fr.cotedazur.univ.polytech.citadellesgroupeq.playerevaluator;

import com.opencsv.*;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;

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
    public static final Path DETAILS_CSV_PATH = Paths.get("stats", "gamestatdetail.csv");
    public static final Path CSV_PATH = Paths.get("stats", "gamestat.csv");

    public void createCsvDetailsFile(){
        try (CSVWriter writer = new CSVWriter(new FileWriter(DETAILS_CSV_PATH.toFile()))) {
            String[] header = {"Id partie", "Nom du joueur","Prix moyen des citadelles achetées", "Rôle préféré",
                    "Nombre de fois où il assassine", "Nombre de fois où il s'est fait assassiner", "Score", "Victoire", "Egalite"
            };
            writer.writeNext(header);
        } catch (IOException e) {
            throw new IllegalStateException("Can't write in this file !");
        }
    }

    public void createCsvFile(List<Player> playerList){
        try {
            String[] header = {"Nom du joueur",
                    "Nombre de parties gagnées",
                    "Pourcentage de parties gagnées",
                    "Nombre de parties perdues",
                    "Pourcentage de parties perdues",
                    "Nombre de parties égalité",
                    "Pourcentage d'égalité",
                    "Nombre de parties jouées"
            };

            if (!Files.exists(CSV_PATH.toAbsolutePath()) || Files.size(CSV_PATH.toAbsolutePath()) == 0) {
                CSVWriter writer = new CSVWriter(new FileWriter(CSV_PATH.toString()));
                List<String[]> playerData = new ArrayList<>();
                playerData.add(header);
                for(Player joueur: playerList) {
                    playerData.add(new String[]{ joueur.getBotLogicName(), "0", "0", "0", "0", "0", "0", "0"});
                }

                writer.writeAll(playerData);
                writer.close();
            }
        } catch (IOException e) {
            throw new IllegalStateException("Can't write in file !");
        }
    }

    public void writeInCsvDetailsFile(String[] data) {
        if(data.length != 9) {
            throw new IllegalArgumentException("Data must be of length 9 !");
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter(DETAILS_CSV_PATH.toString(), true))) {
            if (!Files.exists(DETAILS_CSV_PATH) || Files.size(DETAILS_CSV_PATH) == 0) {
                createCsvDetailsFile();
            }
            writer.writeNext(data);
        } catch (IOException e) {
            throw new IllegalStateException("Can't write in file !");
        }
    }

    public void writeInCsvFile(List<String[]> data) {
        for(String[] lineData: data) {
            if(lineData.length != 8) throw new IllegalArgumentException("All lines of data must be of size 8 !");
        }

        try {
            if (!Files.exists(CSV_PATH)) {
                throw new IllegalStateException("File should exist at this state !");
            }
            CSVWriter writer = new CSVWriter(new FileWriter(CSV_PATH.toString(), false));

            writer.writeAll(data);
            writer.close();
        } catch (IOException e) {
            throw new IllegalStateException("Can't write in file !");
        }
    }

    public CSVReader getReaderOfResumeStatsCsv(){
        try {
            return new CSVReader(new FileReader(CSV_PATH.toString()));
        } catch (IOException e) {
            throw new IllegalStateException("Can't read file !");
        }
    }
}