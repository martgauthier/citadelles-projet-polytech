package fr.cotedazur.univ.polytech.citadellesgroupeq.players;

import fr.cotedazur.univ.polytech.citadellesgroupeq.Color;
import fr.cotedazur.univ.polytech.citadellesgroupeq.District;
import fr.cotedazur.univ.polytech.citadellesgroupeq.PowerManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.strategies.AimForMoneyStrategy;
import fr.cotedazur.univ.polytech.citadellesgroupeq.strategies.DefaultStrategy;
import fr.cotedazur.univ.polytech.citadellesgroupeq.strategies.PreventArchitectStrategy;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Stratégie proposée par notre collège Thomas
 */
public class ThomasPlayer extends Player {
    public ThomasPlayer(int id) {
        super(id);
    }

    @Override
    public String getBotLogicName() {
        return "ThomasPlayer";
    }

    @Override
    public void playTurn(RoundSummary summary, GameLogicManager game) {
        boolean aPlayerIsCloseToWin=game.getPlayersList().stream().anyMatch(joueur -> joueur.isCloseToWin() && joueur!=this);
        /*if(aPlayerIsCloseToWin) {
            setStrategy(new PreventArchitectStrategy(this));
        }
        else {
            setStrategy(new DefaultStrategy(this));
        }*/

        getCoinsFromColorCards(summary);
        getRole().power(game, this, summary);

        boolean hasCardsOver3Coins=getCardsInHand().stream().anyMatch(district -> district.getCost() >= 3);

        if(hasCardsOver3Coins) {
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
    public int selectAndSetRole(List<Role> availableRoles, List<Player> playerList) {
        Role selectedRole=availableRoles.get(0);
        if(availableRoles.contains(Role.VOLEUR)) {
            selectedRole=Role.VOLEUR;
        }
        else if(availableRoles.contains(Role.CONDOTTIERE)) {
            selectedRole=Role.CONDOTTIERE;
        }
        else if(availableRoles.contains(Role.ARCHITECTE)) {
            selectedRole=Role.ARCHITECTE;
        }


        setRole(selectedRole);
        return availableRoles.indexOf(selectedRole);
    }

    @Override
    public Optional<AbstractMap.SimpleEntry<Integer, District>> selectDistrictToDestroyAsCondottiere(List<Player> players) {
        for(Player joueur: players) {
            if(joueur.isCloseToWin() && joueur!=this && Collections.min(joueur.getCity()).getCost() <= this.getCash() && joueur.getRole()!=Role.EVEQUE && !Collections.min(joueur.getCity()).getName().equals("Donjon")) {
                return Optional.of(new AbstractMap.SimpleEntry<>(joueur.getId(), Collections.min(joueur.getCity())));
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Role> selectRoleToSteal(List<Role> availableRoles, List<Role> unstealableRoles) {
        if(availableRoles.contains(Role.ARCHITECTE)) {
            return Optional.of(Role.MARCHAND);
        }
        else if(availableRoles.contains(Role.VOLEUR)) {
            return Optional.of(Role.VOLEUR);
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
        List<District> buyableDistricts = getBuyableCards();
        if(buyableDistricts.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(Collections.min(buyableDistricts));
    }
}
