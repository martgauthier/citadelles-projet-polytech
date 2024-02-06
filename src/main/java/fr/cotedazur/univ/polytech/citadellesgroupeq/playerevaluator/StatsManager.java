package fr.cotedazur.univ.polytech.citadellesgroupeq.playerevaluator;

import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;

import java.util.*;

/**
 * Cette classe s'occupe de stocker par joueur une liste de summary.
 */
public class StatsManager {
    private Map<Player, List<RoundSummary>> playerSummaries;
    private String[] stats;
    public StatsManager(List<Player> playersList) {
        playerSummaries = new HashMap<>();
        for (Player player : playersList) {
            playerSummaries.put (player, new ArrayList<>());
        }
        stats= new String[6];
        Arrays.fill(stats,"");
    }

    public void addSummary(Player playerName, RoundSummary summary) {
        List<RoundSummary> playerSummary = playerSummaries.get(playerName);
        playerSummary.add(summary);
    }

    public Map<Player, List<RoundSummary>> getPlayerSummaries() {
        return playerSummaries;
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
     * Cette méthode permet de fournir la ligne de stat d'un joueur
     * @param player le joueur concerné
     * @return un String[] des différentes stats de la partie
     */
    public String[] getStatForAPlayer(Player player, int round){
        stats[0] = "" + (round + 1);
        stats[1] = player.getBotLogicName();
        setStatOfNumberOfDeath(player);
        return stats;
    }

    public void writePlayersStatInCsv(GameStatsCsv csv, int round){
        for(Player player : playerSummaries.keySet()){
            // On récupère la stat de chaque joueur pour une partie
            String[] statForOnePlayer = getStatForAPlayer(player, round);
            // On les écrit dans le csv
            csv.writeInCsvFile(statForOnePlayer);
        }
    }
}
    
