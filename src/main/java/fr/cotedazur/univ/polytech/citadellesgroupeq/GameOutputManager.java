package fr.cotedazur.univ.polytech.citadellesgroupeq;

import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;

import java.util.AbstractMap;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("java:S106")//asks for printing with a logger instead of System.out.println
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
                describePlayerRound(player, game);
            }


            game.resuscitateAllPlayers();
            System.out.println("--------------");
        }
        System.out.println("Le joueur "+game.whoIsTheWinner().getBotLogicName()+game.whoIsTheWinner().getStrategyName()+" a gagné avec un score de "+game.getScoreOfEnd().get(game.whoIsTheWinner()));
        System.out.println("A noter pour le décompte des points, qu'il possédait ces merveilles: (rien si pas de cartes violettes)");
        for(District district: game.whoIsTheWinner().getCity()) {
            if(district.getColor() == Color.PURPLE) {
                System.out.println("* " + district.getName());
            }
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
            System.out.print("Le joueur " + player.getBotLogicName()+player.getStrategyName());
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
                            System.out.println("et a tué le joueur " + p.getRole() + " de nom " + p.getBotLogicName()+p.getStrategyName());
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
                        System.out.println("et décide d'échanger ses cartes avec le joueur " + exchangedWith.getRole() + " qui est le joueur " + exchangedWith.getBotLogicName()+exchangedWith.getStrategyName());
                    }
                }
                else if(player.getRole().equals(Role.MARCHAND)){
                    System.out.println("et gagne une pièce bonus");
                }
                else if(player.getRole().equals(Role.VOLEUR)){
                    System.out.println("et a volé le joueur " + summary.getStealedRole());
                }
                else if (player.getRole() == Role.CONDOTTIERE && summary.getOptionalDestroyedDistrict().isPresent()) {
                    AbstractMap.SimpleEntry<Integer, District> districtDestroyed=summary.getOptionalDestroyedDistrict().get();
                    System.out.println(" et a détruit le district " + districtDestroyed.getValue().getName() + " du joueur d'id " + districtDestroyed.getKey());
                }
                else if(player.getRole() == Role.ARCHITECTE) {
                    System.out.println("Il a donc automatiquement gagné 2 cartes de plus.");
                }
            }

            if (summary.hasWonCoinsByColorCards()) {
                System.out.println("Grâce à sa couleur et à ses cartes, il a gagné " + summary.getCoinsWonByColorCards() + " pièces.");
            }
            Optional<District> optionalEcoleDeMagie = player.getDistrictInCity("Ecole de magie");
            if (optionalEcoleDeMagie.isPresent()){
                District ecoleDeMagie = optionalEcoleDeMagie.get();
                if(ecoleDeMagie.getColor() == player.getRole().getColor()){
                    System.out.println("Dont une pièce grâce à l'Ecole de magie");
                }
            }

            Optional<District> optionalCimetiere = player.getDistrictInCity("Cimetiere");
            if (optionalCimetiere.isPresent() && summary.getOptionalDestroyedDistrict().isPresent()){
                District cimetiere = optionalCimetiere.get();
                District destroyedDistrict = summary.getOptionalDestroyedDistrict().get().getValue();
                if(summary.getUsedMerveilles().contains(cimetiere.getName())){
                    System.out.println("Grace au cimetiere le joueur recupere dans sa main pour une piece la carte :" + destroyedDistrict.getName());
                }
            }

            if (summary.hasPickedCards()) {
                System.out.println("Il a choisi de piocher 1 carte: " + summary.getDrawnCards().get(0).getName());
            } else if (!player.isDeadForThisTurn()) {
                System.out.println("Il a choisi de prendre 2 pieces, ce qui l'amene a: " + player.getCash() + " pieces. (après achat de la citadelle si il y a eu)");
            }
        }

        if(summary.hasBoughtDistricts()) {
            System.out.println("Il a acheté cette/ces carte(s):");
            System.out.println(getDescriptionOfCards(summary.getBoughtDistricts()));
        }
        if(summary.hasFinishDuringTurn()) {
            System.out.println("Il est le premier premier à finir, car il possède dans sa cité " + GameLogicManager.NUMBER_OF_DISTRICTS_TO_WIN + " citadelles.\n");
            System.out.println("Voici sa cité");
            System.out.println(getDescriptionOfCards(player.getCity()));
        }
        if(summary.hasUsedMerveillePower()){
            System.out.println("Des pouvoirs de merveilles sont actifs. Les voici:");
            for(String city: summary.getUsedMerveilles()){
                System.out.println(city);
            }
        }
        System.out.println("\n");
    }


    /**
     * décrit l'état actuel d'un joueur avant son tour: son rôle, ses cartes en main, et celles dans sa cité, et son cash
     * @param player
     */
    public void describePlayerState(Player player) {
        System.out.print("Joueur " + player.getBotLogicName()+player.getStrategyName());
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
