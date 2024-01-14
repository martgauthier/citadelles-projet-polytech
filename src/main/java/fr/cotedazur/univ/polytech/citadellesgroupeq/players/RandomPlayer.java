package fr.cotedazur.univ.polytech.citadellesgroupeq.players;

import fr.cotedazur.univ.polytech.citadellesgroupeq.District;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class RandomPlayer extends Player {
    public RandomPlayer(int id) {
        super(id);
    }

    public RandomPlayer(int id, int cash, List<District> cards) {
        super(id, cash, cards, false);
    }

    @Override
    public Optional<District> getChoosenDistrictToBuy() {
        List<District> buyableDistricts =getBuyableCards();

        if(buyableDistricts.isEmpty()) return Optional.empty();

        return Optional.of(Collections.min(buyableDistricts));
    }

    /**
     * Méthode qui définit la logique du tour d'un joueur alwaysSpendPlayer.
     *
     * @param summary Résumé du tour actuel.
     */
    @Override
    public void playPlayerTurn(RoundSummary summary, GameLogicManager game) {
        super.getCoinsFromColorCards(summary);

        getRole().power(game, this, summary);//it is no duplicate, as another Player logic could decide not to use its power

        int randomChoice = randomGenerator.nextInt(2);

        if (randomChoice == 1) {
            draw2Coins(summary);
        }
        else{
            pickCard(summary);
        }

        buyDistrictsDuringTurn(summary);
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
}
