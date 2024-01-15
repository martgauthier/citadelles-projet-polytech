package fr.cotedazur.univ.polytech.citadellesgroupeq;

import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PowerManager {
    private GameLogicManager game;

    public PowerManager(GameLogicManager game){
        this.game=game;
    }

    public GameLogicManager getGame() {
        return game;
    }


    public void applyCityPowers(Player player){
        for (District district : player.getCity()) {
            String power = district.getPower();
            if (power != null) {
                applyCityPowers(district,player);
            }
        }
    }


    public void applyCityPowers(District district, Player joueur){
        String pouvoir = district.getPower();
        switch (pouvoir) {
            case "Ecole de magie power":
                PouvoirEcoleDeMagie(joueur, district);
                break;
            // Ajoutez des cas pour d'autres pouvoirs
            default:
                break;
        }
    }


    private void PouvoirEcoleDeMagie(Player joueur, District district) {
        Color playerColor = joueur.getRole().getColor();
        if(!Objects.equals(playerColor, Color.GRAY)){
            district.setColor(playerColor);
        }
    }

}