package fr.cotedazur.univ.polytech.citadellesgroupeq;

import java.util.Objects;

public class District implements Comparable<District> {
    private String name;
    private int cost;
    private Color color;
    private String power;

    public District(String name, int cost, Color color, String power) {
        this.name=name;
        this.cost=cost;
        this.color=color;
        this.power=power;
    }

    public District(String name, int cost, String colorString, String power) {
        this(name, cost, Color.valueOf(colorString.toUpperCase()), power);
    }

    public String getPower(){
        return power;
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

    public void setColor(Color color){
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public int compareTo(District districtToCompare) {
        int otherCost= districtToCompare.getCost();
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
        District district = (District) o;
        return cost == district.cost && Objects.equals(name, district.name) && color == district.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, cost, color);
    }
}
