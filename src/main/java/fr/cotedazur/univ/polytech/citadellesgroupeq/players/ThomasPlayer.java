package fr.cotedazur.univ.polytech.citadellesgroupeq.players;

import fr.cotedazur.univ.polytech.citadellesgroupeq.Color;
import fr.cotedazur.univ.polytech.citadellesgroupeq.District;
import fr.cotedazur.univ.polytech.citadellesgroupeq.PowerManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.strategies.DefaultStrategy;

import java.util.List;
import java.util.Optional;

public class ThomasPlayer extends Player {
    public ThomasPlayer(int id) {
        super(id);
        setStrategy(new DefaultStrategy(this));
    }

    @Override
    public String getBotLogicName() {
        return "ThomasPlayer";
    }

    @Override
    public void playTurn(RoundSummary summary, GameLogicManager game) {
        getCoinsFromColorCards(summary);
        getRole().power(game, this, summary);

        boolean hasCardsOver3Coins=getCardsInHand().stream().anyMatch(district -> district.getCost() >= 3);

        if(hasCardsOver3Coins && getCardsInHand().size() > 2) {
            draw2Coins(summary);
            buyDistrictsDuringTurn(summary);
        }
        else {
            pickCard(summary);
        }

        PowerManager powerManager = new PowerManager(game);
        powerManager.applyCityPowers(this, summary);

        buyDistrictsDuringTurn(summary);
    }

    @Override
    public int selectAndSetRole(List<Role> availableRoles, List<Player> playerList) {
        Role selectedRole=availableRoles.get(0);
        if(availableRoles.contains(Role.ARCHITECTE)) {
            selectedRole=Role.ARCHITECTE;
        }
        else if(availableRoles.contains(Role.VOLEUR)) {
            selectedRole=Role.VOLEUR;
        }
        else if(availableRoles.contains(Role.ROI)) {
            selectedRole=Role.ROI;
        }


        setRole(selectedRole);
        return availableRoles.indexOf(selectedRole);
    }

    @Override
    public Optional<Role> selectRoleToSteal(List<Role> availableRoles, List<Role> unstealableRoles) {
        if(availableRoles.contains(Role.MARCHAND)) {
            return Optional.of(Role.MARCHAND);
        }
        else if(availableRoles.contains(Role.ARCHITECTE)) {
            return Optional.of(Role.ARCHITECTE);
        }
        else {
            return Optional.of(availableRoles.get(0));
        }
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
        Optional<District> choosenDistrict=Optional.empty();

        for(District district: getBuyableCards()) {
            if(choosenDistrict.isEmpty() || (district.getColor()==getRole().getColor() && district.getCost() < 5 && district.getColor() != Color.GRAY) || district.getCost() < choosenDistrict.get().getCost()) {
                choosenDistrict=Optional.of(district);
            }
        }

        return choosenDistrict;
    }
}
