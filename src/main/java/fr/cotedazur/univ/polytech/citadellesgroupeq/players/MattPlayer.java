package fr.cotedazur.univ.polytech.citadellesgroupeq.players;

import fr.cotedazur.univ.polytech.citadellesgroupeq.*;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.strategies.MattStartGameStrategy;
import fr.cotedazur.univ.polytech.citadellesgroupeq.strategies.SecurePointsForEndGame;

import java.util.List;
import java.util.Optional;

public class MattPlayer extends Player {

    public MattPlayer(int id, CardDeck pioche) {
        super(id, pioche);
        setStrategy(new MattStartGameStrategy(this));
    }
    @Override
    public String getBotLogicName() {
        return "MattPlayer";
    }

    @Override
    public void playTurn(RoundSummary summary, GameLogicManager game) {
        if(game.isFinished()) {
            setStrategy(new SecurePointsForEndGame(this));
        }

        getCoinsFromColorCards(summary);

        getRole().power(game, this, summary);

        if(getRole()==Role.MARCHAND) {
            pickCard(summary);
        }
        else if(getRole()==Role.ARCHITECTE) {
            draw2Coins(summary);
        }
        else {
            if(getCash() < 4) draw2Coins(summary);
            else pickCard(summary);
        }


        PowerManager powerManager = new PowerManager(game);
        powerManager.applyCityPowers(this, summary);

        buyDistrictsDuringTurn(summary);
    }

    @Override
    public boolean choosesToExchangeCardWithPlayer() {
        return getCardsInHand().size() <= 3;
    }

    @Override
    public Player selectPlayerToExchangeCardsWithAsMagicien(List<Player> playerList) {
        for(Player joueur: playerList) {
            if(joueur.isCloseToWin() && joueur!=this) {
                return joueur;
            }
        }
        return (playerList.get(0) == this) ? playerList.get(1) : playerList.get(0);
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
        if (playerList.stream().anyMatch(joueur -> joueur.isCloseToWin() && joueur != this) && availableRoles.contains(Role.MAGICIEN) && getCardsInHand().size() <= 3) {
            setRole(Role.MAGICIEN);
            return availableRoles.indexOf(Role.MAGICIEN);
        } else {
            if (availableRoles.contains(Role.MARCHAND)) {
                setRole(Role.MARCHAND);
                return availableRoles.indexOf(Role.MARCHAND);
            } else if (availableRoles.contains(Role.ARCHITECTE)) {
                setRole(Role.ARCHITECTE);
                return availableRoles.indexOf(Role.ARCHITECTE);
            } else if (availableRoles.contains(Role.EVEQUE)) {
                setRole(Role.EVEQUE);
                return availableRoles.indexOf(Role.EVEQUE);
            } else {
                setRole(availableRoles.get(0));
                return 0;
            }
        }
    }
}
