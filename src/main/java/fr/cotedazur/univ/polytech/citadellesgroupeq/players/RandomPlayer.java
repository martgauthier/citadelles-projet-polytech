package fr.cotedazur.univ.polytech.citadellesgroupeq.players;

import fr.cotedazur.univ.polytech.citadellesgroupeq.District;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;

import java.util.List;
import java.util.Optional;

public class RandomPlayer extends Player {
    public RandomPlayer(int id) {
        super(id);
    }

    public RandomPlayer(int id, int cash, List<District> cards) {
        super(id, cash, cards, false);
    }

    /**
     * Méthode qui définit la logique du tour d'un joueur alwaysSpendPlayer.
     *
     * @param summary Résumé du tour actuel.
     */
    @Override
    public void playPlayerTurn(RoundSummary summary, GameManager game) {
        super.getCoinsFromColorCards(summary);

        getRole().power(game, this, summary);//it is no duplicate, as another Player logic could decide not to use its power

        int randomChoice = randomGenerator.nextInt(2);

        if (randomChoice == 1) {
            draw2Coins(summary);
        }
        else{
            pickCard(summary);
        }

        super.buyDistrictsDuringTurn(summary);
    }

}
