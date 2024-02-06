package fr.cotedazur.univ.polytech.citadellesgroupeq.playerevaluator;

import fr.cotedazur.univ.polytech.citadellesgroupeq.CardDeck;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BestScoreCalculator {

    private BestScoreCalculator() {
        //useless, but removes sonar warning
    }

    public static int[] getWinPercentagePerPlayer(List<Class<? extends Player>> playerClasses) {
        //array is 1 index larger, to support tie games
        int[] winPerPlayerIdArray=new int[playerClasses.size()+1];//initialized at 0 by default

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
                    throw new IllegalArgumentException("pas de constructeur prenant un int et un DistrictJSONReader en paramètre trouvé pour la classe " + playerStrategy.getName() + "!");
                }
            }

            GameLogicManager game = new GameLogicManager(players);
            game.setCardDeck(deck);

            while (!game.isFinished()) {
                game.makeAllPlayersSelectRole();
                for (Player joueur : game.getPlayerTreeSet()) {
                    game.playPlayerTurn(joueur);
                }
                game.resuscitateAllPlayers();
            }
            Optional<Player> optionalWinner = game.whoIsTheWinner();
            optionalWinner.ifPresentOrElse(player -> winPerPlayerIdArray[player.getId()]++, () -> winPerPlayerIdArray[winPerPlayerIdArray.length-1]++);//compte la victoire seulement si présent
        }

        for(int i=0; i < winPerPlayerIdArray.length; i++) {
            winPerPlayerIdArray[i]/=10;//passage aux pourcents
        }

        return winPerPlayerIdArray;
    }
}
