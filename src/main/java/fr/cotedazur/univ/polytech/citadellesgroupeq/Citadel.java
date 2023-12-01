package fr.cotedazur.univ.polytech.citadellesgroupeq;

public class Citadel {
    private String name;
    private int cost;

    public Citadel(String name, int cost) {
        this.name=name;
        this.cost=cost;
    }

    public int getCost() {
        return cost;
    }

    public String getName() {
        return name;
    }

    public void setCost(int cost) {
        if(0<=cost) {
            this.cost=cost;
        }
    }
}
