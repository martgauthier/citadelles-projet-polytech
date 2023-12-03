package fr.cotedazur.univ.polytech.citadellesgroupeq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Player implements Comparable<Player> {

    public static final Random randomGenerator=new Random();
    private int cash;
    public static final int DEFAULT_CASH=0;
    public static final List<Citadel> DEFAULT_CARDS=Arrays.asList(new Citadel("Temple", 9), new Citadel("Eglise", 8));

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

    public Player(int id) {this(id, DEFAULT_CASH,DEFAULT_CARDS);}

    public Player(int id, int cash, List<Citadel> cards) {
        this.cash=cash;
        this.role=Role.EMPTY_ROLE;
        this.id=id;
        this.cards=cards;
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
    public void draw2Coins() {
        this.cash+=2;
    }

    public void add(int coins) {
        if(coins >= 0) {
            this.cash+=coins;
        }
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
     * Permet de distribuer deux cartes quartiers de manière aléatoire à un joueur
     *
     */
    public void deal2Cards() throws IOException {
        CitadelsJSONReader citadelsReader = new CitadelsJSONReader();
        List<Citadel> citadelsList = citadelsReader.getCitadelsList();
        List<Citadel> dealCards = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            int randomIndex = randomGenerator.nextInt(citadelsList.size());
            Citadel randomCitadel = citadelsList.get(randomIndex);
            dealCards.add(randomCitadel);
        }
        setCards(dealCards);
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
}