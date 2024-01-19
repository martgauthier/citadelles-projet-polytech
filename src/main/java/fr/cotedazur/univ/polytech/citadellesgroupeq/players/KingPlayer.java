package fr.cotedazur.univ.polytech.citadellesgroupeq.players;

import fr.cotedazur.univ.polytech.citadellesgroupeq.Color;
import fr.cotedazur.univ.polytech.citadellesgroupeq.District;
import fr.cotedazur.univ.polytech.citadellesgroupeq.PowerManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;

import java.util.List;
import java.util.Optional;

public class KingPlayer extends Player {

    public KingPlayer(int id) {
        super(id);
        botLogicName="KingPlayer";
    }

    private boolean hasCardLessThan4Coins() {
        return getCardsInHand().stream().anyMatch(district -> district.getCost() < 4);
    }

    private boolean hasCardMoreOrEqualThan4Coins() {
        return getCardsInHand().stream().anyMatch(district -> district.getCost() >= 4);
    }

    @Override
    public void playTurn(RoundSummary summary, GameLogicManager game) {
        getCoinsFromColorCards(summary);
        getRole().power(game, this, summary);

        //decide whether to take coins or cards
        if(getCardsInHand().size() < 4 || !(hasCardLessThan4Coins() && hasCardMoreOrEqualThan4Coins())) {
            pickCard(summary);
        }
        else {
            draw2Coins(summary);
        }

        PowerManager powerManager = new PowerManager(game);
        powerManager.applyCityPowers(this, summary);

        buyDistrictsDuringTurn(summary);
    }

    @Override
    public boolean choosesToExchangeCardWithPlayer() {
        return false;
    }

    @Override
    public Player selectPlayerToExchangeCardsWithAsMagicien(List<Player> playerList) {
        return null;
    }

    @Override
    public Optional<District> getChoosenDistrictToBuy() {
        List<District> buyableCards = getBuyableCards();
        Optional<District> boughtDistrict = Optional.empty();
        for(District district: buyableCards) {
            if(boughtDistrict.isEmpty()) {
                boughtDistrict=Optional.of(district);
            }
            else if(boughtDistrict.get().getColor()!=Color.YELLOW &&
                    (district.getColor()==Color.YELLOW ||
                    boughtDistrict.get().getCost() < 4
                    || district.getCost() >= 4
                    || boughtDistrict.get().getCost() < district.getCost()
                    || (district.getCost() < boughtDistrict.get().getCost() && district.getCost() >= 4))) {
                    boughtDistrict=Optional.of(district);//condition compliquée, mais qui résume la condition écrite sur le github
            }
            else {//bought districtt == yellow
                if(district.getColor() == Color.YELLOW && district.getCost() < boughtDistrict.get().getCost()) {
                    boughtDistrict=Optional.of(district);
                }
            }
        }

        return boughtDistrict;
    }


    /**
     * Essaye de prendre le roi, puis l'assassin, sinon le premier rôle venu
     * @param availableRoles les rôles disponibles
     * @param playerList
     * @return
     */
    @Override
    public int selectAndSetRole(List<Role> availableRoles, List<Player> playerList) {
        if(availableRoles.contains(Role.ROI)) {
            setRole(Role.ROI);
            return availableRoles.indexOf(Role.ROI);
        }
        else if(availableRoles.contains(Role.ASSASSIN)){
            setRole(Role.ASSASSIN);
            return availableRoles.indexOf(Role.ASSASSIN);
        }
        else {
            setRole(availableRoles.get(0));
            return 0;//first role available
        }
    }

    /**
     * Essaye de tuer le roi, sinon le premier rôle venu (qui n'est pas lui-même)
     * @param availableRoles les rôles disponible
     * @return
     */
    @Override
    public Role selectRoleToKillAsAssassin(List<Role> availableRoles) {
        if(availableRoles.contains(Role.ROI)) {
            return Role.ROI;
        }
        else if (availableRoles.get(0) == Role.ASSASSIN){
            return availableRoles.get(1);
        }
        else {
            return availableRoles.get(0);
        }
    }
}
