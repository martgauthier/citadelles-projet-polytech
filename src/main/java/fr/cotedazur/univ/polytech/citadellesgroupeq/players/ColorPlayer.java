package fr.cotedazur.univ.polytech.citadellesgroupeq.players;

import fr.cotedazur.univ.polytech.citadellesgroupeq.*;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ColorPlayer extends Player {
    public ColorPlayer(int id, CardDeck pioche) {
        super(id, pioche);
    }

    public ColorPlayer(int id, int cash, List<District> cards, CardDeck pioche) {
        super(id, cash, cards, false, pioche);
    }

    @Override
    public int selectAndSetRole(List<Role> availableRoles, List<Player> playerList) {
        if(availableRoles.isEmpty()) throw new IllegalArgumentException("availableRoles cannot be empty.");

        for(int i=0; i < availableRoles.size(); i++) {
            if(availableRoles.get(i).getColor() != Color.GRAY) {
                setRole(availableRoles.get(i));
                return i;
            }
        }
        setRole(availableRoles.get(0));
        return 0;//if no roles had color
    }

    /**
     * Il essaye toujours de choisir un role qui a une couleur (si aucun n'est dispo, il prend le premier qui vient)
     * Il pioche des cartes citadelles si son nombre de cartes en main est inferieur à 4
     * Sinon, il pioche des pièces
     * À tous les tours, il essaye de poser la carte qui a une couleur. S'il y en a plusieurs, il tente avec la moins chere. S'il n'y en pas, il essaye d'acheter la premiere carte qu'il a
     * @param summary summary
     * @param game game
     */
    @Override
    public void playTurn(RoundSummary summary, GameLogicManager game) {
        getCoinsFromColorCards(summary);

        getRole().power(game, this, summary);//it is no duplicate, as another Player logic could decide not to use its power.

        if(getCardsInHand().size() < 4) {
            if(!haveObservatoryInCity()){
                pickCard(summary);
            }
        }
        else {
            draw2Coins(summary);
        }


        PowerManager powerManager = new PowerManager(game);
        powerManager.applyCityPowers(this, summary);

        buyDistrictsDuringTurn(summary);
    }

    @Override
    public String getBotLogicName() {
        return "ColorPlayer";
    }

    @Override
    public Optional<District> getChoosenDistrictToBuy() {
        Optional<District> minCardWithColor=Optional.empty();

        for(District card: getBuyableCards()) {
            if(card.getColor() == getRole().getColor() && (minCardWithColor.isEmpty() || card.compareTo(minCardWithColor.get()) < 0)) {
                minCardWithColor=Optional.of(card);
            }
        }

        if(minCardWithColor.isPresent() && minCardWithColor.get().getCost() <= getCash()) {
            return minCardWithColor;
        }
        else if(!getBuyableCards().isEmpty()){//No card with color are buyable, but some cards are present in hand. Let's try to buy the cheapest.
            District minCard=Collections.min(getBuyableCards());
            if(minCard.getCost() <= getCash()){
                return Optional.of(minCard);
            }
        }

        return Optional.empty();//if no cards were buyable
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
