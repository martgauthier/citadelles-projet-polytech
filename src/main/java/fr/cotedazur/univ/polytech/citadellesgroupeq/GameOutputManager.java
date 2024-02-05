package fr.cotedazur.univ.polytech.citadellesgroupeq;

import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.loggerformat.SimpleFormatterWithoutDate;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;

import java.util.AbstractMap;
import java.util.List;
import java.util.Optional;
import java.util.logging.*;


public class GameOutputManager {
    private GameLogicManager game;
    private int rounds;
    private int playersNumber;
    private static final Logger GAMEPLAY_LOGGER = Logger.getLogger("Gameplay_Logger");

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

    public void setupLogger(Level newLevel){
        GAMEPLAY_LOGGER.setLevel(newLevel);
        GAMEPLAY_LOGGER.getParent().getHandlers()[0].setFormatter(new SimpleFormatterWithoutDate());
    }

    public void startMainOutputLoop() {
        setupLogger(Level.INFO);
        while(!game.isFinished()) {//pour chaque manche
            GAMEPLAY_LOGGER.log(Level.INFO, "Tour : {}\n--------------\n", rounds++);
            List<Role> availableRoles = game.generateAvailableRoles(playersNumber);

            describeRoles(availableRoles);//liste les rôles rendus disponibles à choisir par le jeu

            describeRolePicking(game.makeAllPlayersSelectRole(availableRoles), game.getMasterOfTheGameIndex());//décrit les rôles choisis par chaque joueur
            for(Player player : game.getPlayerTreeSet()) {
                describePlayerRound(player, game);
            }


            game.resuscitateAllPlayers();
            GAMEPLAY_LOGGER.info("--------------");
        }
        GAMEPLAY_LOGGER.info("Voici le score des joueurs qui n'ont pas gagné:");
        for(Player player : game.getPlayersList()){
            if(player!=game.whoIsTheWinner()){
                GAMEPLAY_LOGGER.log(Level.INFO,
                        "Le joueur {} {}  a un score de {}",
                        new Object[]{player.getBotLogicName(), player.getStrategyName(), game.getScoreOfEnd().get(player)}
                );
            }
        }
        GAMEPLAY_LOGGER.log(
                Level.INFO, "{} {} a gagné avec un score de {} ",
                new Object[]{game.whoIsTheWinner().getBotLogicName(),game.whoIsTheWinner().getStrategyName(),game.getScoreOfEnd().get(game.whoIsTheWinner())}
        );

        GAMEPLAY_LOGGER.info("A noter pour le décompte des points, qu'il possédait ces merveilles: (rien si pas de cartes violettes)");
        for(District district: game.whoIsTheWinner().getCity()) {
            if(district.getColor() == Color.PURPLE) {
                GAMEPLAY_LOGGER.info("* " + district.getName());
            }
        }
        GAMEPLAY_LOGGER.info("Jeu fini !");
    }


    public void describeRoles(List<Role> listeRoles) {
        GAMEPLAY_LOGGER.info("Les roles disponibles sont: ");
        for(Role role: listeRoles) {
            GAMEPLAY_LOGGER.info("\t-"+role.name());
        }
        GAMEPLAY_LOGGER.info("\n");
    }

    public void describeRolePicking(List<Player> players, int masterOfTheGameIndex) {
        for(Player player: players) {
            GAMEPLAY_LOGGER.log(Level.INFO,
                    "Le joueur {} {}", new Object[]{player.getBotLogicName(),player.getStrategyName()}
            );
            if(player.getId() == masterOfTheGameIndex) {
                GAMEPLAY_LOGGER.info(" (maitre du jeu)");
            }
            GAMEPLAY_LOGGER.log(Level.INFO, " a choisi son role: {} ({})",
                    new Object[]{player.getRole(), player.getRole().getColor().name()}
            );
        }
    }

