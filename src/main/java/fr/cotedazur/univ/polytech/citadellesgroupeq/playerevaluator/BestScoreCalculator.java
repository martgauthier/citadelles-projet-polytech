package fr.cotedazur.univ.polytech.citadellesgroupeq.playerevaluator;

import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;

import java.util.HashMap;
import java.util.Map;

public class BestScoreCalculator {
    public static void main(String[] args) {
        Map<String, Integer> winPerPlayerMap=new HashMap<>();

        for(int i=0; i < 3000; i++) {
            GameLogicManager game = new GameLogicManager();
            while (!game.isFinished()) {
                game.makeAllPlayersSelectRole();
                for (Player joueur : game.getPlayerTreeSet()) {
                    game.playPlayerTurn(joueur);
                }
                game.resuscitateAllPlayers();
            }
            winPerPlayerMap.put(game.whoIsTheWinner().getBotLogicName(), winPerPlayerMap.getOrDefault(game.whoIsTheWinner().getBotLogicName(), 0) + 1);
        }

        for(Map.Entry<String, Integer> entry: winPerPlayerMap.entrySet()) {
            System.out.println(entry.getKey()+": " + entry.getValue()/30 + "%"); //  /3000 * 100 => percentage
        }
    }
}
