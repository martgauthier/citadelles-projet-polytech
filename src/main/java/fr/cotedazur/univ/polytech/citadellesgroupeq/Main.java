package fr.cotedazur.univ.polytech.citadellesgroupeq;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import fr.cotedazur.univ.polytech.citadellesgroupeq.logger.EasyLogger;
import fr.cotedazur.univ.polytech.citadellesgroupeq.playerevaluator.BestScoreCalculator;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.*;

import java.util.List;
import java.util.logging.Level;

public class Main {
    @Parameter(names = "--demo", description = "Demo of one game")
    private boolean demo = false;
    @Parameter(names = "--2thousands", description = "Demo of 2x1000 games")
    private boolean twoThousands = false;
    @Parameter(names = "--csv", description = "Simulation for statistics")
    private boolean csv = false;


    private static final List<Class<? extends Player>> playersAgainstThomas=List.of(MattPlayer.class, ThomasPlayer.class, AlwaysSpendPlayer.class, RandomPlayer.class);
    private static final List<Class<? extends Player>> fullThomasPlayerList=List.of(ThomasPlayer.class, ThomasPlayer.class, ThomasPlayer.class, ThomasPlayer.class);
    private static final EasyLogger LOGGER= EasyLogger.getLogger("main");

    public static void main(String... args) {

        Main main=new Main();
        JCommander jCommander = JCommander.newBuilder().addObject(main).build();
        jCommander.parse(args);
        if(main.csv){
            //faire le truc pour csv
            System.out.println("Argument CSV");
        }
        else if (main.demo) {
            GameOutputManager outputManager = new GameOutputManager();

            outputManager.startMainOutputLoop();
        }
        else if (main.twoThousands) {
            LOGGER.info("MATTPLAYER VS THOMASPLAYER ---");

            int[][] dataPerPlayer= BestScoreCalculator.getDataFor1000GamesPerPlayer(playersAgainstThomas);

            int tieGames=dataPerPlayer[dataPerPlayer.length-1][0];
            int tieGamesPercentage=tieGames/10;
            for(int playerid=0; playerid < playersAgainstThomas.size(); playerid++) {
                int wonGames=dataPerPlayer[playerid][0];
                int wonPercentage=wonGames/10;
                int lostGames=1000-wonGames-tieGames;
                int lostGamesPercentage=lostGames/10;
                int meanScore=dataPerPlayer[playerid][1];

                LOGGER.log(Level.INFO, "Le joueur d`id {} et de classe {} a gagne: {} parties ({}%)", new Object[] {playerid, playersAgainstThomas.get(playerid).getSimpleName(), wonGames, wonPercentage});
                LOGGER.log(Level.INFO, "Et il a perdu: {} parties ({} %)", new Object[]{lostGames, lostGamesPercentage});
                LOGGER.log(Level.INFO, "Son score moyen est: {}", meanScore);
            }
            LOGGER.log(Level.INFO, "Et il y a eu : {} ({} %) egalites.", new Object[] {tieGames, tieGamesPercentage});





            dataPerPlayer= BestScoreCalculator.getDataFor1000GamesPerPlayer(fullThomasPlayerList);
            LOGGER.info("THOMASPLAYER VS HIMSELF:");
            tieGames=dataPerPlayer[dataPerPlayer.length-1][0];
            tieGamesPercentage=tieGames/10;

            for(int playerid=0; playerid < fullThomasPlayerList.size(); playerid++) {
                int wonGames=dataPerPlayer[playerid][0];
                int wonPercentage=wonGames/10;
                int lostGames=1000-wonGames-tieGames;
                int lostGamesPercentage=lostGames/10;
                int meanScore=dataPerPlayer[playerid][1];

                LOGGER.log(Level.INFO, "Le joueur d`id {} et de classe {} a gagne: {} parties ({}%)", new Object[] {playerid, fullThomasPlayerList.get(playerid).getSimpleName(), wonGames, wonPercentage});
                LOGGER.log(Level.INFO, "Et il a perdu: {} parties ({} %)", new Object[]{lostGames, lostGamesPercentage});
                LOGGER.log(Level.INFO, "Son score moyen est: {}", meanScore);
            }
            LOGGER.log(Level.INFO, "Et il y a eu : {} ({} %) egalites.", new Object[] {tieGames, tieGamesPercentage});
        }
        else{
            //faire un truc par défault
            System.out.println("Pas d'arguments.");
        }
    }
}