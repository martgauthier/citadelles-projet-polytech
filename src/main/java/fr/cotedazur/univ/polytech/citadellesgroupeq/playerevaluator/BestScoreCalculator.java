package fr.cotedazur.univ.polytech.citadellesgroupeq.playerevaluator;

import fr.cotedazur.univ.polytech.citadellesgroupeq.CardDeck;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.*;

import java.lang.reflect.Constructor;
import java.util.*;

public class BestScoreCalculator {

    private BestScoreCalculator() {
        //useless, but removes sonar warning
    }

    public static void main(String[] args) {
        List<Class<? extends Player>> playersAgainstThomas=List.of(MattPlayer.class, ThomasPlayer.class, AlwaysSpendPlayer.class, RandomPlayer.class);
        getDataFor1000GamesPerPlayer(playersAgainstThomas);
    }


    /**
     *
     * @param playerClasses
     * @return a 2d int array, 1st dimension = player, 2nd dimension = 0: winPercentage, 1: meanScore
     */
    public static int[][] getDataFor1000GamesPerPlayer(List<Class<? extends Player>> playerClasses) {
        //array is 1 index larger, to support tie games
        int[] winPerPlayerIdArray=new int[playerClasses.size()+1];//initialized at 0 by default
        Map<Player, List<Integer>> scorePerPlayerPerGame=new HashMap<>();

        int[][] returnedData=new int[5][2];

        GameStatsCsv csv = new GameStatsCsv();

        for(int i=0; i < 1000; i++) {
            int ids=0;
            CardDeck deck=new CardDeck();
            List<Player> players=new ArrayList<>();
            scorePerPlayerPerGame=new HashMap<>();
            for(Class<? extends Player> playerStrategy: playerClasses) {
                try {
                    Constructor<? extends Player> constructor = playerStrategy.getDeclaredConstructor(int.class, CardDeck.class);
                    Player newPlayer=constructor.newInstance(ids++, deck);
                    players.add(newPlayer);
                    scorePerPlayerPerGame.put(newPlayer, new ArrayList<>());
                }
                catch(Exception e) {
                    throw new IllegalArgumentException("pas de constructeur prenant un int et un DistrictJSONReader en paramètre trouvé pour la classe " + playerStrategy.getName() + "!");
                }
            }

            GameLogicManager game = new GameLogicManager(players);
            StatsManager statsManager = new StatsManager(game.getPlayersList());
            game.setCardDeck(deck);

            while (!game.isFinished()) {
                game.makeAllPlayersSelectRole();
                for (Player joueur : game.getPlayerTreeSet()) {
                    RoundSummary summary = game.playPlayerTurn(joueur);
                    statsManager.addSummary(joueur, summary); // on ajoute le RoundSummary a liste des RoundSummary du joueur
                }
                game.resuscitateAllPlayers();
            }

            for(Map.Entry<Player, Integer> score: game.getScoreOfEnd().entrySet()) {
                scorePerPlayerPerGame.get(score.getKey()).add(score.getValue());//ajoute le score du joueur, à la liste des scores du joueur
            }
            Optional<Player> optionalWinner = game.whoIsTheWinner();
            optionalWinner.ifPresentOrElse(player -> winPerPlayerIdArray[player.getId()]++, () -> winPerPlayerIdArray[winPerPlayerIdArray.length-1]++);//compte la victoire seulement si présent

            statsManager.writePlayersDetailsStatInCsv(csv,game,i);
            statsManager.updatePlayerStatInCsv(csv);
        }


        for(Map.Entry<Player, List<Integer>> scoreEntry: scorePerPlayerPerGame.entrySet()) {
            //Calcule la moyenne des scores
            Player joueur= scoreEntry.getKey();
            double averageScore=scoreEntry.getValue().stream().mapToDouble(a -> a).average().orElse(0.0);
            returnedData[joueur.getId()][1]= (int) averageScore;

            //ajoute le nombre de victoires au tableau returnedData
            returnedData[joueur.getId()][0]=winPerPlayerIdArray[joueur.getId()];
        }

        //pour les égalités
        returnedData[4][0]=winPerPlayerIdArray[4];//car la case qui stocke le nombre d'égalité est à l'index 4

        return returnedData;
    }
}
