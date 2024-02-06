package fr.cotedazur.univ.polytech.citadellesgroupeq.strategies;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Color;
import fr.cotedazur.univ.polytech.citadellesgroupeq.District;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
public class RichardRichStrategy extends DefaultStrategy{
    public RichardRichStrategy(Player player){
        super(player);
        strategyName="[RichardRichStrategy]";
    }
    /**
     * Il utilise cette stratégie quand il est riche
     */
}
