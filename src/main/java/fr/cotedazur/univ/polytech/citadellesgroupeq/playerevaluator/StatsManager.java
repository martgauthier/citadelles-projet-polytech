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

/**
 * Cette classe s'occupe de stocker par joueur une liste de summary.
 */
public class StatsManager {
    private final Map<Player, List<RoundSummary>> playerSummaries;


    private final String[] stats;

    private final boolean[] playerHasTieGameArray;

    public static final String WIN_STRING="100.0";

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
    public Map<Player, List<RoundSummary>> getPlayerSummaries() { return playerSummaries; }

    public void updatePlayerStatInCsv(GameStatsCsv csvToUpdate, GameLogicManager game, int round){
        csvToUpdate.createCsvFile(game.getPlayersList());//works perfectly
        for(Player player : playerSummaries.keySet()){
            // On récupère la stat de chaque joueur pour une partie
            String[] statForOnePlayer = getStatForAPlayer(player,game, round);//works

            try {
                CSVReader reader = csvToUpdate.getReaderOfResumeStatsCsv();

                List<String[]> data = getUpdatedStatsLine(reader, statForOnePlayer); // La on, récupère le petit csv à modif

                reader.close();
                csvToUpdate.writeInCsvFile(data);
            }
            catch(IOException e) {
                throw new IllegalStateException(e);
            }
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
                if (nextLine[CSVLineIndexes.PLAYER_NAME.ordinal()].equals(stats[1])) {
                    playerHasBeenAdded=true;
                    int totalGames = Integer.parseInt(nextLine[CSVLineIndexes.GAME_NUMBER.ordinal()]);

                    int nombreWin=Integer.parseInt(nextLine[CSVLineIndexes.WIN_NUMBER.ordinal()]);
                    int nombreTie=Integer.parseInt(nextLine[CSVLineIndexes.TIE_NUMBER.ordinal()]);
                    int nombreLoose=Integer.parseInt(nextLine[CSVLineIndexes.LOOSE_NUMBER.ordinal()]);

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
                    nextLine[CSVLineIndexes.WIN_PERCENTAGE.ordinal()] = Double.toString(newWinPercentage);
                    nextLine[CSVLineIndexes.WIN_NUMBER.ordinal()]=Integer.toString(nombreWin);

                    double newTiePercentage = (((double) nombreTie) / (totalGames+1)) * 100;
                    nextLine[CSVLineIndexes.TIE_PERCENTAGE.ordinal()] = Double.toString(newTiePercentage);
                    nextLine[CSVLineIndexes.TIE_NUMBER.ordinal()]=Integer.toString(nombreTie);

                    double newLoosePercentage = (((double) nombreLoose) / (totalGames+1)) * 100;
                    nextLine[CSVLineIndexes.LOOSE_PERCENTAGE.ordinal()] = Double.toString(newLoosePercentage);
                    nextLine[CSVLineIndexes.LOOSE_NUMBER.ordinal()]=Integer.toString(nombreLoose);


                    nextLine[CSVLineIndexes.GAME_NUMBER.ordinal()] = Integer.toString(totalGames+1);
                }

                data.add(nextLine);
            }

            if(!playerHasBeenAdded) {
                if(stats[7].equals("Oui")) {//win
                    data.add(new String[] {stats[1], "1", WIN_STRING, "0", "0.0", "0", "0.0", "1"});//totalGames=1 game because this could happen at first game
                }
                else if(stats[8].equals("Oui")) {//tie
                    data.add(new String[] {stats[1], "0", "0.0", "1", WIN_STRING, "0", "0.0", "1"});//totalGames=1 game because this could happen at first game
                }
                else {//loose
                    data.add(new String[] {stats[1], "0", "0.0", "0", "0.0", "1", WIN_STRING, "1"});//totalGames=1 game because this could happen at first game
                }

            }
        } catch (IOException | CsvValidationException e) {
            throw new IllegalStateException("Can't write this data in CSV !");
        }

        return data;
    }
    public void setTieForPlayer(Player joueur) {
        playerHasTieGameArray[joueur.getId()]=true;
    }

    public Optional<Player> getPlayerWinning() { return playerWinning; }

    public boolean[] getPlayerHasTieGameArray() { return playerHasTieGameArray; }
}
    
