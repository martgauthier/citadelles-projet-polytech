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

    private Optional<Player> playerWinning;

    public StatsManager(List<Player> playersList) {
        playerSummaries = new HashMap<>();
        for (Player player : playersList) {
            playerSummaries.put (player, new ArrayList<>());
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

    private boolean isFirstTime=false;

    public void updatePlayerStatInCsv(GameStatsCsv csvToUpdate, GameLogicManager game, int round){
        csvToUpdate.createCsvFile();//works perfectly
        for(Player player : playerSummaries.keySet()){
            // On récupère la stat de chaque joueur pour une partie
            String[] statForOnePlayer = getStatForAPlayer(player,game, round);//works

            List<String[]> data=getUpdatedStatsLine(csvToUpdate.getReaderOfResumeStatsCsv(), statForOnePlayer); // La on, récupère le petit csv à modif

            List<String[]> dataWithoutHeader=new ArrayList<>(data);
            dataWithoutHeader.remove(0);

            boolean containsTieGame=dataWithoutHeader.stream().anyMatch(array -> !array[3].equals("0.0"));


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

            while ((nextLine = csvReader.readNext()) != null) {
                if (nextLine[0].equals(stats[1])) {
                    int totalGames = Integer.parseInt(nextLine[4]);

                    double winPercentage = Double.parseDouble(nextLine[1]);//chiffre en pourcentage: si 50% de victoire, winPercentage=50

                    double tiePercentage = Double.parseDouble(nextLine[3]);
                    double loosePercentage = 100.0d-winPercentage-tiePercentage;

                    int nombreWin=(int) Math.ceil(((double)winPercentage/100.0d) * totalGames);
                    int nombreLoose=(int) Math.ceil(((double)loosePercentage/100.0d) * totalGames);
                    int nombreTie=(int) Math.ceil(((double)tiePercentage/100.0d) * totalGames);

                    if (stats[7].equals("Oui")) {//victoire
                        if(totalGames==0) {
                            nextLine[1] = Double.toString(100.0d);
                            nextLine[2] = Double.toString(0.0d);
                            nextLine[3] = Double.toString(0.0d);
                        }
                        nombreWin++;
                    }
                    else if (stats[8].equalsIgnoreCase("Oui")){//égalité
                        if(totalGames==0) {
                            nextLine[1] = Double.toString(0.0d);
                            nextLine[2] = Double.toString(0.0d);
                            nextLine[3] = Double.toString(100.0d);
                        }
                        nombreTie++;
                    }
                    else {//loose
                        if(totalGames==0) {
                            nextLine[1]=Double.toString(0.0d);
                            nextLine[2]=Double.toString(100.0d);
                            nextLine[3]=Double.toString(0.0d);
                        }
                        nombreLoose++;
                    }



                    double newWinPercentage = (((double) nombreWin) /(totalGames+1)) * 100;
                    nextLine[1] = Double.toString(newWinPercentage);

                    double newTiePercentage = (((double) nombreTie) / (totalGames+1)) * 100;
                    nextLine[3] = Double.toString(newTiePercentage);

                    double newLoosePercentage = 100.0d - newWinPercentage - newTiePercentage;
                    nextLine[2] = Double.toString(newLoosePercentage);


                    nextLine[4] = Integer.toString(totalGames+1);
                }

                data.add(nextLine);
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
    
