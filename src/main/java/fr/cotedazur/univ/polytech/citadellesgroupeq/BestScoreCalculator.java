package fr.cotedazur.univ.polytech.citadellesgroupeq;

import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.ThomasPlayer;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BestScoreCalculator {
    @Test
    public void getWinningRatioPerPlayer() {
        Map<String, Integer> winPerPlayerMap=new HashMap<>();

        for(int i=0; i < 1000; i++) {
            try {
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
            catch (Exception e) {
                throw new RuntimeException(e.toString());
            }
        }

        for(Map.Entry<String, Integer> entry: winPerPlayerMap.entrySet()) {
            System.out.println(entry.getKey()+": " + entry.getValue()/10 + "%"); //  /1000 * 100 => percentage
        }
    }

    /**
     * WARNING: REALLY LONG TO RUN !!! (5 minutes sur le pc de gauthier)
     */
     @Test
    public void getBestCombinaisonForThomasPlayer() {
        Map<String, Integer> winPerPlayerMap;
        Integer maxScore=0;
        int idMaxScore=0;
        int maxPickMoreThan=1;
        List<List<Role>> combinaisons=List.of(
                List.of(Role.ASSASSIN, Role.ARCHITECTE, Role.EVEQUE),
                List.of(Role.ASSASSIN, Role.ARCHITECTE, Role.ROI),
                List.of(Role.ASSASSIN, Role.ARCHITECTE, Role.VOLEUR),
                List.of(Role.VOLEUR, Role.ROI, Role.CONDOTTIERE),
                List.of(Role.ARCHITECTE, Role.VOLEUR, Role.ROI),
                List.of(Role.ARCHITECTE, Role.MARCHAND, Role.EVEQUE),
                List.of(Role.ROI, Role.VOLEUR, Role.CONDOTTIERE),
                List.of(Role.ARCHITECTE, Role.MARCHAND, Role.CONDOTTIERE),
                List.of(Role.CONDOTTIERE, Role.ARCHITECTE, Role.MARCHAND)
        );

        for(int j=0; j<combinaisons.size(); j++) {
            System.out.println("COMBINAISON N°" + j);
            for(int k=1; k<7; k++) {
                winPerPlayerMap=new HashMap<>();
                System.out.println("TENTATIVE k:" + k);
                ThomasPlayer.ROLES_TO_PICK_IN_ORDER=combinaisons.get(j);
                ThomasPlayer.PICK_CARD_FOR_MORE_THAN=k;
                for(int i=0; i < 1000; i++) {
                    GameLogicManager game = new GameLogicManager();
                    while(!game.isFinished()) {
                        game.makeAllPlayersSelectRole();
                        for(Player joueur: game.getPlayerTreeSet()) {
                            game.playPlayerTurn(joueur);
                        }
                        game.resuscitateAllPlayers();
                    }
                    winPerPlayerMap.put(game.whoIsTheWinner().getBotLogicName(), winPerPlayerMap.getOrDefault(game.whoIsTheWinner().getBotLogicName(), 0) + 1);
                }

                if(winPerPlayerMap.getOrDefault("ThomasPlayer", 0)/10 > maxScore) {
                    idMaxScore=j;
                    maxScore= winPerPlayerMap.get("ThomasPlayer")/10;
                    maxPickMoreThan=k;
                }
            }
        }

        System.out.println("La meilleure combinaison est l'id: " + idMaxScore);
        System.out.println("Avec un taux de : " + maxScore + "%");
        System.out.println("Et un pickmorethan de : " + maxPickMoreThan);
    }
}
