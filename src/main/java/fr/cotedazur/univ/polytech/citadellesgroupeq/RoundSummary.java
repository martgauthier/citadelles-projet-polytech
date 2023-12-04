package fr.cotedazur.univ.polytech.citadellesgroupeq;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant le bilan d'un tour: pièces tirées, cartes tirées, citadelles achetées
 */
public class RoundSummary {

    public static final int DEFAULT_DRAWN_COINS=0;
    public static final List<Citadel> EMPTY_CITADEL_LIST=new ArrayList<>();

    private List<Citadel> boughtCitadels;
    private int drawnCoins;

    private List<Citadel> drawnCards;

    public RoundSummary(int drawnCoins, List<Citadel> drawnCards, List<Citadel> boughtCitadels) {
        this.drawnCoins=drawnCoins;
        this.drawnCards=drawnCards;
        this.boughtCitadels=boughtCitadels;
    }

    public RoundSummary() {
        this(DEFAULT_DRAWN_COINS, EMPTY_CITADEL_LIST, EMPTY_CITADEL_LIST);
    }

    public List<Citadel> getBoughtCitadels() { return boughtCitadels; }

    public void setBoughtCitadels(List<Citadel> boughtCitadels) {
        this.boughtCitadels = boughtCitadels;
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

    /**
     *
     * @return True si le joueur a récupéré des pièces, et non pas les cartes. False sinon
     */
    public boolean pickedCash() {
        return drawnCoins>0;
    }

    /**
     *
     * @return True si le joueur a récupéré des cartes, et non pas les pièces. False sinon
     */
    public boolean pickedCards() {
        return !drawnCards.isEmpty();
    }

    /**
     *
     * @return True si le joueur a posé une de ses citadelles dans sa cité, False sinon
     */
    public boolean boughtCitadels() {
        return !boughtCitadels.isEmpty();
    }
}
