package fr.cotedazur.univ.polytech.citadellesgroupeq.playerevaluator;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import fr.cotedazur.univ.polytech.citadellesgroupeq.District;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;

import java.io.IOException;
import java.util.*;

import static java.util.Map.entry;

/**
 * Cette classe s'occupe de stocker par joueur une liste de summary.
 */
public class StatsManager {
    private final Map<Player, List<RoundSummary>> playerSummaries;
    private final String[] stats;

    private final boolean[] playerHasTieGameArray;


    /**
     * Map regroupant les index dans les lignes de stat du CSV détaillé. Permet d'éviter d'utiliser des magic numbers
     */
    public static final Map<String, Integer> CSV_INDEXES=Map.ofEntries(
            entry("PLAYER_NAME", 0),
            entry("WIN_NUMBER", 1),
            entry("WIN_PERCENTAGE", 2),
            entry("LOOSE_NUMBER", 3),
            entry("LOOSE_PERCENTAGE", 4),
            entry("TIE_NUMBER", 5),
            entry("TIE_PERCENTAGE", 6),
            entry("GAME_NUMBER", 7)
    );

    private Optional<Player> playerWinning;

    public StatsManager(List<Player> playersList) {
        playerSummaries = new HashMap<>();
        for (Player player : playersList) {
            playerSummaries.put(player, new ArrayList<>());
        }
        stats= new String[9];
        Arrays.fill(stats,"");
        playerHasTieGameArray=new boolean[4];
        playerWinning=Optional.empty();
    }

    public void addSummary(Player playerName, RoundSummary summary) {
        List<RoundSummary> playerSummary = playerSummaries.get(playerName);
        playerSummary.add(summary);
    }

    /**
     * Cette méthode permet de fournir la ligne de stat d'un joueur
     * @param player le joueur concerné
     * @return un String[] des différentes stats de la partie
     */
    public String[] getStatForAPlayer(Player player,GameLogicManager game ,int round){
        stats[0] = "" + (round + 1);
        stats[1] = player.getBotLogicName();
        setStatOfAverageDistrictPrice(player);
        setStatOfFavoriteRole(player);
        setStatOfNumberOfKills(player);
        setStatOfNumberOfDeath(player);
        setStatOfScore(player, game);
        stats[7]=(playerWinning.isPresent() && playerWinning.get().equals(player)) ? "Oui" : "Non";
        stats[8]=(playerHasTieGameArray[player.getId()]) ? "Oui" : "Non";
        return stats;
    }

    public void writePlayersDetailsStatInCsv(GameStatsCsv csv, GameLogicManager game ,int round){
        for(Player player : playerSummaries.keySet()){
            // On récupère la stat de chaque joueur pour une partie
            String[] statForOnePlayer = getStatForAPlayer(player,game, round);
            csv.writeInCsvDetailsFile(statForOnePlayer);
        }
    }

    public void updatePlayerStatInCsv(GameStatsCsv csvToUpdate, GameLogicManager game, int round){
        csvToUpdate.createCsvFile(game.getPlayersList());//works perfectly
        for(Player player : playerSummaries.keySet()){
            // On récupère la stat de chaque joueur pour une partie
            String[] statForOnePlayer = getStatForAPlayer(player,game, round);//works

            List<String[]> data=getUpdatedStatsLine(csvToUpdate.getReaderOfResumeStatsCsv(), statForOnePlayer); // La on, récupère le petit csv à modif

            csvToUpdate.writeInCsvFile(data);
        }

    }
    /**
     * Le but de cette méthode est d'ajouter la stat du nombre de morts à la ligne associée à la partie d'un joueur.
     */
    public void setStatOfNumberOfDeath(Player player){
        int numberOfDeaths = 0;
        List<RoundSummary> playerSummary = playerSummaries.get(player);
        for (RoundSummary summary : playerSummary) {
            if(summary.hasBeenKilled()) {
                numberOfDeaths++;
            }
        }
        stats[5] = "" + numberOfDeaths;
    }

    /**
     * Le but de cette méthode est d'ajouter la stat du cout moyen d'achat d'un district.
     */
    public void setStatOfAverageDistrictPrice(Player player){
        List<District> city = player.getCity();
        if(city.isEmpty()){
            stats[2] = "" + 0;
        }
        else {
            int totalCityCost = 0;
            for (District district : city) {
                totalCityCost += district.getCost();
            }
            stats[2] = (totalCityCost/city.size())+ " pièces";
        }
    }

