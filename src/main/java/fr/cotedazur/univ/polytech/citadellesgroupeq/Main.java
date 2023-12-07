package fr.cotedazur.univ.polytech.citadellesgroupeq;

import java.util.List;

public class Main {

    public static void main(String... args) {
        GameManager game = new GameManager();
        int tours=1;
        int playersNumbers = 2;

        while(!game.isFinished()) {
            System.out.println("Tour : " + tours++ + "\n--------------\n");
            List<Role> availableRoles = game.generateAvailableRoles(playersNumbers);
            describeRoles(availableRoles);

            describeRolePicking(game.makeAllPlayersSelectRole(availableRoles), game.getMasterOfTheGameIndex());

            for(Player player : game.getPlayerTreeSet()) {
                System.out.println(player.getDescriptionOfCards());
                if(!game.isFinished()) {//actuellement, on s'arrête DES qu'un joueur a 8 cartes. Dans la version finale, il faudra laisser la fin du tour
                    describePlayerRound(player, game);
                }
            }
            System.out.println("--------------");
        }
        System.out.println("Jeu fini !");
    }

    public static void describeRoles(List<Role> listeRoles) {
        System.out.println("Les rôles disponible sont: ");
        for(Role role: listeRoles) {
            System.out.print("\t-"+role.name());
        }
        System.out.println("\n");
    }

    public static void describeRolePicking(List<Player> players, int masterOfTheGameIndex) {
        for(Player player: players) {
            System.out.print("Le joueur " + player.getId());
            if(player.getId() == masterOfTheGameIndex) {
                System.out.print(" (maître du jeu)");
            }
            System.out.println(" a choisi son rôle: " + player.getRole());
        }
    }

    public static void describePlayerRound(Player player, GameManager game) {
        System.out.print("Joueur " + player.getId());
        System.out.println(" joue son tour, en tant que " + player.getRole().name() + "("+player.getRole().ordinal()+").");
        System.out.println("Il possède actuellement " + player.getCash() + " pièces, et ces cartes: ");
        System.out.println(getDescriptionOfCards(player.getCards()));

        RoundSummary summary = game.playPlayerTurn(player);

        if(summary.pickedCards()) {
            System.out.println("Il a choisi de piocher 2 cartes");
        }
        else {
            System.out.println("Il a choisi de prendre 2 pièces, ce qui l'amène à: " + player.getCash() + " pièces.");
        }

        if(summary.boughtCitadels()) {
            System.out.println("Il a assez pour acheter ces cartes: ");
            System.out.println(getDescriptionOfCards(summary.getBoughtCitadels()));
            System.out.println("Il a donc gagné !");
        }
        System.out.println("\n");
    }

    /**
     *
     * @return Un {@link String} contenant la liste des cartes dans la main du joueur. PAS CELLES POSEES DANS SA CITE
     */
    public static String getDescriptionOfCards(List<Citadel> cards) {
        if(!cards.isEmpty()) {
            StringBuilder output = new StringBuilder("Cartes : \n");
            for (Citadel card : cards) {
                output.append("\t*").append(card.getName()).append(" : ").append(card.getCost()).append("\n");
            }
            return output.toString();
        }
        else {
            return "";
        }
    }
}