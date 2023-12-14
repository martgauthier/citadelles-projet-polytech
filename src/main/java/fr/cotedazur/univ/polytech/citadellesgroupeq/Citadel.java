package fr.cotedazur.univ.polytech.citadellesgroupeq;

import java.util.Objects;

public class Citadel implements Comparable<Citadel> {
    private String name;
    private int cost;

    private Color color;

    public Citadel(String name, int cost, Color color) {
        this.name=name;
        this.cost=cost;
        this.color=color;
    }

    public Citadel(String name, int cost, String colorString) {
        this(name, cost, Color.valueOf(colorString.toUpperCase()));
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

    public Color getColor() {
        return color;
    }

    @Override
    public int compareTo(Citadel citadelToCompare) {
        int otherCost= citadelToCompare.getCost();
        if(this.cost > otherCost) {
            return 1;
        }
        else if(this.cost < otherCost) {
            return -1;
        }
        else {
            return 0;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Citadel citadel = (Citadel) o;
        return cost == citadel.cost && Objects.equals(name, citadel.name) && color == citadel.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, cost, color);
    }
}
