package fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic;

import fr.cotedazur.univ.polytech.citadellesgroupeq.District;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Classe représentant le bilan d'un tour: pièces tirées, cartes tirées, citadelles achetées
 */
public class RoundSummary {

    public static final int DEFAULT_DRAWN_COINS=0;
    public static final List<District> EMPTY_DISTRICT_LIST =new ArrayList<>();

    private List<District> boughtDistricts;
    private int drawnCoins;

    private int coinsWonByColorCards;
    private boolean hasUsedPower;
    private boolean hasBeenKilledDuringTurn;


    private boolean hasWonDuringTurn;

    private List<District> drawnCards;

    public RoundSummary(int drawnCoins, List<District> drawnCards, List<District> boughtDistricts, boolean hasWonDuringTurn, int coinsWonByColorCards, boolean hasUsedPower, boolean hasBeenKilledDuringTurn) {
        this.drawnCoins=drawnCoins;
        this.drawnCards=new ArrayList<>(drawnCards);
        this.boughtDistricts =new ArrayList<>(boughtDistricts);
        this.hasWonDuringTurn = hasWonDuringTurn;
        this.coinsWonByColorCards=coinsWonByColorCards;
        this.hasUsedPower = hasUsedPower;
        this.hasBeenKilledDuringTurn=hasBeenKilledDuringTurn;
    }

    public RoundSummary() {
        this(DEFAULT_DRAWN_COINS, EMPTY_DISTRICT_LIST, EMPTY_DISTRICT_LIST, false, 0, false, false);
    }

    public List<District> getBoughtDistricts() { return boughtDistricts; }

    public void setBoughtDistricts(List<District> boughtDistricts) {
        this.boughtDistricts = boughtDistricts;
    }

    public void addBoughtDistrict(District boughtDistrict) {
        this.boughtDistricts.add(boughtDistrict);
    }

    public int getDrawnCoins() { return drawnCoins; }

    public void setDrawnCoins(int drawnCoins) {
        this.drawnCoins = drawnCoins;
    }

    public void addCoins(int coinsToAdd) {
        drawnCoins+=coinsToAdd;
    }

    public List<District> getDrawnCards() { return drawnCards; }

    public void setDrawnCards(List<District> drawnCards) {
        this.drawnCards = drawnCards;
    }

    public void addDrawnCard(District cardToAdd) {
        this.drawnCards.add(cardToAdd);
    }

    /**
     *
     * @return True si le joueur a récupéré des pièces, et non pas les cartes. False sinon
     */
    public boolean hasPickedCash() {
        return drawnCoins>0;
    }

    /**
     *
     * @return True si le joueur a récupéré des cartes, et non pas les pièces. False sinon
     */
    public boolean hasPickedCards() {
        return !drawnCards.isEmpty();
    }

    /**
     *
     * @return True si le joueur a posé un de ses districts dans sa cité, False sinon
     */
    public boolean hasBoughtDistricts() {
        return !boughtDistricts.isEmpty();
    }
    public boolean hasUsedPower(){return hasUsedPower;}
    public void toggleUsePower(){
        hasUsedPower = !hasUsedPower;
    }

    public void setHasUsedPower() {
        hasUsedPower=true;
    }
    public boolean hasBeenKilled(){return hasBeenKilledDuringTurn;}
    public void setHasBeenKilledDuringTurn(){
        hasBeenKilledDuringTurn =true;
    }

    public boolean hasWonDuringTurn() {
        return hasWonDuringTurn;
    }

    public void setHasWonDuringTurn(boolean hasWonDuringTurn) {
        this.hasWonDuringTurn=hasWonDuringTurn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoundSummary summary = (RoundSummary) o;
        return drawnCoins == summary.drawnCoins && Objects.equals(boughtDistricts, summary.boughtDistricts) && Objects.equals(drawnCards, summary.drawnCards) && hasWonDuringTurn == summary.hasWonDuringTurn();
    }

    @Override
    public int hashCode() {
        return Objects.hash(boughtDistricts, drawnCoins, drawnCards);
    }


    public int getCoinsWonByColorCards() {
        return coinsWonByColorCards;
    }

    public void setCoinsWonByColorCards(int coins) {
        coinsWonByColorCards=coins;
    }

    public void addCoinsWonByColorCards(int coins) {
        coinsWonByColorCards+=coins;
    }

    public boolean hasWonCoinsByColorCards() {
        return coinsWonByColorCards>0;
    }
}
