package fr.cotedazur.univ.polytech.citadellesgroupeq;

import org.json.simple.parser.ParseException;
import java.util.*;

/**
 * La classe Player représente un joueur dans le jeu Citadelles. Chaque joueur a un identifiant unique, une quantité
 * d'argent (cash), des cartes dans sa main (non posées dans sa cité), un rôle attribué
 */
public class Player implements Comparable<Player>, Cloneable {

    public static final Random randomGenerator=new Random();
    private int cash;
    public static final int DEFAULT_CASH=0;

    /**
     * Identification du bot: bot numéro 0 -> id=0, bot numéro 1 -> id=1...
     */
    private final int id;

    /**
     * Cartes que le joueur contient dans sa main. PAS LES CARTES POSÉES DANS SA CITE
     */
    private List<Citadel> cards;

    private Role role;

    /**
     * public Player(int id) throws IOException {this(id, DEFAULT_CASH);deal2Cards();}
     * public Player(int id, int cash){
     *      this(id,cash,new ArrayList<>());}
     */

    public Player(int id) {
        this(id, DEFAULT_CASH, new ArrayList<>());
        pickCard(new RoundSummary());
        pickCard(new RoundSummary());//no need to get summary
    }

    public Player(int id, int cash, List<Citadel> cards) {
        this.cash=cash;
        this.role=Role.EMPTY_ROLE;
        this.id=id;
        this.cards=new ArrayList<>(cards);//to make sure List is modifiable
    }

    public int getCash() {
        return cash;
    }

    public List<Citadel> getCards() {return cards;}

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role=role;
    }

    public void setCash(int cash) {
        this.cash=(cash >= 0) ? cash : this.cash;
    }
    public void setCards(List<Citadel> cards) {this.cards = cards;}

    public boolean stillHasCash() {
        return (cash > 0);
    }

    /**
     * Ajoute 2 au cash du joueur. Utile pour chaque début de tour
     */
    public void draw2Coins(RoundSummary summary) {
        this.cash+=2;
        summary.addCoins(2);
    }

    public void add(int coins) {
        if(coins >= 0) {
            this.cash+=coins;
        }
    }

    public void addCard(Citadel cardToAdd) {
        cards.add(cardToAdd);
    }

    public void addCards(List<Citadel> cardsToAdd){
        cards.addAll(cardsToAdd);
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
        for(Citadel card : cards) {
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

        addCard(choosenCard);

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

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder("Rôle: ");
        output.append(role.name()).append("\n");
        output.append("Cash: ").append(getCash()).append("\n");
        output.append(getDescriptionOfCards());

        return output.toString();
    }

    /**
     *
     * @return Un {@link String} contenant la liste des cartes dans la main du joueur. PAS CELLES POSEES DANS SA CITE
     */
    public String getDescriptionOfCards() {
        if(!cards.isEmpty()) {
            StringBuilder output = new StringBuilder("Cartes en main: \n");
            for (Citadel card : cards) {
                output.append("\t*").append(card.getName()).append(" : ").append(card.getCost()).append("\n");
            }
            return output.toString();
        }
        else {
            return "";
        }
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

    public Object clone() {
        Object o = null;
        try {
            // On récupère l'instance à renvoyer par l'appel de la
            // méthode super.clone()
            o = super.clone();
        } catch(CloneNotSupportedException cnse) {
            // Ne devrait jamais arriver, car nous implémentons
            // l'interface Cloneable
            cnse.printStackTrace(System.err);
        }
        // on renvoie le clone
        return o;
    }
}