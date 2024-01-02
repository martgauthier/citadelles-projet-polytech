package fr.cotedazur.univ.polytech.citadellesgroupeq.players;

import fr.cotedazur.univ.polytech.citadellesgroupeq.Citadel;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;

import java.util.List;
import java.util.Optional;

public class RandomPlayer extends Player {
    public RandomPlayer(int id) {
        super(id);
    }

    public RandomPlayer(int id, int cash, List<Citadel> cards) {
        super(id, cash, cards, false);
    }

    /**
     * Méthode qui définit la logique du tour d'un joueur alwaysSpendPlayer.
     *
     * @param summary Résumé du tour actuel.
     */
    @Override
    public void playPlayerTurn(RoundSummary summary, GameManager game) {
        super.playPlayerTurn(summary, game);

        getRole().power(game, this, summary);

        int randomChoice = randomGenerator.nextInt(2);

        if (randomChoice == 1) {
            draw2Coins(summary);
        }
        else{
            pickCard(summary);
        }
        Optional<Citadel> choosenCitadel = getChoosenCitadelToBuy();
        if (choosenCitadel.isPresent()) {
            Citadel citadel = choosenCitadel.get();
            addCitadelToCity(citadel);
            summary.addBoughtCitadel(citadel);
            removeCardFromHand(citadel);
            removeCoins(citadel.getCost());
        }
    }

}
