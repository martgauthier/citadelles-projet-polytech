package fr.cotedazur.univ.polytech.citadellesgroupeq.playerevaluator;

import fr.cotedazur.univ.polytech.citadellesgroupeq.CardDeck;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;

import java.lang.reflect.Constructor;
import java.util.*;

public class BestScoreCalculator {

    private BestScoreCalculator() {
        //useless, but removes sonar warning
    }


    /**
     *
     * @param playerClasses
     * @return a 2d int array, 1st dimension = player, 2nd dimension = 0: win number, 1: meanScore, 2: tie number
     */
    public static int[][] getDataFor1000GamesPerPlayer(List<Class<? extends Player>> playerClasses) {
        //array is 1 index larger, to support tie games
        int[] winPerPlayerIdArray=new int[playerClasses.size()];//initialized at 0 by default
        int[] tiePerPlayerIdArray=new int[playerClasses.size()];

        Map<Player, List<Integer>> scorePerPlayerPerGame=new HashMap<>();

        int[][] returnedData=new int[4][3];

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
            game.setCardDeck(deck);

            while (!game.isFinished()) {
                game.makeAllPlayersSelectRole();
                for (Player joueur : game.getPlayerTreeSet()) {
                    game.playPlayerTurn(joueur);
                }
                game.resuscitateAllPlayers();
            }



            boolean shouldCountAsTieGame=false;
            int bestScore = Collections.max(game.getScoreOfEnd().values());
            Optional<Player> optionalWinner = game.whoIsTheWinner();

            if(optionalWinner.isPresent()) {
                winPerPlayerIdArray[optionalWinner.get().getId()]++;
            }
            else {//égalité
                shouldCountAsTieGame=true;
            }

            for(Map.Entry<Player, Integer> score: game.getScoreOfEnd().entrySet()) {
                scorePerPlayerPerGame.get(score.getKey()).add(score.getValue());//ajoute le score du joueur, à la liste des scores du joueur

                if(shouldCountAsTieGame && score.getValue()==bestScore) {//si il y a une égalité, et que le joueur en question fait partie des meilleurs joueurs à égalité
                    tiePerPlayerIdArray[score.getKey().getId()]++;//on augmente son nombre d'égalité
                }
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
}
