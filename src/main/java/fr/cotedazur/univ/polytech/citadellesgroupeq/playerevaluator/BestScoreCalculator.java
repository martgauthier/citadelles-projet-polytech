package fr.cotedazur.univ.polytech.citadellesgroupeq.playerevaluator;

import fr.cotedazur.univ.polytech.citadellesgroupeq.CardDeck;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.*;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class BestScoreCalculator {
    private static final Logger SCORE_LOGGER=Logger.getLogger("SCORE");

    public static void main(String[] args) {
        List<Class<? extends Player>> playersAgainstThomas=List.of(MattPlayer.class, ThomasPlayer.class, AlwaysSpendPlayer.class, RandomPlayer.class);
        getWinPercentagePerPlayer(playersAgainstThomas);
    }
    public static int[] getWinPercentagePerPlayer(List<Class<? extends Player>> playerClasses) {
        int[] winPerPlayerIdArray=new int[playerClasses.size()];//initialized at 0 by default

        for(int i=0; i < 1000; i++) {
            int ids=0;
            CardDeck deck=new CardDeck();
            List<Player> players=new ArrayList<>();
            for(Class<? extends Player> playerStrategy: playerClasses) {
                try {
                    Constructor<? extends Player> constructor = playerStrategy.getDeclaredConstructor(int.class, CardDeck.class);
                    players.add(constructor.newInstance(ids++, deck));
                }
                catch(Exception e) {
                    throw new RuntimeException("pas de constructeur prenant un int et un DistrictJSONReader en paramètre trouvé pour la classe " + playerStrategy.getName() + "! Erreur de code");
                }
            }

            GameLogicManager game = new GameLogicManager(players);
            StatsManager statsManager = new StatsManager(game.getPlayersList());
            GameStatsCsv csv = new GameStatsCsv();
            game.setCardDeck(deck);

            while (!game.isFinished()) {
                game.makeAllPlayersSelectRole();
                for (Player joueur : game.getPlayerTreeSet()) {
                    RoundSummary summary = game.playPlayerTurn(joueur);
                    statsManager.addSummary(joueur, summary); // on ajoute le RoundSummary a liste des RoundSummary du joueur
                }
                game.resuscitateAllPlayers();
            }
            statsManager.writePlayersStatInCsv(csv,i);
            winPerPlayerIdArray[game.whoIsTheWinner().getId()]++;
        }

        for(int i=0; i < winPerPlayerIdArray.length; i++) {
            winPerPlayerIdArray[i]/=10;
        }

        return winPerPlayerIdArray;
    }
}
