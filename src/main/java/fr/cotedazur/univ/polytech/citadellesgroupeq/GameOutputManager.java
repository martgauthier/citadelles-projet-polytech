package fr.cotedazur.univ.polytech.citadellesgroupeq;

import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;

import java.util.AbstractMap;
import java.util.List;

public class GameOutputManager {
    private GameLogicManager game;
    private int rounds;
    private int playersNumber;

    public GameOutputManager() {
        game=new GameLogicManager();
        rounds=1;
        playersNumber=4;
    }

    public GameOutputManager(List<Player> playerList) {
        game=new GameLogicManager(playerList);
        rounds=1;
        playersNumber=playerList.size();
    }

    public void startMainOutputLoop() {
        while(!game.isFinished()) {//pour chaque manche
            System.out.println("Tour : " + rounds ++ + "\n--------------\n");
            List<Role> availableRoles = game.generateAvailableRoles(playersNumber);

            describeRoles(availableRoles);//liste les rôles rendus disponibles à choisir par le jeu

            describeRolePicking(game.makeAllPlayersSelectRole(availableRoles), game.getMasterOfTheGameIndex());//décrit les rôles choisis par chaque joueur

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


    public void describeRoles(List<Role> listeRoles) {
        System.out.println("Les roles disponibles sont: ");
        for(Role role: listeRoles) {
            System.out.print("\t-"+role.name());
        }
        System.out.println("\n");
    }

    public void describeRolePicking(List<Player> players, int masterOfTheGameIndex) {
        for(Player player: players) {
            System.out.print("Le joueur " + player.getId());
            if(player.getId() == masterOfTheGameIndex) {
                System.out.print(" (maitre du jeu)");
            }
            System.out.println(" a choisi son role: " + player.getRole() + " ("+player.getRole().getColor().name()+")");
        }
    }

    public void describePlayerRound(Player player, GameLogicManager game) {
        describePlayerState(player);

        RoundSummary summary = game.playPlayerTurn(player);
        if(summary.hasBeenKilled()){
            System.out.println("Ce joueur a été tué par l'assassin, il ne peut donc pas effectuer son tour");
        }
        else {
            if (summary.hasUsedPower()) {
                System.out.println("Ce joueur utilise son pouvoir de " + player.getRole().name());
                if(player.getRole().equals(Role.ASSASSIN)) {
                    for (Player p : game.getPlayersList()) {
                        if (p.isDeadForThisTurn()) {
                            System.out.println("et a tué le joueur " + p.getRole() + " d'id " + p.getId());
                            break;
                        }
                    }
                }
                else if(player.getRole().equals(Role.MAGICIEN)){
                    if(summary.hasExchangedCardsWithPileAsMagician()) {
                        System.out.println("et décide d'échanger ses cartes avec la pioche et il a échangé "+summary.getExchangedCardsWithPileIndex().length+" cartes.");
                    }
                    else {
                        Player exchangedWith = game.getPlayersList().get(summary.getExchangedCardsPlayerId());
                        System.out.println("et décide d'échanger ses cartes avec le joueur " + exchangedWith.getRole() + " qui est le joueur " + exchangedWith.getId());
                    }
                }
                else if(player.getRole().equals(Role.VOLEUR)){
                    System.out.println("et a volé le joueur " + summary.getStealedRole());
                }
                else if (player.getRole() == Role.CONDOTTIERE && summary.getOptionalDestroyedDistrict().isPresent()) {
                    AbstractMap.SimpleEntry<Integer, District> districtDestroyed=summary.getOptionalDestroyedDistrict().get();
                    System.out.println(" et a détruit le district " + districtDestroyed.getValue().getName() + " du joueur d'id " + districtDestroyed.getKey());
                }
            }

            if (summary.hasWonCoinsByColorCards()) {
                System.out.println("Grâce à sa couleur et à ses cartes, il a gagné " + summary.getCoinsWonByColorCards() + " pièces.");
            }

            if (summary.hasPickedCards()) {
                System.out.println("Il a choisi de piocher 1 carte: " + summary.getDrawnCards().get(0).getName());
            } else if (!player.isDeadForThisTurn()) {
                System.out.println("Il a choisi de prendre 2 pieces, ce qui l'amene a: " + player.getCash() + " pieces. (après achat de la citadelle si il y a eu)");
            }
        }

        if(summary.hasBoughtDistricts()) {
            System.out.println("Il a acheté cette carte:");
            System.out.println(getDescriptionOfCards(summary.getBoughtDistricts()));
        }
        if(summary.hasWonDuringTurn()) {
            System.out.println("Il a gagné, car il possède dans sa cité " + GameLogicManager.NUMBER_OF_DISTRICTS_TO_WIN + " citadelles.\n");
            System.out.println("Voici sa cité");
            System.out.println(getDescriptionOfCards(player.getCity()));
        }
        System.out.println("\n");
    }


    /**
     * décrit l'état actuel d'un joueur avant son tour: son rôle, ses cartes en main, et celles dans sa cité, et son cash
     * @param player
     */
    public void describePlayerState(Player player) {
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
    }

    /**
     *
     * @return Un {@link String} contenant la liste des cartes dans la main du joueur. PAS CELLES POSEES DANS SA CITE
     */
    public String getDescriptionOfCards(List<District> cards) {
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
