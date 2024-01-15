package fr.cotedazur.univ.polytech.citadellesgroupeq.players;

import fr.cotedazur.univ.polytech.citadellesgroupeq.*;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.strategies.DefaultStrategy;
import fr.cotedazur.univ.polytech.citadellesgroupeq.strategies.IStrategy;

import java.util.*;

/**
 * La classe Player représente un joueur dans le jeu Citadelles. Chaque joueur a un identifiant unique, une quantité
 * d'argent (cash), des cartes dans sa main (non posées dans sa cité), un rôle attribué
 */
public abstract class Player implements Comparable<Player>, IStrategy {

    public Random randomGenerator=new Random();
    private int cash;
    private boolean deadForThisTurn;
    public static final int DEFAULT_CASH=0;

    /**
     * Identification du bot: bot numéro 0 -> id=0, bot numéro 1 -> id=1...
     */
    private final int id;

    /**
     * Cartes que le joueur contient dans sa main. PAS LES CARTES POSÉES DANS SA CITE
     */
    private List<District> cardsInHand;

    private List<District> city;//la cité, où le joueur pose ses cartes

    private Role role;

    /**
     * Strategy used by Player
     */
    private IStrategy strategy;

    protected Player(int id) {
        this(id, DEFAULT_CASH, new ArrayList<>(),false);
        pickCard(new RoundSummary());
        pickCard(new RoundSummary());//no need to get summary
    }

    protected Player(int id, int cash, List<District> cards, boolean deadForThisTurn) {
        this.cash=cash;
        this.role=Role.EMPTY_ROLE;
        this.id=id;
        this.cardsInHand =new ArrayList<>(cards);//to make sure List is modifiable
        this.city=new ArrayList<>();
        this.deadForThisTurn = deadForThisTurn;
        this.strategy=new DefaultStrategy(this);
    }

    public void setRandomGenerator(Random customRandom) {
        randomGenerator = customRandom;
    }

    public int getCash() {
        return cash;
    }

