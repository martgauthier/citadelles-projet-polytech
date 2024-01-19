package fr.cotedazur.univ.polytech.citadellesgroupeq.strategies;

import fr.cotedazur.univ.polytech.citadellesgroupeq.District;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;

import java.util.AbstractMap;
import java.util.List;
import java.util.Optional;

public class DefaultStrategy implements IStrategy {
    protected final Player player;
    public DefaultStrategy(Player player) {
        this.player=player;
    }

    @Override
    public Optional<AbstractMap.SimpleEntry<Integer, District>> selectDistrictToDestroyAsCondottiere(List<Player> players) {
        return player.selectDistrictToDestroyAsCondottiere(players);
    }

    @Override
    public int[] selectCardsToExchangeWithPileAsMagicien() {
        return player.selectCardsToExchangeWithPileAsMagicien();
    }

    @Override
    public void playTurn(RoundSummary summary, GameLogicManager game) {
        player.playTurn(summary, game);
    }

    @Override
    public boolean choosesToExchangeCardWithPlayer() {
        return player.choosesToExchangeCardWithPlayer();
    }

    @Override
    public Player selectPlayerToExchangeCardsWithAsMagicien(List<Player> playerList) {
        return player.selectPlayerToExchangeCardsWithAsMagicien(playerList);
    }

    @Override
    public Optional<District> getChoosenDistrictToBuy() {
        return player.getChoosenDistrictToBuy();
    }

    @Override
    public int selectRole(List<Role> availableRoles, List<Player> playerList) {
        return player.selectRole(availableRoles, playerList);
    }

    @Override
    public Optional<Role> selectRoleToSteal(List<Role> availableRoles, List<Role> unstealableRoles) {
        return player.selectRoleToSteal(availableRoles, unstealableRoles);
    }

    @Override
    public Role selectRoleToKillAsAssassin(List<Role> availableRoles) {
        return player.selectRoleToKillAsAssassin(availableRoles);
    }

    @Override
    public Player getPlayer() {
        return player;
    }
}
