package fr.cotedazur.univ.polytech.citadellesgroupeq.strategies;

import fr.cotedazur.univ.polytech.citadellesgroupeq.Color;
import fr.cotedazur.univ.polytech.citadellesgroupeq.District;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;

import javax.swing.text.html.Option;
import java.util.*;

public class SecurePointsForEndGame extends DefaultStrategy {
    public SecurePointsForEndGame(Player player) {
        super(player);
    }

    @Override
    public Optional<District> getChoosenDistrictToBuy() {
        List<District> buyableDistricts = this.player.getBuyableCards();
        if (buyableDistricts.isEmpty()) return Optional.empty();
        else {
            District bestOption = getMaxDistrictCostBuyable(buyableDistricts);
            if (this.player.getRole() != Role.ARCHITECTE) {
                List<Color> remainingColorsList = getRemainingColorsToBuy(this.player.getCity());
                if (remainingColorsList.size() == 1) {
                    // On récupère la carte restante pour compléter la couleur
                    District districtToBuyForColor = getMaxDistrictByColor(buyableDistricts, remainingColorsList.get(0));
                    if (districtToBuyForColor.getCost() + 3 >= bestOption.getCost()) { // Cas ou c plus benef de faire les 5 couleurs
                        bestOption = districtToBuyForColor;
                    }
                }
            }
        return Optional.of(bestOption);
        }
    }

    public District getMaxDistrictCostBuyable(List<District> city) {
        if (city == null || city.isEmpty()) {
            throw new IllegalArgumentException("La liste de district est vide ou nulle.");
        }
        District maxDistrict = city.get(0);
        int maxCost = city.get(0).getCost();
        for (District district : city) {
            int currentCost = district.getCost();
            if (currentCost > maxCost) {
                maxCost = currentCost;
                maxDistrict = district;
            }
        }
        return maxDistrict;
    }

    public District getMaxDistrictByColor(List<District> city, Color color) {
        if (city == null || city.isEmpty()) {
            throw new IllegalArgumentException("La liste de district est vide ou nulle.");
        }
        District maxDistrict = null;
        int maxCost = Integer.MIN_VALUE;
        for (District district : city) {
            if (district.getColor() == color) {
                int currentCost = district.getCost();
                if (currentCost > maxCost) {
                    maxCost = currentCost;
                    maxDistrict = district;
                }
            }
        }
        if (maxDistrict == null) {
            throw new IllegalArgumentException("Aucun district de la couleur spécifiée trouvé dans la liste.");
        }
        return maxDistrict;
    }



    public List<Color> getRemainingColorsToBuy(List<District> city) {
        EnumSet<Color> colorsInCity = EnumSet.noneOf(Color.class);

        // Ajoutez les couleurs déjà présentes dans la cité à l'ensemble
        for (District district : city) {
            colorsInCity.add(district.getColor());
        }

        List<Color> remainingColorsToBuy = new ArrayList<>();

        for (Color color : Color.values()) {
            if (color != Color.GRAY && !colorsInCity.contains(color)) {
                remainingColorsToBuy.add(color);
            }
        }
        return remainingColorsToBuy;
    }
}
