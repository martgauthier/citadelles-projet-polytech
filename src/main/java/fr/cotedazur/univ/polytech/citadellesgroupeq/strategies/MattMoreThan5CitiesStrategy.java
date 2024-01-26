package fr.cotedazur.univ.polytech.citadellesgroupeq.strategies;

import fr.cotedazur.univ.polytech.citadellesgroupeq.Color;
import fr.cotedazur.univ.polytech.citadellesgroupeq.District;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MattMoreThan5CitiesStrategy extends DefaultStrategy {
    public MattMoreThan5CitiesStrategy(Player player) {
        super(player);
        strategyName="[MattMoreThan5CitiesStrategy]";
    }

    /**
     * Une fois qu'il a 5 districts dans sa cité, il achète à chaque fois le district le moins cher, parmi les couleurs qui lui manquent. (Si il a déjà toutes les couleurs, il pose sa plus petite de toute sa main). Sinon, il priorise les cartes de sa couleur, avec le prix le plus proche de 3. (Si il a pas sa couleur, il priorise la carte la plus proche de 3 peu importe la couleur.
     * @return
     */
    @Override
    public Optional<District> getChoosenDistrictToBuy() {
        if(player.getCity().size()<5) {
            player.setStrategy(new DefaultStrategy(player));
            return player.getChoosenDistrictToBuy();
        }
        else {
            Map<Color, Boolean> colorsContained=player.getColorsContainedInCityMap();
            Optional<District> choosenDistrict=Optional.empty();

            //first step: select lowest price from missing colors
            for(District card: player.getBuyableCards()) {
                if(choosenDistrict.isEmpty()) choosenDistrict=Optional.of(card);
                else if(!colorsContained.get(card.getColor()) && choosenDistrict.get().getCost() > card.getCost()) {
                        choosenDistrict=Optional.of(card);

                }
            }

            if(choosenDistrict.isPresent()) return choosenDistrict;
            else {//can't buy card from missing colors.
                List<District> cardsColoredLikePlayer=player.getBuyableCards().stream().filter(card -> card.getColor()==player.getRole().getColor()).toList();
                if(!cardsColoredLikePlayer.isEmpty()) {
                    return Optional.of(Collections.min(cardsColoredLikePlayer));
                }
                else {//has no cards from missing color and no card from his own color
                    for(District card: player.getBuyableCards()) {
                        if(choosenDistrict.isEmpty() || Math.abs(3-card.getCost()) < Math.abs(3-choosenDistrict.get().getCost())) {
                            choosenDistrict=Optional.of(card);
                        }
                    }
                    return choosenDistrict;
                }
            }
        }
    }
}