    public void describePlayerRound(Player player, GameLogicManager game) {
        describePlayerState(player);

        RoundSummary summary = game.playPlayerTurn(player);

        if(summary.hasBeenKilled()){
            GAMEPLAY_LOGGER.info("Ce joueur a été tué par l'assassin, il ne peut donc pas effectuer son tour");
        }
        else {
            if (summary.hasUsedPower()) {
                GAMEPLAY_LOGGER.info("Ce joueur utilise son pouvoir de " + player.getRole().name());
                if(player.getRole().equals(Role.ASSASSIN)) {
                    for (Player p : game.getPlayersList()) {
                        if (p.isDeadForThisTurn()) {
                            GAMEPLAY_LOGGER.log(Level.INFO, "et a tué le joueur {} de nom {} {}",
                                    new Object[]{p.getRole(), p.getBotLogicName(), p.getStrategyName()}
                            );
                            break;
                        }
                    }
                }
                else if(player.getRole().equals(Role.MAGICIEN)){
                    if(summary.hasExchangedCardsWithPileAsMagician()) {
                        GAMEPLAY_LOGGER.log(Level.INFO,
                                "et décide d'échanger ses cartes avec la pioche et il a échangé {} cartes.",
                                summary.getExchangedCardsWithPileIndex().length
                        );
                    }
                    else {
                        Player exchangedWith = game.getPlayersList().get(summary.getExchangedCardsPlayerId());
                        GAMEPLAY_LOGGER.log(Level.INFO,
                                "et décide d'échanger ses cartes avec le joueur {} qui est le joueur {} {}",
                                new Object[]{exchangedWith.getRole(),exchangedWith.getBotLogicName(), exchangedWith.getStrategyName()}
                        );
                    }
                }
                else if(player.getRole().equals(Role.MARCHAND)){
                    GAMEPLAY_LOGGER.info("et gagne une pièce bonus");
                }
                else if(player.getRole().equals(Role.VOLEUR)){
                    GAMEPLAY_LOGGER.info("et a volé le joueur " + summary.getStealedRole());
                }
                else if (player.getRole() == Role.CONDOTTIERE && summary.getOptionalDestroyedDistrict().isPresent()) {
                    AbstractMap.SimpleEntry<Integer, District> districtDestroyed=summary.getOptionalDestroyedDistrict().get();
                    GAMEPLAY_LOGGER.log(Level.INFO, " et a détruit le district {} du joueur d'id {}",
                            new Object[]{districtDestroyed.getValue().getName(), districtDestroyed.getKey()}
                    );
                }
                else if(player.getRole() == Role.ARCHITECTE) {
                    GAMEPLAY_LOGGER.info("Il a donc automatiquement gagné 2 cartes de plus.");
                }
            }

            if (summary.hasWonCoinsByColorCards()) {
                GAMEPLAY_LOGGER.log(Level.INFO,"Grâce à sa couleur et à ses cartes, il a gagné {} pièces.",
                        summary.getCoinsWonByColorCards()
                );
            }
            Optional<District> optionalEcoleDeMagie = player.getDistrictInCity("Ecole de magie");
            if (optionalEcoleDeMagie.isPresent()){
                District ecoleDeMagie = optionalEcoleDeMagie.get();
                if(ecoleDeMagie.getColor() == player.getRole().getColor()){
                    GAMEPLAY_LOGGER.info("Dont une pièce grâce à l'Ecole de magie");
                }
            }


            if (summary.hasPickedCards()) {
                GAMEPLAY_LOGGER.log(Level.INFO,"Il a choisi de piocher 1 carte: {}",
                        summary.getDrawnCards().get(0).getName()
                );
            } else if (!player.isDeadForThisTurn()) {
                GAMEPLAY_LOGGER.log(Level.INFO,
                        "Il a choisi de prendre 2 pieces, ce qui l'amene a: {} pièces. (après achat de la citadelle si il y a eu)",
                        player.getCash());
            }
        }

        if(summary.hasBoughtDistricts()) {
            GAMEPLAY_LOGGER.info("Il a acheté cette/ces carte(s):");
            GAMEPLAY_LOGGER.log(Level.INFO, "{}",getDescriptionOfCards(summary.getBoughtDistricts()));
        }
        if(summary.hasFinishDuringTurn()) {
            GAMEPLAY_LOGGER.log(Level.INFO, "Il est le premier premier à finir, car il possède dans sa cité {} citadelles.\n",
                    GameLogicManager.NUMBER_OF_DISTRICTS_TO_WIN
            );
            GAMEPLAY_LOGGER.info("Voici sa cité");
            GAMEPLAY_LOGGER.log(Level.INFO, "{}",getDescriptionOfCards(player.getCity()));
        }
        if(summary.hasUsedMerveillePower()){
            GAMEPLAY_LOGGER.info("Des pouvoirs de merveilles sont actifs. Les voici:");
            for(String city: summary.getUsedMerveilles()){
                GAMEPLAY_LOGGER.info(city);
            }
        }
        GAMEPLAY_LOGGER.info("\n");
    }


    /**
     * Décrit l'état actuel d'un joueur avant son tour : son rôle, ses cartes en main, et celles dans sa cité, et son cash
     * @param player joueur
     */
    public void describePlayerState(Player player) {
        GAMEPLAY_LOGGER.log(Level.INFO,
                "Joueur {}{}",new Object[]{player.getBotLogicName(), player.getStrategyName()}
        );
        GAMEPLAY_LOGGER.log(Level.INFO,
                "joue son tour, en tant que {} ({}-{}).",
                new Object[]{player.getRole().name(), player.getRole().ordinal(), player.getRole().getColor().name()}
        );
        GAMEPLAY_LOGGER.log(Level.INFO,
                "Il possede actuellement {} pieces, et ces cartes en main:", player.getCash());
        GAMEPLAY_LOGGER.log(Level.INFO, "{}",getDescriptionOfCards(player.getCardsInHand()));
        if(player.hasEmptyCity()) {
            GAMEPLAY_LOGGER.info("Il n'a rien dans sa cité.");
        }
        else {
            GAMEPLAY_LOGGER.info("Sa cité contient: ");
            GAMEPLAY_LOGGER.log(Level.INFO, "{}",getDescriptionOfCards(player.getCity()));
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
