package fr.cotedazur.univ.polytech.citadellesgroupeq;

import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.*;

public class PowerManager {
    private GameLogicManager game;

    public PowerManager(GameLogicManager game){
        this.game=game;
    }

    public GameLogicManager getGame() {
        return game;
    }


    public void applyCityPowers(Player player, RoundSummary summary){
        for (District district : player.getCity()) {
            String power = district.getPower();
            if (power != null) {
                applyCityPowers(district,player, summary);
            }
        }
    }


    public void applyCityPowers(District district, Player joueur, RoundSummary summary){
        String pouvoir = district.getPower();
        switch (pouvoir) {
            case "Ecole de magie power":
                PouvoirEcoleDeMagie(joueur, district);
                summary.setHasUsedMerveillesPower();
                summary.getUsedMerveilles().add(district.getName());
                break;
            case "Bibliotheque power":
                bibliothequePower(joueur,district,summary);
                summary.setHasUsedMerveillesPower();
                summary.getUsedMerveilles().add(district.getName());
                break;
            case "Laboratoire power":
                laboratoirePower(joueur,district,summary);
                summary.setHasUsedMerveillesPower();
                summary.getUsedMerveilles().add(district.getName());
                break;
            default:
                break;
        }
    }


    private void PouvoirEcoleDeMagie(Player joueur, District district) {
        if(joueur.getRole().getColor()!=Color.GRAY){
            joueur.addCoins(1);//si il a une couleur, il va gagner une pièce car c'est comme si le district "prenait" sa couleur de rôle, et lui faisait donc gagner une pièce
        }
    }
    private void bibliothequePower(Player joueur, District district,RoundSummary summary){
        if(summary.hasPickedCards()){
            joueur.pickCard(summary);
        }
    }
    private void laboratoirePower(Player player, District district,RoundSummary summary){
        if(player.getCash()<2 && player.getCardsInHand().toArray().length>2){
            District bigcity=player.getCardsInHand().get(0);
            for(District city : player.getCardsInHand()){
                if(bigcity.getCost()<city.getCost()){
                    bigcity=city;
                }
            }
            player.removeCardFromHand(bigcity);
            player.addCoins(1);
        }
    }

}