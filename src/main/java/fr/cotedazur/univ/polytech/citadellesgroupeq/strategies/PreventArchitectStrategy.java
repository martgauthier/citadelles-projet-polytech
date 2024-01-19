package fr.cotedazur.univ.polytech.citadellesgroupeq.strategies;

import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;

import java.util.List;
import java.util.Optional;

/**
 * Si un joueur est proche de gagner, il est recommandé de lui éviter de prendre l'architecte.
 * Cette stratégie va 1. prendre le rôle condottière et tuer l'architecte si possible. 2. Prendre le rôle architecte si disponible. 3. Prendre le rôle par défaut sinon
 */
public class PreventArchitectStrategy extends DefaultStrategy {
    public PreventArchitectStrategy(Player copiedPlayer) {
        super(copiedPlayer);
    }

    @Override
    public int selectRole(List<Role> availableRoles, List<Player> playerList) {
        Optional<Player> playerCloseToWin=Optional.empty();

        for(Player checkedPlayer: playerList) {
            if(checkedPlayer.isCloseToWin() && checkedPlayer!=player) {
                playerCloseToWin=Optional.of(checkedPlayer);
            }
        }

        if(playerCloseToWin.isPresent()) {
            if(availableRoles.contains(Role.CONDOTTIERE)) {
                return availableRoles.indexOf(Role.CONDOTTIERE);
            }
            else if (availableRoles.contains(Role.ARCHITECTE)) {
                return availableRoles.indexOf(Role.ARCHITECTE);
            }
        }
        return player.selectRole(availableRoles, playerList);
    }

    @Override
    public Role selectRoleToKillAsAssassin(List<Role> availableRoles) {
        if(availableRoles.contains(Role.ARCHITECTE) ) {
            return Role.ARCHITECTE;
        }
        return player.selectRoleToKillAsAssassin(availableRoles);//else
    }
}