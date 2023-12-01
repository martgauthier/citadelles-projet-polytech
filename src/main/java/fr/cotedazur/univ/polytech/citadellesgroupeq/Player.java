package fr.cotedazur.univ.polytech.citadellesgroupeq;

import java.util.List;
import java.util.Random;

public class Player {

    public static final Random randomGenerator=new Random();
    private int cash;
    public static final int DEFAULT_CASH=0;

    private Role role;

    public Player() {
        this(DEFAULT_CASH);
    }

    public Player(int cash) {
        this.cash=cash;
        this.role=Role.EMPTY_ROLE;
    }

    public int getCash() {
        return cash;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role=role;
    }

    public void setCash(int cash) {
        this.cash=(cash >= 0) ? cash : this.cash;
    }

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

    public int selectRole(List<Role> availableRoles) {
        int selectedRoleIndex=randomGenerator.nextInt(availableRoles.size());//la sélection est pour l'instant aléatoire
        setRole(availableRoles.get(selectedRoleIndex));
        return selectedRoleIndex;
    }
}