package fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic;

import fr.cotedazur.univ.polytech.citadellesgroupeq.Citadel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Classe représentant le bilan d'un tour: pièces tirées, cartes tirées, citadelles achetées
 */
public class RoundSummary {

    public static final int DEFAULT_DRAWN_COINS=0;
    public static final List<Citadel> EMPTY_CITADEL_LIST=new ArrayList<>();

    private List<Citadel> boughtCitadels;
    private int drawnCoins;

    private int coinsWonByColorCards;

    private boolean hasWonDuringTurn;

    private List<Citadel> drawnCards;

    public RoundSummary(int drawnCoins, List<Citadel> drawnCards, List<Citadel> boughtCitadels, boolean hasWonDuringTurn, int coinsWonByColorCards) {
        this.drawnCoins=drawnCoins;
        this.drawnCards=new ArrayList<>(drawnCards);
        this.boughtCitadels=new ArrayList<>(boughtCitadels);
        this.hasWonDuringTurn = hasWonDuringTurn;
        this.coinsWonByColorCards=coinsWonByColorCards;
    }

    public RoundSummary() {
        this(DEFAULT_DRAWN_COINS, EMPTY_CITADEL_LIST, EMPTY_CITADEL_LIST, false, 0);
    }

    public List<Citadel> getBoughtCitadels() { return boughtCitadels; }

    public void setBoughtCitadels(List<Citadel> boughtCitadels) {
        this.boughtCitadels = boughtCitadels;
    }

    public void addBoughtCitadel(Citadel boughtCitadel) {
        this.boughtCitadels.add(boughtCitadel);
    }

    public int getDrawnCoins() { return drawnCoins; }

    public void setDrawnCoins(int drawnCoins) {
        this.drawnCoins = drawnCoins;
    }

    public void addCoins(int coinsToAdd) {
        drawnCoins+=coinsToAdd;
    }

    public List<Citadel> getDrawnCards() { return drawnCards; }

    public void setDrawnCards(List<Citadel> drawnCards) {
        this.drawnCards = drawnCards;
    }

    public void addDrawnCard(Citadel cardToAdd) {
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
     * @return True si le joueur a posé une de ses citadelles dans sa cité, False sinon
     */
    public boolean hasBoughtCitadels() {
        return !boughtCitadels.isEmpty();
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
        return drawnCoins == summary.drawnCoins && Objects.equals(boughtCitadels, summary.boughtCitadels) && Objects.equals(drawnCards, summary.drawnCards) && hasWonDuringTurn == summary.hasWonDuringTurn();
    }

    @Override
    public int hashCode() {
        return Objects.hash(boughtCitadels, drawnCoins, drawnCards);
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
