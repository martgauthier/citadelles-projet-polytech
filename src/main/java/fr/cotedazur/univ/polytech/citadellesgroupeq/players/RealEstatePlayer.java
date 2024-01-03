package fr.cotedazur.univ.polytech.citadellesgroupeq.players;

import fr.cotedazur.univ.polytech.citadellesgroupeq.District;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * La classe alwaysSpendPlayer représente un joueur spécifique dans le jeu Citadelles,
 * qui choisit toujours de dépenser ou de récupérer des pièces lors de son tour.
 * Cette classe hérite de la classe abstraite {@link Player}.
 */
public class RealEstatePlayer extends Player {

    public RealEstatePlayer(int id) {
        super(id);
    }

    public RealEstatePlayer(int id, int cash, List<District> cards) {
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
            super.buyDistrictsDuringTurn(summary);
        }
    }

    @Override
    public Optional<District> getChoosenDistrictToBuy() {
        List<District> buyableDistricts =getBuyableCards();

        if(buyableDistricts.isEmpty()) return Optional.empty();

        return Optional.of(Collections.min(buyableDistricts));
    }

    @Override
    public Player selectPlayerToExchangeCardsWithAsMagicien(List<Player> playersList) {
        Player selectedPlayer=playersList.get(0);
        while(selectedPlayer==this) {
            selectedPlayer=playersList.get(randomGenerator.nextInt(playersList.size()));//joueur aléatoire, pas de logique particulière pour l'instant
        }

        return selectedPlayer;
    }

    @Override
    public boolean choosesToExchangeCardWithPlayer() {
        return randomGenerator.nextBoolean();//pas de logique particulière à ce sujet
    }

    @Override
    public int[] selectCardsToExchangeWithPileAsMagicien() {//Randomize it
        int start= randomGenerator.nextInt(getCardsInHand().size());
        int end= randomGenerator.nextInt(start, getCardsInHand().size());
        int size = end - start + 1;

        int[] returnedArray = new int[size];

        // Fill the array with values between start and end
        for (int i = 0; i < size; i++) {
            returnedArray[i] = start + i;
        }

        return returnedArray;
    }
}
