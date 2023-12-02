package fr.cotedazur.univ.polytech.citadellesgroupeq;

import java.io.IOException;

public class Main {

    public static void main(String... args) {
        GameManager game = new GameManager();
        int tours=1;
        int playersNumbers=2;

        while(!game.isFinished()) {
            System.out.println("Tour : " + tours++ + "\n--------------\n");
            System.out.println(game.makeAllPlayersSelectRole());
            for(Player player : game.getPlayerTreeSet()) {
                if(!game.isFinished()) {//actuellement, on s'arrête DES qu'un joueur a 8 cartes. Dans la version finale, il faudra laisser la fin du tour
                    System.out.println(game.playPlayerTurn(player));
                }
            }
            System.out.println("--------------");
        }
        System.out.println("Jeu fini !");
    }

}