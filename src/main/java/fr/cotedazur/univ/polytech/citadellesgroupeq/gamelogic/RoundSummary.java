package fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic;

import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;
import fr.cotedazur.univ.polytech.citadellesgroupeq.District;

import java.util.*;

/**
 * Classe représentant le bilan d'un tour: pièces tirées, cartes tirées, citadelles achetées...
 */
public class RoundSummary {


    /**
     * Pièces tirées par defaut par un joueur n'ayant pas joué = 0
     */
    public static final int DEFAULT_DRAWN_COINS=0;

    /**
     * Liste de districts vide, utilisee pour initialiser la liste des districts achetes par exemple.
     */
    protected static final List<District> EMPTY_DISTRICT_LIST =new ArrayList<>();

    /**
     * Districts achetés et poses en cite par le joueur durant le tour.
     */
    private List<District> boughtDistricts;

    /**
     * Pièces tirées par le joueur durant le tour.
     */
    private int drawnCoins;

    /**
     * Pieces gagnees à ce tour grâce aux cartes de la cite ayant la meme couleur que le rôle du joueur.
     */
    private int coinsWonByColorCards;

    /**
     * true si le joueur a utilisé son pouvoir de rôle durant le tour, false sinon.
     */
    private boolean hasUsedRolePower;

    /**
     * true si le joueur a utilisé le pouvoir d'au moins une merveille durant le tour.
     * Les merveilles n'ayant pas un pouvoir "applicable" (exemple: donjon) ne passent PAS ce booléen à "true".
     */
    private boolean hasUsedMerveillesPower;

    /**
     * true si le joueur a été tué par l'assassin pour ce round.
     */
    private boolean hasBeenKilledDuringTurn;

    /**
     * Contient {@link Role#EMPTY_ROLE} si le joueur n'a pas volé de rôle, le rôle volé par son pouvoir de voleur sinon
     */
    private Role stealedRole;

    /**
     * Liste des merveilles dont le joueur a utilisé le pouvoir. (en lien avec {@link RoundSummary#hasUsedMerveillesPower})
     */
    private List<String> usedMerveilles =new ArrayList<>();

    /**
     * true si le joueur a échangé les cartes avec la pile en tant que magicien, false sinon
     */
    private boolean hasExchangedCardsWithPileAsMagician;

    /**
     * Utilisé pour stocker la paire (ID du joueur, district) indiquant
     * quel joueur a perdu un district et quel district a été détruit. Si aucun district
     * n'a été détruit pendant le tour, l'Optional est vide.
     */
    private Optional<AbstractMap.SimpleEntry<Integer, District>> optionalDestroyedDistrict = Optional.empty();

    /**
     *
     * @return voir {@link RoundSummary#optionalDestroyedDistrict}
     */
    public Optional<AbstractMap.SimpleEntry<Integer, District>> getOptionalDestroyedDistrict() { return optionalDestroyedDistrict; }

    /**
     * voir {@link RoundSummary#optionalDestroyedDistrict}
     * @param destroyedDistrict
     */
    public void setDestroyedDistrict(AbstractMap.SimpleEntry<Integer, District> destroyedDistrict) {
        this.optionalDestroyedDistrict = Optional.of(destroyedDistrict);
    }

    /**
     * true si le joueur finit son tour avec 8 citadelles
     */
    private boolean hasFinishDuringTurn;


    /**
     * Id du joueur avec qui il a échangé ses cartes en tant que magicien. -1 si personne
     */
    private int exchangedCardsPlayerId=-1;

    /**
     * index des cartes dans la main du joueur, qu'il a échangé avec la pile. Vide si pas d'échange
     */
    private int[] exchangedCardsWithPileIndex=new int[0];

    public int[] getExchangedCardsWithPileIndex() { return exchangedCardsWithPileIndex; }

    public void setExchangedCardsWithPileIndex(int[] exchangedCardsWithPileIndex) {
        this.exchangedCardsWithPileIndex = exchangedCardsWithPileIndex;
    }

    /**
     * Liste des cartes piochées
     */
    private List<District> drawnCards;

    public RoundSummary(int drawnCoins, List<District> drawnCards, List<District> boughtDistricts, boolean hasFinishDuringTurn, int coinsWonByColorCards, boolean hasUsedRolePower, boolean hasBeenKilledDuringTurn, Role stealedRole) {
        this.drawnCoins=drawnCoins;
        this.drawnCards=new ArrayList<>(drawnCards);
        this.boughtDistricts =new ArrayList<>(boughtDistricts);
        this.hasFinishDuringTurn = hasFinishDuringTurn;
        this.coinsWonByColorCards=coinsWonByColorCards;
        this.hasUsedRolePower = hasUsedRolePower;
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
    public boolean hasUsedRolePower(){return hasUsedRolePower;}

    /**
     * Inverse la valeur de {@link RoundSummary#hasUsedRolePower}
     */
    public void toggleUsedRolePower(){
        hasUsedRolePower = !hasUsedRolePower;
    }

    public void setHasUsedRolePower() {
        hasUsedRolePower =true;
    }
    public boolean hasUsedMerveillePower(){return hasUsedMerveillesPower;}
    public void setHasUsedMerveillesPower(){ hasUsedMerveillesPower =true;}
    public boolean hasBeenKilled(){return hasBeenKilledDuringTurn;}
    public void setHasBeenKilledDuringTurn(){
        hasBeenKilledDuringTurn =true;
    }
    public List<String> getUsedMerveilles(){
        return usedMerveilles;
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


    /**
     *
     * @return true si le joueur a acheté la cour des miracles à ce tour.
     * Utile pour empêcher d'utiliser le pouvoir de cette carte si achetée au dernier tour
     */
    public boolean containsCourDesMiracles() {
        for(District district: boughtDistricts) {
            if(district.getName().equals("Cour des miracles")) {
                return true;
            }
        }
        return false;
    }
}
