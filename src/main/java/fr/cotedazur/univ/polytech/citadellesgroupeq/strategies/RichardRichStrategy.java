package fr.cotedazur.univ.polytech.citadellesgroupeq.strategies;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;

import java.util.List;
public class RichardRichStrategy extends DefaultStrategy{
    public RichardRichStrategy(Player player){
        super(player);
        strategyName="[RichardRichStrategy]";
    }
    /**
     * Il utilise cette stratégie quand il est riche
     */
    @Override
    public int selectAndSetRole(List<Role> availableRoles, List<Player> playerList) {
        if(availableRoles.contains(Role.ARCHITECTE)){
            player.setRole(Role.ARCHITECTE);
            return availableRoles.indexOf(Role.ARCHITECTE);
        }
        else if(availableRoles.contains(Role.MAGICIEN)) {
            player.setRole(Role.MAGICIEN);
            return availableRoles.indexOf((Role.MAGICIEN));
        }
        else {
            int selectedRoleIndex=0;
            if(availableRoles.get(selectedRoleIndex)==Role.CONDOTTIERE) {//lorsqu'il veut finir, il ne prend pas le condottiere
                selectedRoleIndex=1;
            }
            player.setRole(availableRoles.get(selectedRoleIndex));
            return selectedRoleIndex;
        }
    }
}
