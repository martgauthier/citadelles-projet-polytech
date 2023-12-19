package fr.cotedazur.univ.polytech.citadellesgroupeq.players;

import fr.cotedazur.univ.polytech.citadellesgroupeq.*;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import org.json.simple.parser.ParseException;

import java.util.*;

/**
 * La classe Player représente un joueur dans le jeu Citadelles. Chaque joueur a un identifiant unique, une quantité
 * d'argent (cash), des cartes dans sa main (non posées dans sa cité), un rôle attribué
 */
public abstract class Player implements Comparable<Player>, Cloneable {

    public static final Random randomGenerator=new Random();
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
    private List<Citadel> cardsInHand;

    private List<Citadel> city;//la cité, où le joueur pose ses cartes

    private Role role;

    /**
     * public Player(int id) throws IOException {this(id, DEFAULT_CASH);deal2Cards();}
     * public Player(int id, int cash){
     *      this(id,cash,new ArrayList<>());}
     */

    public Player(int id) {
        this(id, DEFAULT_CASH, new ArrayList<>(),false);
        pickCard(new RoundSummary());
        pickCard(new RoundSummary());//no need to get summary
    }

    public Player(int id, int cash, List<Citadel> cards,boolean deadForThisTurn) {
        this.cash=cash;
        this.role=Role.EMPTY_ROLE;
        this.id=id;
        this.cardsInHand =new ArrayList<>(cards);//to make sure List is modifiable
        this.city=new ArrayList<>();
        this.deadForThisTurn = deadForThisTurn;
    }

    public int getCash() {
        return cash;
    }

    public List<Citadel> getCardsInHand() {return cardsInHand;}

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role=role;
    }

    public void setCash(int cash) {
        this.cash=(cash >= 0) ? cash : this.cash;
    }
    public void setCardsInHand(List<Citadel> cards) {this.cardsInHand = cards;}

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


    /**
     * Choisi un rôle à Assasiner
     * @param availableRoles les rôles disponible
     * @return un rôle
     */
    public Role selectRoleToKillAsAssassin(List<Role> availableRoles){
        Role assassinatedRole;
        do { //boucle while pour éviter qu'il se tue lui même
            assassinatedRole=availableRoles.get(randomGenerator.nextInt(availableRoles.size()));
        } while((assassinatedRole.equals(this.getRole())));
        return assassinatedRole;
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

    public boolean removeCardFromHand(Citadel cardToRemove) {
        return cardsInHand.remove(cardToRemove);
    }

    public void addCardToHand(Citadel cardToAdd) {
        cardsInHand.add(cardToAdd);
    }

    public void addAllCardsToHand(List<Citadel> cardsToAdd){
        cardsInHand.addAll(cardsToAdd);
    }


    /**
     * Sélectionne un rôle aléatoirement dans la liste availableRoles pour le joueur
     * @param availableRoles les rôles disponibles
     * @return l'id dans la liste fournie du rôle sélectionné
     */
    public int selectRole(List<Role> availableRoles) {
        int selectedRoleIndex=randomGenerator.nextInt(availableRoles.size());//la sélection est pour l'instant aléatoire
        setRole(availableRoles.get(selectedRoleIndex));
        return selectedRoleIndex;
    }

    /**
     *
     * @return les {@link Citadel} que le joueur a dans sa main et qu'il est capable d'acheter
     */
    public List<Citadel> getBuyableCards() {
        return getBuyableCards(getCash());
    }

    /**
     *
     * @param cashAvailable le montant maximal des cartes
     * @return La liste des cartes de sa main que le joueur est capable de payer
     */
    public List<Citadel> getBuyableCards(int cashAvailable) {
        List<Citadel> buyableCards = new ArrayList<>();
        for(Citadel card : cardsInHand) {
            if(card.getCost() <= cashAvailable) {
                buyableCards.add(card);
            }
        }
        return buyableCards;
    }

    /**
     * Permet de générer 2 cartes aléatoires. Utile pour proposer à un joueur 2 cartes parmi lesquelles choisir
     */
    public List<Citadel> generate2Cards(){
        try {
            CitadelsJSONReader citadelsReader = new CitadelsJSONReader();
            List<Citadel> citadelsList = citadelsReader.getCitadelsList();
            List<Citadel> dealCards = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                int randomIndex = randomGenerator.nextInt(citadelsList.size());
                Citadel randomCitadel = citadelsList.get(randomIndex);
                dealCards.add(randomCitadel);
            }
            return dealCards;
        }
        catch (ParseException e) {
            throw new RuntimeException("Impossible de lire les cartes dans le fichier JSON", e);
        }
    }

    /**
     * Choisit une carte parmi celles proposées pour l'ajouter au jeu du joueur.
     * @param cards les cartes proposées
     * @return la carte choisie
     */
    public Citadel pickCard(RoundSummary summary, List<Citadel> cards) {
        if(cards.isEmpty()) {
            throw new IllegalArgumentException("cards must not be empty.");
        }
        Citadel choosenCard=cards.get(randomGenerator.nextInt(cards.size()));

        addCardToHand(choosenCard);

        summary.addDrawnCard(choosenCard);

        return choosenCard;
    }

    /**
     * Appelle {@link #pickCard(RoundSummary, List)}, avec 2 cartes choisies aléatoirement.
     * @return la carte choisie par le joueur
     */
    public Citadel pickCard(RoundSummary summary) {
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


    public List<Citadel> getCity() { return city; }

    public void setCity(List<Citadel> city) {
        this.city = city;
    }

    public void addCitadelToCity(Citadel citadelToAdd) {
        this.city.add(citadelToAdd);
    }

    public void addAllCitadelsToCity(List<Citadel> citadelsToAdd) {
        this.city.addAll(citadelsToAdd);
    }

    public boolean removeCitadelFromCity(Citadel citadelToRemove) {
        return city.remove(citadelToRemove);
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
        for(Citadel citadel: city) {
            sum+=citadel.getCost();
        }

        return sum;
    }

    public boolean hasEmptyCity() {
        return this.city.isEmpty();
    }

    /**
     *
     * @return la citadelle que le joueur a choisi d'acheter (par défaut, la moins chère). Si le joueur n'est pas en mesure d'acheter une citadelle, l'Optional est empty
     */
    public Optional<Citadel> getChoosenCitadelToBuy() {
        List<Citadel> buyableCitadels=getBuyableCards();

        if(buyableCitadels.isEmpty()) return Optional.empty();

        return Optional.of(Collections.min(buyableCitadels));
    }

    public void playPlayerTurn(RoundSummary summary) {
        for(Citadel cartePosee: city) {
            if(cartePosee.getColor() == role.getColor() && role.getColor()!= Color.GRAY) {
                addCoins(1);
                summary.addCoinsWonByColorCards(1);
            }
        }
    }
}