    public List<District> getCardsInHand() {return cardsInHand;}

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role=role;
    }

    public void setCash(int cash) {
        this.cash=(cash >= 0) ? cash : this.cash;
    }

    public void setCardInHand(int index, District card) {
        if(index < 0 || index >= cardsInHand.size()) throw new IllegalArgumentException("Index must be in list bounds.");

        this.cardsInHand.set(index, card);
    }
    public void setCardsInHand(List<District> cards) {this.cardsInHand = cards;}

    public boolean stillHasCash() {
        return (cash > 0);
    }
    public boolean isDeadForThisTurn() {
        return deadForThisTurn;
    }

    public void dieForThisTurn() {
        deadForThisTurn = true;
    }
    public void rescucitate(){
        deadForThisTurn = false;
    }



    @Override
    public Role selectRoleToKillAsAssassin(List<Role> availableRoles){
        Role assassinatedRole;
        do { //boucle while pour éviter qu'il se tue lui même
            assassinatedRole=availableRoles.get(randomGenerator.nextInt(availableRoles.size()));
        } while((assassinatedRole.equals(this.getRole())));
        return assassinatedRole;
    }


    @Override
    public Optional<Role> selectRoleToSteal(List<Role> availableRoles, List<Role> unstealableRoles) {
        for (int i = 0; i < availableRoles.size(); i++) {
            Role stealedRole = availableRoles.get(randomGenerator.nextInt(availableRoles.size()));
            if (!unstealableRoles.contains(stealedRole)) {
                return Optional.of(stealedRole);
            }
        }
        return Optional.empty();
    }

    /**
     * Ajoute 2 au cash du joueur. Utile pour chaque début de tour
     */
    public void draw2Coins(RoundSummary summary) {
        this.cash+=2;
        summary.addCoins(2);
    }

    public void addCoins(int coins) {
        if(coins >= 0) {
            this.cash+=coins;
        }
    }

    public void removeCoins(int coins) {
        if(cash >= coins && coins >= 0) {
            this.cash-=coins;
        }
    }

    public boolean removeCardFromHand(District cardToRemove) {
        return cardsInHand.remove(cardToRemove);
    }

    public void addCardToHand(District cardToAdd) {
        cardsInHand.add(cardToAdd);
    }

    public void addAllCardsToHand(List<District> cardsToAdd){
        cardsInHand.addAll(cardsToAdd);
    }

    public void addAllCardsToHand(District...cardsToAdd) {
        addAllCardsToHand(List.of(cardsToAdd));
    }



    @Override
    public int selectRole(List<Role> availableRoles) {
        int selectedRoleIndex=randomGenerator.nextInt(availableRoles.size());//la sélection est pour l'instant aléatoire
        setRole(availableRoles.get(selectedRoleIndex));
        return selectedRoleIndex;
    }

    /**
     *
     * @return les {@link District} que le joueur a dans sa main et qu'il est capable d'acheter
     */
    public List<District> getBuyableCards() {
        return getBuyableCards(getCash());
    }

    /**
     *
     * @param cashAvailable le montant maximal des cartes
     * @return La liste des cartes de sa main que le joueur est capable de payer
     */
    public List<District> getBuyableCards(int cashAvailable) {
        List<District> buyableCards = new ArrayList<>();
        for(District card : cardsInHand) {
            if(card.getCost() <= cashAvailable) {
                buyableCards.add(card);
            }
        }
        return buyableCards;
    }

    /**
     * Permet de générer 2 cartes aléatoires. Utile pour proposer à un joueur 2 cartes parmi lesquelles choisir
     */
    public List<District> generate2Cards(){
        DistrictsJSONReader districtsReader = new DistrictsJSONReader();
        List<District> districtsList = districtsReader.getDistrictsList();
        List<District> dealCards = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            int randomIndex = randomGenerator.nextInt(districtsList.size());
            District randomDistrict = districtsList.get(randomIndex);
            dealCards.add(randomDistrict);
        }
        return dealCards;
    }

    /**
     * Choisit une carte parmi celles proposées pour l'ajouter au jeu du joueur.
     * @param cards les cartes proposées
     * @return la carte choisie
     */
    public District pickCard(RoundSummary summary, List<District> cards) {
        if(cards.isEmpty()) {
            throw new IllegalArgumentException("cards must not be empty.");
        }
        District choosenCard=cards.get(randomGenerator.nextInt(cards.size()));

        addCardToHand(choosenCard);

        summary.addDrawnCard(choosenCard);

        return choosenCard;
    }

    /**
     * Appelle {@link #pickCard(RoundSummary, List)}, avec 2 cartes choisies aléatoirement.
     * @return la carte choisie par le joueur
     */
    public District pickCard(RoundSummary summary) {
        return pickCard(summary, generate2Cards());
    }

    /**
     * Permet de manière aléatoire de distribuer deux cartes ou de donner deux pièces au joueur
     */
    public void dealCardsOrCash(RoundSummary summary) {
        if (randomGenerator.nextInt(2) == 1) {
            draw2Coins(summary);
        } else {
            pickCard(summary);
        }
    }


    @Override
    public int compareTo(Player o) {
        return role.compareTo(o.getRole());
    }

    /**
     *
     * @return Voir {@link #id}
     */
    public int getId() {
        return id;
    }


    public List<District> getCity() { return city; }

    public void setCity(List<District> city) {
        this.city = city;
    }

    public void addDistrictToCity(District districtToAdd) {
        this.city.add(districtToAdd);
    }

    public void addAllDistrictsToCity(List<District> districtsToAdd) {
        this.city.addAll(districtsToAdd);
    }

    public boolean removeDistrictFromCity(District districtToRemove) {
        return city.remove(districtToRemove);
    }

    public void clearCity() {
        city.clear();
    }

    /**
     *
     * @return la somme des prix des citadelles actuellement posées dans la cité du joueur
     */
    public int getTotalCityPrice() {
        int sum=0;
        for(District district: city) {
            sum+=district.getCost();
        }

        return sum;
    }

    public boolean hasAllColorsInCity(){
        boolean blue=false;
        boolean green=false;
        boolean yellow=false;
        boolean red=false;
        boolean purple=false;
        for(District district: city){
            if(district.getColor()==Color.BLUE){
                blue=true;
            }
            if(district.getColor()==Color.GREEN){
                green=true;
            }
            if(district.getColor()==Color.YELLOW){
                yellow=true;
            }
            if(district.getColor()==Color.RED){
                red=true;
            }
            if(district.getColor()==Color.PURPLE){
                purple=true;
            }
        }
        if(blue && green && yellow && red && purple){
            return true;
        }else{
            return false;
        }
    }

    public boolean hasEmptyCity() {
        return this.city.isEmpty();
    }

    public void getCoinsFromColorCards(RoundSummary summary) {
        for(District cartePosee: city) {
            if(cartePosee.getColor() == role.getColor() && role.getColor()!= Color.GRAY) {
                addCoins(1);
                summary.addCoinsWonByColorCards(1);
            }
        }
    }

    public void buyDistrictsDuringTurn(RoundSummary summary) {
        Optional<District> choosenDistrict = strategy.getChoosenDistrictToBuy();
        if (choosenDistrict.isPresent()) {
            District district = choosenDistrict.get();
            addDistrictToCity(district);
            summary.addBoughtDistrict(district);
            removeCardFromHand(district);
            removeCoins(district.getCost());
        }
    }

    public void clearHand() {
        cardsInHand.clear();
    }

    @Override
    public int[] selectCardsToExchangeWithPileAsMagicien() {//liste des index des cartes que le magicien voudrait échanger, si il choisit d'échanger des cartes avec la pile
        if (!getCardsInHand().isEmpty()) {
            int start = randomGenerator.nextInt(getCardsInHand().size());
            int end = randomGenerator.nextInt(start, getCardsInHand().size());
            int size = end - start + 1;

            int[] returnedArray = new int[size];

            // Fill the array with values between start and end
            for (int i = 0; i < size; i++) {
                returnedArray[i] = start + i;
            }

            return returnedArray;
        } else {
            return new int[0];
        }
    }

    @Override
    public Optional<AbstractMap.SimpleEntry<Integer, District>> selectDistrictToDestroyAsCondottiere(List<Player> players) {
        for(Player testedPlayer: players) {//Default strategy: returns first destroyable district not from the current player
            if(testedPlayer.getRole() != Role.CONDOTTIERE && (testedPlayer.getRole() != Role.EVEQUE || testedPlayer.isDeadForThisTurn())){
                //un eveque peut se faire détruire un district si il est mort.
                for(District district: testedPlayer.getCity()) {
                    if(district.getCost() - 1 <= this.getCash() && !(district.getColor() == Color.PURPLE && district.getName().equalsIgnoreCase("donjon"))) {
                        return Optional.of(new AbstractMap.SimpleEntry<>(testedPlayer.getId(), district));
                    }
                }
            }
        }

        return Optional.empty();
    }

    public IStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(IStrategy strategy) {
        this.strategy=strategy;
    }

    @Override
    public Player getPlayer() {
        return this;
    }

    public abstract String getBotLogicName();

    public boolean isDistrictInCity(String districtToCheck){
        List<District> city = this.getCity();
        for(District district: city){
            String districtName = district.getName();
            if(districtToCheck.equalsIgnoreCase(districtName)) {
                return true;
            }
        }
        return false;
    }

    public Optional<District> getDistrictInCity(String districtToGet) {
        //TODO Refactor it with "list.indexOf"
        List<District> city = this.getCity();
        for (District district : city) {
            String districtName = district.getName();
            if (districtToGet.equalsIgnoreCase(districtName)) {
                return Optional.of(district);
            }
        }
        return Optional.empty();
    }


}