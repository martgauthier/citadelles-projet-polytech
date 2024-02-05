package fr.cotedazur.univ.polytech.citadellesgroupeq.strategies;

import fr.cotedazur.univ.polytech.citadellesgroupeq.Color;
import fr.cotedazur.univ.polytech.citadellesgroupeq.District;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
}
