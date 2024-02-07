package fr.cotedazur.univ.polytech.citadellesgroupeq.strategies;

import fr.cotedazur.univ.polytech.citadellesgroupeq.Color;
import fr.cotedazur.univ.polytech.citadellesgroupeq.District;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;

import java.util.*;

public class RichardMaliceStrategy extends DefaultStrategy{
    public RichardMaliceStrategy(Player player) {
        super(player);
        strategyName="[RichardMaliceStrategy]";
    }
    /**
     * Il utilise cette strategie quand un joueur est sur le point de poser son avant-dernier quartier
     */
    @Override
    public Role selectRoleToKillAsAssassin(List<Role> availableRoles) {
        return player.selectRoleToKillAsAssassin(availableRoles);
    }

    @Override
    public Optional<AbstractMap.SimpleEntry<Integer, District>> selectDistrictToDestroyAsCondottiere(List<Player> players) {
        for(Player testedPlayer: players) {
            if(testedPlayer!=player && testedPlayer.getCity().size()==6 && (testedPlayer.getRole() != Role.CONDOTTIERE && (testedPlayer.getRole() != Role.EVEQUE || testedPlayer.isDeadForThisTurn()))){
                    //un eveque peut se faire détruire un district s'il est mort.
                    for(District district: testedPlayer.getCity()) {
                        if(district.getCost() - 1 <= player.getCash() && !(district.getColor() == Color.PURPLE && district.getName().equalsIgnoreCase("donjon"))) {
                            return Optional.of(new AbstractMap.SimpleEntry<>(testedPlayer.getId(), district));
                        }
                    }
            }
        }
        return Optional.empty();
    }

    @Override
    public int selectAndSetRole(List<Role> availableRoles, List<Player> playerList) {
        if (availableRoles.contains(Role.ROI)) {
            player.setRole(Role.ROI);
            return availableRoles.indexOf(Role.ROI);
        } else if (availableRoles.contains(Role.ASSASSIN)) {
            player.setRole(Role.ASSASSIN);
            return availableRoles.indexOf(Role.ASSASSIN);
        } else if (availableRoles.contains(Role.CONDOTTIERE)){
            player.setRole(Role.CONDOTTIERE);
            return availableRoles.indexOf(Role.CONDOTTIERE);
        } else if (availableRoles.contains(Role.EVEQUE)) {
            player.setRole(Role.EVEQUE);
            return availableRoles.indexOf(Role.EVEQUE);
        } else {
            player.setRole(availableRoles.get(0));
            return 0;
        }
    }
}
