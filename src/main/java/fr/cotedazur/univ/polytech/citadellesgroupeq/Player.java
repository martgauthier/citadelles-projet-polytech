package fr.cotedazur.univ.polytech.citadellesgroupeq;

public class Player {
    private int cash;
    public static final int DEFAULT_CASH=0;

    public Player() {
        this(DEFAULT_CASH);
    }

    public Player(int cash) {
        this.cash=cash;
    }

    public int getCash() {
        return cash;
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
}
