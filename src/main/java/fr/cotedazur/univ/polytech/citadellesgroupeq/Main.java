package fr.cotedazur.univ.polytech.citadellesgroupeq;

public class Main {

    public static void main(String... args) {
        System.out.println("Main");

        GameManager game = new GameManager();

        while(!game.isFinished()) {
            System.out.println(game.makeAllPlayersSelectRole());
            for(Player player : game.getPlayerTreeSet()) {
                if(!game.isFinished()) {//actuellement, on s'arrête DES qu'un joueur a 8 cartes. Dans la version finale, il faudra laisser la fin du tour
                    System.out.println(game.playPlayerTurn(player));
                }
            }
        }
        System.out.println("Game done !");
    }

}