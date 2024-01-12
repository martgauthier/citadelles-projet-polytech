package fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic;

import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;
import fr.cotedazur.univ.polytech.citadellesgroupeq.District;

import java.util.*;

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
    private Role stealedRole;

    private boolean hasExchangedCardsWithPileAsMagician;

    private Optional<AbstractMap.SimpleEntry<Integer, District>> optionalDestroyedDistrict=Optional.empty();

    public Optional<AbstractMap.SimpleEntry<Integer, District>> getOptionalDestroyedDistrict() { return optionalDestroyedDistrict; }

    public void setDestroyedDistrict(AbstractMap.SimpleEntry<Integer, District> destroyedDistrict) {
        this.optionalDestroyedDistrict = Optional.of(destroyedDistrict);
    }

    private boolean hasFinishDuringTurn;


    /**
     * Id du joueur avec qui il a échangé ses cartes en tant que magicien. -1 si personne
     */
    private int exchangedCardsPlayerId=-1;

    private int[] exchangedCardsWithPileIndex=new int[0];

    public int[] getExchangedCardsWithPileIndex() { return exchangedCardsWithPileIndex; }

    public void setExchangedCardsWithPileIndex(int[] exchangedCardsWithPileIndex) {
        this.exchangedCardsWithPileIndex = exchangedCardsWithPileIndex;
    }

    private List<District> drawnCards;

    public RoundSummary(int drawnCoins, List<District> drawnCards, List<District> boughtDistricts, boolean hasFinishDuringTurn, int coinsWonByColorCards, boolean hasUsedPower, boolean hasBeenKilledDuringTurn, Role stealedRole) {
        this.drawnCoins=drawnCoins;
        this.drawnCards=new ArrayList<>(drawnCards);
        this.boughtDistricts =new ArrayList<>(boughtDistricts);
        this.hasFinishDuringTurn = hasFinishDuringTurn;
        this.coinsWonByColorCards=coinsWonByColorCards;
        this.hasUsedPower = hasUsedPower;
        this.hasBeenKilledDuringTurn=hasBeenKilledDuringTurn;
        this.stealedRole = stealedRole;
    }

    public RoundSummary() {
        this(DEFAULT_DRAWN_COINS, EMPTY_DISTRICT_LIST, EMPTY_DISTRICT_LIST, false, 0, false, false, Role.EMPTY_ROLE);
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

    public void setStealedRole(Role stealedRole){this.stealedRole = stealedRole;}

    public Role getStealedRole(){
        return stealedRole;
    }

    public boolean hasFinishDuringTurn() {
        return hasFinishDuringTurn;
    }

    public void setHasFinishDuringTurn(boolean hasWonDuringTurn) {
        this.hasFinishDuringTurn =hasWonDuringTurn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoundSummary summary = (RoundSummary) o;
        return drawnCoins == summary.drawnCoins && Objects.equals(boughtDistricts, summary.boughtDistricts) && Objects.equals(drawnCards, summary.drawnCards) && hasFinishDuringTurn == summary.hasFinishDuringTurn();
    }

    @Override
    public int hashCode() {
        return Objects.hash(boughtDistricts, drawnCoins, drawnCards);
    }


    public int getCoinsWonByColorCards() {
        return coinsWonByColorCards;
    }

    public void setExchangedCardsPlayerId(int id) {
        exchangedCardsPlayerId=id;
    }

    public int getExchangedCardsPlayerId() {
        return exchangedCardsPlayerId;
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

    public void setHasExchangedCardsWithPileAsMagician(boolean hasExchanged) {
        hasExchangedCardsWithPileAsMagician=hasExchanged;
    }

    public boolean hasExchangedCardsWithPileAsMagician() {
        return hasExchangedCardsWithPileAsMagician;
    }
}
