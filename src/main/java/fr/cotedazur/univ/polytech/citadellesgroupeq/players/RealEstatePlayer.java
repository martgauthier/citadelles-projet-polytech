package fr.cotedazur.univ.polytech.citadellesgroupeq.players;

import fr.cotedazur.univ.polytech.citadellesgroupeq.Citadel;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;

import java.util.List;

/**
 * La classe alwaysSpendPlayer représente un joueur spécifique dans le jeu Citadelles,
 * qui choisit toujours de dépenser ou de récupérer des pièces lors de son tour.
 * Cette classe hérite de la classe abstraite {@link Player}.
 */
public class RealEstatePlayer extends Player {

    public RealEstatePlayer(int id) {
        super(id);
    }

    public RealEstatePlayer(int id, int cash, List<Citadel> cards) {
        super(id, cash, cards, false);
    }

    /**
     * Méthode qui définit la logique du tour d'un joueur alwaysSpendPlayer.
     *
     * @param summary Résumé du tour actuel.
     * @param game
     */
    @Override
    public void playPlayerTurn(RoundSummary summary, GameManager game) {
        super.getCoinsFromColorCards(summary);

        getRole().power(game, this, summary);

        if (getCardsInHand().size() != 8) {
            pickCard(summary);
        }
        else {
            draw2Coins(summary);
            super.buyCitadelsDuringTurn(summary);
        }
    }

}
