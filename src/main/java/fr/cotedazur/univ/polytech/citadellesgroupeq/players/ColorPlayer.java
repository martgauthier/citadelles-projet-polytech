package fr.cotedazur.univ.polytech.citadellesgroupeq.players;

import fr.cotedazur.univ.polytech.citadellesgroupeq.Citadel;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Color;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ColorPlayer extends Player {
    public ColorPlayer(int id) {
        super(id);
    }

    public ColorPlayer(int id, int cash, List<Citadel> cards) {
        super(id, cash, cards, false);
    }

    @Override
    public int selectRole(List<Role> availableRoles) {
        if(availableRoles.isEmpty()) throw new IllegalArgumentException("availableRoles cannot be empty.");

        for(int i=0; i < availableRoles.size(); i++) {
            if(availableRoles.get(i).getColor() != Color.GRAY) {
                setRole(availableRoles.get(i));
                return i;
            }
        }
        return 0;//if no roles had color
    }

    /**
     * Il essaye toujours de choisir un rôle qui a une couleur (si aucun n'est dispo, il prend le premier qui vient)
     * Il pioche des cartes citadelles si son nombre de cartes en main est inférieur à 4
     * Sinon, il pioche des pièces
     * A tous les tours, il essaye de poser la carte qui a une couleur. Si il y en a plusieurs, il tente avec la moins chère. SI il n'y en pas, il essaye d'acheter la première carte qu'il a
     * @param summary
     * @param game
     */
    @Override
    public void playPlayerTurn(RoundSummary summary, GameManager game) {
        super.playPlayerTurn(summary, game);

        getRole().power(game, this, summary);
        summary.setHasUsedPower();

        if(getCardsInHand().size() < 4) {
            pickCard(summary);
        }
        else {
            draw2Coins(summary);
        }

        Optional<Citadel> choosenCitadelToBuy=getChoosenCitadelToBuy();

        if(choosenCitadelToBuy.isPresent()) {
            Citadel citadel = choosenCitadelToBuy.get();
            addCitadelToCity(citadel);
            summary.addBoughtCitadel(citadel);
            removeCardFromHand(citadel);
            removeCoins(citadel.getCost());
        }
    }

    @Override
    public Optional<Citadel> getChoosenCitadelToBuy() {
        Optional<Citadel> minCardWithColor=Optional.empty();

        for(Citadel card: getCardsInHand()) {
            if(card.getColor()!=Color.GRAY && card.getColor()!=Color.PURPLE && (minCardWithColor.isEmpty() || card.compareTo(minCardWithColor.get()) < 0)) {
                minCardWithColor=Optional.of(card);
            }
        }

        if(minCardWithColor.isPresent() && minCardWithColor.get().getCost() <= getCash()) {
            return minCardWithColor;
        }
        else if(!getCardsInHand().isEmpty()){//no card with color are buyable, but some cards are present in hand. Let's try to buy the cheapest
            Citadel minCard=Collections.min(getCardsInHand());
            if(minCard.getCost() <= getCash()){
                return Optional.of(minCard);
            }
        }

        return Optional.empty();//if no cards were buyable
    }
}
