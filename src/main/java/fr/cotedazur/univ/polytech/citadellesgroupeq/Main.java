package fr.cotedazur.univ.polytech.citadellesgroupeq;

import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;

import java.util.List;

public class Main {

    public static void main(String... args) {
        GameManager game = new GameManager();
        int tours=1;
        int playersNumbers = 4;

        while(!game.isFinished()) {
            System.out.println("Tour : " + tours++ + "\n--------------\n");
            List<Role> availableRoles = game.generateAvailableRoles(playersNumbers);
            describeRoles(availableRoles);

            describeRolePicking(game.makeAllPlayersSelectRole(availableRoles), game.getMasterOfTheGameIndex());

            for(Player player : game.getPlayerTreeSet()) {
                if(!game.isFinished()) {//actuellement, on s'arrête DES qu'un joueur a 8 cartes. Dans la version finale, il faudra laisser la fin du tour
                    describePlayerRound(player, game);
                }
            }


            game.resuscitateAllPlayers();
            System.out.println("--------------");
        }
        System.out.println("Jeu fini !");
    }

    public static void describeRoles(List<Role> listeRoles) {
        System.out.println("Les roles disponibles sont: ");
        for(Role role: listeRoles) {
            System.out.print("\t-"+role.name());
        }
        System.out.println("\n");
    }

    public static void describeRolePicking(List<Player> players, int masterOfTheGameIndex) {
        for(Player player: players) {
            System.out.print("Le joueur " + player.getId());
            if(player.getId() == masterOfTheGameIndex) {
                System.out.print(" (maitre du jeu)");
            }
            System.out.println(" a choisi son role: " + player.getRole() + " ("+player.getRole().getColor().name()+")");
        }
    }

    public static void describePlayerRound(Player player, GameManager game) {
        System.out.print("Joueur " + player.getId());
        System.out.println(" joue son tour, en tant que " + player.getRole().name() + "("+player.getRole().ordinal()+"-" + player.getRole().getColor().name()+").");
        System.out.println("Il possede actuellement " + player.getCash() + " pieces, et ces cartes en main: ");
        System.out.println(getDescriptionOfCards(player.getCardsInHand()));
        if(player.hasEmptyCity()) {
            System.out.println("Il n'a rien dans sa cité.");
        }
        else {
            System.out.println("Sa cité contient: ");
            System.out.println(getDescriptionOfCards(player.getCity()));
        }

        RoundSummary summary = game.playPlayerTurn(player);
        if(summary.hasBeenKilled()){
            System.out.println("Ce joueur a été tué par l'assassin, il ne peut donc pas effectuer son tour");
        }
        else {
            if (summary.hasUsedPower()) {
                System.out.println("Ce joueur utilise son pouvoir de " + player.getRole().name());
                if(player.getRole() == Role.ASSASSIN) {
                    for (Player p : game.getPlayersList()) {
                        if (p.isDeadForThisTurn()) {
                            System.out.println("et a tué le joueur " + p.getRole() + " qui est le joueur " + p.getId());
                            break;
                        }
                    }
                }
                else if(player.getRole()==Role.MAGICIEN) {
                    if(summary.hasExchangedCardsWithPileAsMagician()) {
                        System.out.println("Il a échangé des cartes avec la pile.");
                    }
                    else {//a échangé ses cartes avec un joueur
                        System.out.println("Il a échangé ses cartes avec le joueur d'id "  + summary.getExchangedCardsPlayerId());
                    }
                }
            }

            if (summary.hasWonCoinsByColorCards()) {
                System.out.println("Grâce à sa couleur et à ses cartes, il a gagné " + summary.getCoinsWonByColorCards() + " pièces.");
            }

            if (summary.hasPickedCards()) {
                System.out.println("Il a choisi de piocher 1 carte: " + summary.getDrawnCards().get(0).getName());
            } else if (!player.isDeadForThisTurn()) {
                System.out.println("Il a choisi de prendre 2 pieces, ce qui l'amene a: " + player.getCash() + " pieces. (après achat du district si il y a eu)");
            }
        }

        if(summary.hasBoughtDistricts()) {
            System.out.println("Il a acheté cette carte:");
            System.out.println(getDescriptionOfCards(summary.getBoughtDistricts()));
        }
        if(summary.hasWonDuringTurn()) {
            System.out.println("Il a gagné, car il possède dans sa cité " + GameManager.NUMBER_OF_DISTRICTS_TO_WIN + " districts.\n");
            System.out.println("Voici sa cité");
            System.out.println(getDescriptionOfCards(player.getCity()));
        }
        System.out.println("\n");
    }

    /**
     *
     * @return Un {@link String} contenant la liste des cartes dans la main du joueur. PAS CELLES POSEES DANS SA CITE
     */
    public static String getDescriptionOfCards(List<District> cards) {
        if(!cards.isEmpty()) {
            StringBuilder output = new StringBuilder("Cartes : \n");
            for (District card : cards) {
                output.append("\t*").append(card.getName()).append(" : ");
                output.append(card.getCost());
                output.append(" (").append(card.getColor().name()).append(")");
                output.append("\n");
            }
            return output.toString();
        }
        else {
            return "";
        }
    }
}