    /**
     * Le but de cette méthode est d'ajouter la stat du role préféré.
     */
    public void setStatOfFavoriteRole(Player player){
        EnumMap<Role, Integer> roleCounts = new EnumMap<>(Role.class);
        for (Role role : Role.values()) {
            roleCounts.put(role, 0);
        }
        List<RoundSummary> playerSummary = playerSummaries.get(player);
        for (RoundSummary summary : playerSummary) {
            int counter = roleCounts.get(summary.getPlayerRole());
            roleCounts.put(summary.getPlayerRole(), counter + 1);
        }
        Role favoriteRole = Collections.max(roleCounts.entrySet(), Map.Entry.comparingByValue()).getKey();
        stats[3] = "" + favoriteRole;
    }

    /**
     * Le but de cette méthode est d'ajouter la stat du nombre de fois que le joueur a assassiné.
     */
    public void setStatOfNumberOfKills(Player player){
        int numberOfKills = 0;
        List<RoundSummary> playerSummary = playerSummaries.get(player);
        for (RoundSummary summary : playerSummary) {
            if(summary.getPlayerRole() == Role.ASSASSIN && summary.hasUsedRolePower()) {
                numberOfKills++;
            }
        }
        stats[4] = "" + numberOfKills;
    }
    public void setStatOfScore(Player player, GameLogicManager game){
        int score = game.getScoreOfEnd().get(player);
        stats[6] = "" + score;
    }

    public void setWinForPlayer(Player player) {
        playerWinning=Optional.of(player);
    }

    public List<String[]> getUpdatedStatsLine(CSVReader csvReader, String[] stats) {
        List<String[]> data = new ArrayList<>();
        try {
            data.add(csvReader.readNext());//add header to data

            String[] nextLine;

            boolean playerHasBeenAdded=false;

            while ((nextLine = csvReader.readNext()) != null) {
                if (nextLine[CSV_INDEXES.get("PLAYER_NAME")].equals(stats[1])) {
                    playerHasBeenAdded=true;
                    int totalGames = Integer.parseInt(nextLine[CSV_INDEXES.get("GAME_NUMBER")]);

                    int nombreWin=Integer.parseInt(nextLine[CSV_INDEXES.get("WIN_NUMBER")]);
                    int nombreTie=Integer.parseInt(nextLine[CSV_INDEXES.get("TIE_NUMBER")]);
                    int nombreLoose=Integer.parseInt(nextLine[CSV_INDEXES.get("LOOSE_NUMBER")]);

                    if (stats[7].equals("Oui")) {//victoire
                        nombreWin++;
                    }
                    else if (stats[8].equalsIgnoreCase("Oui")){//égalité
                        nombreTie++;
                    }
                    else {//loose
                        nombreLoose++;
                    }



                    double newWinPercentage = (((double) nombreWin) /(totalGames+1)) * 100;
                    nextLine[CSV_INDEXES.get("WIN_PERCENTAGE")] = Double.toString(newWinPercentage);
                    nextLine[CSV_INDEXES.get("WIN_NUMBER")]=Integer.toString(nombreWin);

                    double newTiePercentage = (((double) nombreTie) / (totalGames+1)) * 100;
                    nextLine[CSV_INDEXES.get("TIE_PERCENTAGE")] = Double.toString(newTiePercentage);
                    nextLine[CSV_INDEXES.get("TIE_NUMBER")]=Integer.toString(nombreTie);

                    double newLoosePercentage = (((double) nombreLoose) / (totalGames+1)) * 100;
                    nextLine[CSV_INDEXES.get("LOOSE_PERCENTAGE")] = Double.toString(newLoosePercentage);
                    nextLine[CSV_INDEXES.get("LOOSE_NUMBER")]=Integer.toString(nombreLoose);


                    nextLine[CSV_INDEXES.get("GAME_NUMBER")] = Integer.toString(totalGames+1);
                }

                data.add(nextLine);
            }

            if(!playerHasBeenAdded) {
                if(stats[CSV_INDEXES.get("WIN_PERCENTAGE")].equals("Oui")) {//win
                    data.add(new String[] {stats[1], "1", "100.0", "0", "0.0", "0", "0.0", "1"});//totalGames=1 game because this could happen at first game
                }
                else if(stats[CSV_INDEXES.get("TIE_PERCENTAGE")].equals("Oui")) {//tie
                    data.add(new String[] {stats[1], "0", "0.0", "1", "100.0", "0", "0.0", "1"});//totalGames=1 game because this could happen at first game
                }
                else {//loose
                    data.add(new String[] {stats[1], "0", "0.0", "0", "0.0", "1", "100.0", "1"});//totalGames=1 game because this could happen at first game
                }

            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
            // Gérer l'exception selon la logique métier
        }

        return data;
    }
    public void setTieForPlayer(Player joueur) {
        playerHasTieGameArray[joueur.getId()]=true;
    }
}
    
