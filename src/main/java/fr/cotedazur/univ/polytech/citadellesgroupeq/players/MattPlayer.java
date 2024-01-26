package fr.cotedazur.univ.polytech.citadellesgroupeq.players;

import fr.cotedazur.univ.polytech.citadellesgroupeq.*;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;

import java.util.List;
import java.util.Optional;

public class MattPlayer extends Player {

    public MattPlayer(int id, CardDeck pioche) {
        super(id, pioche);
    }
    @Override
    public String getBotLogicName() {
        return "MattPlayer";
    }

    @Override
    public void playTurn(RoundSummary summary, GameLogicManager game) {
        getCoinsFromColorCards(summary);

        getRole().power(game, this, summary);

        //pickCard(summary);
        //or
        //draw2Coins(summary);



        //            buyDistrictsDuringTurn(summary);


        PowerManager powerManager = new PowerManager(game);
        powerManager.applyCityPowers(this, summary);
    }

    @Override
    public boolean choosesToExchangeCardWithPlayer() {
        return false;
    }

    @Override
    public Player selectPlayerToExchangeCardsWithAsMagicien(List<Player> playerList) {
        return null;
    }


    /**
     * Choisit le district selon ces règles:
     * -Achetable
     * -De la couleur verte (marchand) si possible
     * La carte dont le prix est le plus proche de 3
     * @return
     */
    @Override
    public Optional<District> getChoosenDistrictToBuy() {
        Optional<District> boughtCard=Optional.empty();
        for(District card: getBuyableCards()) {
            if(boughtCard.isEmpty()) {
                boughtCard=Optional.of(card);
            }
            else if(card.getColor()==Color.GREEN) {//marchand
                if(boughtCard.get().getColor()!=Color.GREEN || Math.abs(3-boughtCard.get().getCost()) >= Math.abs(3-card.getCost())) {//le prix le plus proche de 3
                    boughtCard=Optional.of(card);
                }
            }
            else if(boughtCard.get().getColor()!=Color.GREEN && (Math.abs(3-boughtCard.get().getCost()) >= Math.abs(3-card.getCost()))) {
                    boughtCard=Optional.of(card);

            }
        }
        return boughtCard;
    }

    @Override
    public int selectAndSetRole(List<Role> availableRoles, List<Player> playerList) {
        if(availableRoles.contains(Role.MARCHAND)) {
            setRole(Role.MARCHAND);
            return availableRoles.indexOf(Role.MARCHAND);
        }
        else if(availableRoles.contains(Role.ARCHITECTE)) {
            setRole(Role.ARCHITECTE);
            return availableRoles.indexOf(Role.ARCHITECTE);
        }
        else if(availableRoles.contains(Role.EVEQUE)) {
            setRole(Role.EVEQUE);
            return availableRoles.indexOf(Role.EVEQUE);
        }
        else {
            setRole(availableRoles.get(0));
            return 0;
        }
    }
}
