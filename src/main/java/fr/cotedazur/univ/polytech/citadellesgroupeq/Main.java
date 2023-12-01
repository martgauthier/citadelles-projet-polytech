package fr.cotedazur.univ.polytech.citadellesgroupeq;

public class Main {

    public static void main(String... args) {
        System.out.println("Main");

        GameManager game = new GameManager();

        System.out.println(game.makeAllPlayersSelectRole());
    }

}
