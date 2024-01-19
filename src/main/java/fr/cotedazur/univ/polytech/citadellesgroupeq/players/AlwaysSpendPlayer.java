package fr.cotedazur.univ.polytech.citadellesgroupeq.players;

import fr.cotedazur.univ.polytech.citadellesgroupeq.District;
import fr.cotedazur.univ.polytech.citadellesgroupeq.PowerManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.strategies.PreventArchitectStrategy;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * La classe alwaysSpendPlayer représente un joueur spécifique dans le jeu Citadelles,
 * qui choisit toujours de dépenser ou de récupérer des pièces lors de son tour.
 * Cette classe hérite de la classe abstraite {@link Player}.
 */
public class AlwaysSpendPlayer extends Player {

    public AlwaysSpendPlayer(int id) {
        super(id);
        botLogicName="AlwaysSpendPlayer";
    }

    public AlwaysSpendPlayer(int id, int cash, List<District> cards) {
        super(id, cash, cards, false);
        botLogicName="AlwaysSpendPlayer";
    }

    /**
     * Méthode qui définit la logique du tour d'un joueur alwaysSpendPlayer.
     *
     * @param summary Résumé du tour actuel.
     * @param game
     */
    @Override
    public void playTurn(RoundSummary summary, GameLogicManager game) {
        super.getCoinsFromColorCards(summary);

        getRole().power(game, this, summary);//it is no duplicate, as another Player logic could decide not to use its power

        if (!getCardsInHand().isEmpty()) {
            draw2Coins(summary);
            buyDistrictsDuringTurn(summary);
        }
        else {
            pickCard(summary);
        }


        PowerManager powerManager = new PowerManager(game);
        powerManager.applyCityPowers(this, summary);
    }

    @Override
    public String getBotLogicName() {
        return "AlwaysSpendPlayer";
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

}
