package fr.cotedazur.univ.polytech.citadellesgroupeq.players;

import fr.cotedazur.univ.polytech.citadellesgroupeq.District;
import fr.cotedazur.univ.polytech.citadellesgroupeq.CardDeck;
import fr.cotedazur.univ.polytech.citadellesgroupeq.PowerManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class RandomPlayer extends Player {
    public RandomPlayer(int id, CardDeck pioche) {
        super(id, pioche);
    }

    public RandomPlayer(int id, int cash, List<District> cards, CardDeck pioche) {
        super(id, cash, cards, false, pioche);
    }

    @Override
    public Optional<District> getChoosenDistrictToBuy() {
        List<District> buyableDistricts =getBuyableCards();

        if(buyableDistricts.isEmpty()) return Optional.empty();

        return Optional.of(Collections.min(buyableDistricts));
    }

    /**
     * Methode qui definit la logique du tour d'un joueur alwaysSpendPlayer.
     *
     * @param summary Resume du tour actuel.
     */
    @Override
    public void playTurn(RoundSummary summary, GameLogicManager game) {
        super.getCoinsFromColorCards(summary);

        getRole().power(game, this, summary);//it is no duplicate, as another Player logic could decide not to use its power.

        int randomChoice = getRandomGenerator().nextInt(2);

        if (randomChoice == 1) {
            draw2Coins(summary);
        }
        else{
            if(!haveObservatoryInCity()){
                pickCard(summary);
            }
        }

        PowerManager powerManager = new PowerManager(game);
        powerManager.applyCityPowers(this, summary);

        buyDistrictsDuringTurn(summary);
    }

    @Override
    public String getBotLogicName() {
        return "RandomPlayer";
    }


    @Override
    public Player selectPlayerToExchangeCardsWithAsMagicien(List<Player> playersList) {
        Player selectedPlayer=playersList.get(0);
        while(selectedPlayer==this) {
            selectedPlayer=playersList.get(getRandomGenerator().nextInt(playersList.size()));//joueur aléatoire, pas de logique particulière pour l'instant
        }

        return selectedPlayer;
    }

    @Override
    public boolean choosesToExchangeCardWithPlayer() {
        return getRandomGenerator().nextBoolean();//pas de logique particulière à ce sujet
    }
}
