package fr.cotedazur.univ.polytech.citadellesgroupeq.strategies;

import fr.cotedazur.univ.polytech.citadellesgroupeq.PowerManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;

import java.util.List;

public class MattStartGameStrategy extends DefaultStrategy {
    public MattStartGameStrategy(Player player) {
        super(player);
        strategyName="[MattStartGame Strategy]";
    }

    /**
     * Choisit par ordre de priorité:
     * 1. Marchand 2. Architecte 3. Assassin 4. Autre rôle disponible
     * @param availableRoles les rôles disponibles
     * @param playerList
     * @return
     */
    @Override
    public int selectAndSetRole(List<Role> availableRoles, List<Player> playerList) {
        if(availableRoles.contains(Role.MARCHAND)) {
            player.setRole(Role.MARCHAND);
            return availableRoles.indexOf(Role.MARCHAND);
        }
        else if(availableRoles.contains(Role.ARCHITECTE)) {
            player.setRole(Role.ARCHITECTE);
            return availableRoles.indexOf(Role.ARCHITECTE);
        }
        else if(availableRoles.contains(Role.ASSASSIN)) {
            player.setRole(Role.ASSASSIN);
            return availableRoles.indexOf(Role.ASSASSIN);
        }
        else {
            player.setRole(availableRoles.get(0));
            return 0;
        }
    }

    @Override
    public Role selectRoleToKillAsAssassin(List<Role> availableRoles) {
        return (availableRoles.contains(Role.ARCHITECTE) && player.getRole()!=Role.ARCHITECTE) ? Role.ARCHITECTE : availableRoles.get(0);
    }

    @Override
    public void playTurn(RoundSummary summary, GameLogicManager game) {
        if(player.getCardsInHand().size() >= 4 && player.getCash() >= 5) {//no needs to use MattStartGameStrategy anymore
            player.setStrategy(new DefaultStrategy(player));
        }
        player.playTurn(summary, game);
    }
}
