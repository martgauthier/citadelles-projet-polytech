package fr.cotedazur.univ.polytech.citadellesgroupeq.playerevaluator;

import fr.cotedazur.univ.polytech.citadellesgroupeq.CardDeck;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.*;

import java.lang.reflect.Constructor;
import java.util.*;

/**
 * Classe permettant de faire tourner 1000 parties et d'obtenir les données agrégées pendant ces parties.
 */
public class BestScoreCalculator {

    private BestScoreCalculator() {}//useless, but removes sonar warning


    /**
     *
     * @param playerClasses Liste des joueurs à faire jouer. Nécessite la liste des classes à instancier, et non pas les instances, pour éviter des problèmes de deep copy. (voir issue #53)
     * @return a 2d int array, 1st dimension = player, 2nd dimension = 0: win number, 1: meanScore, 2: tie games number
     */
    public static int[][] getDataFor1000GamesPerPlayer(List<Class<? extends Player>> playerClasses, boolean writeInCsv) {
        //array is 1 index larger, to support tie games
        int[] winPerPlayerIdArray=new int[playerClasses.size()];//initialized at 0 by default
        int[] tiePerPlayerIdArray=new int[playerClasses.size()];

        Map<Player, List<Integer>> scorePerPlayerPerGame=new HashMap<>();

        int[][] returnedData=new int[4][3];

        GameStatsCsv csv = new GameStatsCsv();



        for(int i=0; i < 1000; i++) {
            CardDeck deck=new CardDeck();
            List<Player> players=createPlayerListFromClasses(playerClasses, deck);

            scorePerPlayerPerGame=new HashMap<>();//resets it
            for(Player joueur: players) {
                scorePerPlayerPerGame.put(joueur, new ArrayList<>());
            }


            GameLogicManager game = new GameLogicManager(players, false);
            StatsManager statsManager = new StatsManager(game.getPlayersList());
            game.setCardDeck(deck);

            runFullGame(game, statsManager);



            boolean shouldCountAsTieGame=false;
            int bestScore = Collections.max(game.getScoreOfEnd().values());
            Optional<Player> optionalWinner = game.whoIsTheWinner();

            if(optionalWinner.isPresent()) {
                winPerPlayerIdArray[optionalWinner.get().getId()]++;
                statsManager.setWinForPlayer(optionalWinner.get());
            }
            else {//égalité
                shouldCountAsTieGame=true;
            }

            for(Map.Entry<Player, Integer> score: game.getScoreOfEnd().entrySet()) {
                scorePerPlayerPerGame.get(score.getKey()).add(score.getValue());//ajoute le score du joueur, à la liste des scores du joueur

                if(shouldCountAsTieGame && score.getValue()==bestScore) {//si il y a une égalité, et que le joueur en question fait partie des meilleurs joueurs à égalité
                    tiePerPlayerIdArray[score.getKey().getId()]++;//on augmente son nombre d'égalité
                    statsManager.setTieForPlayer(score.getKey());
                }
            }

            if(writeInCsv) {
                statsManager.writePlayersDetailsStatInCsv(csv, game, i);
                statsManager.updatePlayerStatInCsv(csv, game, i);
            }
        }

        for(Map.Entry<Player, List<Integer>> scoreEntry: scorePerPlayerPerGame.entrySet()) {
            //Calcule la moyenne des scores
            Player joueur= scoreEntry.getKey();
            double averageScore=scoreEntry.getValue().stream().mapToDouble(a -> a).average().orElse(0.0);
            returnedData[joueur.getId()][1]= (int) averageScore;

            //ajoute le nombre de victoires au tableau returnedData
            returnedData[joueur.getId()][0]=winPerPlayerIdArray[joueur.getId()];

            returnedData[joueur.getId()][2]=tiePerPlayerIdArray[joueur.getId()];
        }

        return returnedData;
    }

    public static List<Player> createPlayerListFromClasses(List<Class<? extends Player>> playerClasses, CardDeck deck) {
        int ids=0;
        List<Player> playerList=new ArrayList<>();
        for(Class<? extends Player> playerStrategy: playerClasses) {
            try {
                Constructor<? extends Player> constructor = playerStrategy.getDeclaredConstructor(int.class, CardDeck.class);
                Player newPlayer=constructor.newInstance(ids++, deck);
                playerList.add(newPlayer);
            }
            catch(Exception e) {
                throw new IllegalArgumentException("pas de constructeur prenant un int et un DistrictJSONReader en paramètre trouvé pour la classe " + playerStrategy.getName() + "!");
            }
        }

        return playerList;
    }

    public static void runFullGame(GameLogicManager game, StatsManager statsManager) {
        while (!game.isFinished()) {
            game.makeAllPlayersSelectRole();
            for (Player joueur : game.getPlayerTreeSet()) {
                RoundSummary summary = game.playPlayerTurn(joueur);
                statsManager.addSummary(joueur, summary); // on ajoute le RoundSummary a liste des RoundSummary du joueur
            }
            game.resuscitateAllPlayers();
        }
    }
}
