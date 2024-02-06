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
     * Il utilise cette stratégie quand un joueur est sur le point de poser son avant-dernier quartier
     */
    @Override
    public Role selectRoleToKillAsAssassin(List<Role> availableRoles) {
        return player.selectRoleToKillAsAssassin(availableRoles);
    }
    @Override
    public Optional<AbstractMap.SimpleEntry<Integer, District>> selectDistrictToDestroyAsCondottiere(List<Player> players) {
        for(Player testedPlayer: players) {
            if(testedPlayer!=player && testedPlayer.getCity().size()==6){
                if(testedPlayer.getRole() != Role.CONDOTTIERE && (testedPlayer.getRole() != Role.EVEQUE || testedPlayer.isDeadForThisTurn())){
                    //un eveque peut se faire détruire un district s'il est mort.
                    for(District district: testedPlayer.getCity()) {
                        if(district.getCost() - 1 <= player.getCash() && !(district.getColor() == Color.PURPLE && district.getName().equalsIgnoreCase("donjon"))) {
                            return Optional.of(new AbstractMap.SimpleEntry<>(testedPlayer.getId(), district));
                        }
                    }
                }
            }
        }
        return Optional.empty();
    }
}
