package fr.cotedazur.univ.polytech.citadellesgroupeq;

import java.util.List;
import java.util.Optional;

/**
 * La classe alwaysSpendPlayer représente un joueur spécifique dans le jeu Citadelles,
 * qui choisit toujours de dépenser ou de récupérer des pièces lors de son tour.
 * Cette classe hérite de la classe abstraite {@link Player}.
 */
public class realEstatePlayer extends Player{

    public realEstatePlayer(int id) {
        super(id);
    }

    public realEstatePlayer(int id, int cash, List<Citadel> cards) {
        super(id, cash, cards);
    }

    /**
     * Méthode qui définit la logique du tour d'un joueur alwaysSpendPlayer.
     *
     * @param summary Résumé du tour actuel.
     */
    @Override
    public void playerTurn(RoundSummary summary) {
        super.playerTurn(summary);

        if (getCardsInHand().size() != 8) {
            pickCard(summary);
        }
        else{
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
    }

}
