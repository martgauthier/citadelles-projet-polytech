package fr.cotedazur.univ.polytech.citadellesgroupeq;

import java.util.List;
import java.util.Optional;

/**
 * La classe alwaysSpendPlayer représente un joueur spécifique dans le jeu Citadelles,
 * qui choisit toujours de dépenser ou de récupérer des pièces lors de son tour.
 * Cette classe hérite de la classe abstraite {@link Player}.
 */
public class alwaysSpendPlayer extends Player{

    public alwaysSpendPlayer(int id) {
        super(id);
    }

    public alwaysSpendPlayer(int id, int cash, List<Citadel> cards) {
        super(id, cash, cards);
    }

    /**
     * Méthode qui définit la logique du tour d'un joueur alwaysSpendPlayer.
     *
     * @param summary Résumé du tour actuel.
     */
    @Override
    public void playerTurn(RoundSummary summary) {
        if (!getCardsInHand().isEmpty()) {
            draw2Coins(summary);
            Optional<Citadel> choosenCitadel = getChoosenCitadelToBuy();
            if (choosenCitadel.isPresent()) {
                Citadel citadel = choosenCitadel.get();
                addCitadelToCity(citadel);
                summary.addBoughtCitadel(citadel);
                removeCardFromHand(citadel);
                removeCoins(citadel.getCost());
            }
        }
        else{
            pickCard(summary);
        }
    }


}
