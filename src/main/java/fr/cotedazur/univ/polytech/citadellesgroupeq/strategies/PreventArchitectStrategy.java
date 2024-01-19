package fr.cotedazur.univ.polytech.citadellesgroupeq.strategies;

import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;

import java.util.List;

/**
 * Si un joueur est proche de gagner, il est recommandé de lui éviter de prendre l'architecte.
 * Cette stratégie va 1. prendre le rôle condottière et tuer l'architecte si possible. 2. Prendre le rôle architecte si disponible. 3. Prendre le rôle par défaut sinon
 */
public class PreventArchitectStrategy extends DefaultStrategy {
    public PreventArchitectStrategy(Player copiedPlayer) {
        super(copiedPlayer);
    }


    /**
     *
     * @param verifiedPlayer
     * @return True si verifiedPlayer possède 4 pièces ou plus, 2 carte en main ou plus, 6 quartiers posés ou plus
     */
    public boolean isPlayerCloseToWin(Player verifiedPlayer) {
        return verifiedPlayer.getCash() >= 4 && verifiedPlayer.getCardsInHand().size() >= 2 && verifiedPlayer.getCity().size() >= 6;
    }

    @Override
    public int selectRole(List<Role> availableRoles, List<Player> playerList) {
        boolean aPlayerIsCloseToWin=playerList.stream().anyMatch(this::isPlayerCloseToWin);

        if(aPlayerIsCloseToWin) {
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