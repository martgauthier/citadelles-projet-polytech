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


    private static final List<Class<? extends Player>> playersAgainstThomas=List.of(MattPlayer.class, ThomasPlayer.class, RichardPlayer.class, AlwaysSpendPlayer.class);
    private static final List<Class<? extends Player>> fullThomasPlayerList=List.of(ThomasPlayer.class, ThomasPlayer.class, ThomasPlayer.class, ThomasPlayer.class);
    private static final EasyLogger LOGGER= EasyLogger.getLogger("main");

    public static void main(String... args) {

        Main main=new Main();
        JCommander jCommander = JCommander.newBuilder().addObject(main).build();
        jCommander.parse(args);

        if(main.demo && main.twoThousands) {
            LOGGER.severe("Impossible de lancer le programme avec cette combinaison d'arguments !");
            System.exit(1);
        }

        if (main.twoThousands || main.csv) {
            LOGGER.info("MATTPLAYER VS THOMASPLAYER ---");

            int[][] dataPerPlayer= BestScoreCalculator.getDataFor1000GamesPerPlayer(playersAgainstThomas, main.csv);

            for(int playerid=0; playerid < playersAgainstThomas.size(); playerid++) {
                int tieGames=dataPerPlayer[playerid][2];

                int wonGames=dataPerPlayer[playerid][0];
                int lostGames=1000-wonGames-tieGames;
                int meanScore=dataPerPlayer[playerid][1];

                int tieGamesPercentage=tieGames/10;
                int wonPercentage=wonGames/10;
                int lostGamesPercentage=lostGames/10;

                if(main.twoThousands) {//on n'affiche pas ca si on n'a a appelé le programme qu'avec --csv
                    LOGGER.log(Level.INFO, "Le joueur d`id {} et de classe {} a gagne: {} parties ({}%)", new Object[]{playerid, playersAgainstThomas.get(playerid).getSimpleName(), wonGames, wonPercentage});
                    LOGGER.log(Level.INFO, "Et il a perdu: {} parties ({} %)", new Object[]{lostGames, lostGamesPercentage});
                    LOGGER.log(Level.INFO, "Il a eu : {} ({} %) egalites.", new Object[]{tieGames, tieGamesPercentage});
                    LOGGER.log(Level.INFO, "Son score moyen est: {}\n", meanScore);
                }
            }
            //TODO: if csv, write in gamestat.csv summary

            if(main.twoThousands) {
                dataPerPlayer = BestScoreCalculator.getDataFor1000GamesPerPlayer(fullThomasPlayerList, main.csv);

                LOGGER.info("\n\n");//clear visual space a little

                LOGGER.info("THOMASPLAYER VS HIMSELF:");

                for (int playerid = 0; playerid < fullThomasPlayerList.size(); playerid++) {
                    int tieGames = dataPerPlayer[playerid][2];

                    int wonGames = dataPerPlayer[playerid][0];
                    int lostGames = 1000 - wonGames - tieGames;
                    int meanScore = dataPerPlayer[playerid][1];

                    int tieGamesPercentage = tieGames / 10;
                    int wonPercentage = wonGames / 10;
                    int lostGamesPercentage = lostGames / 10;

                    LOGGER.log(Level.INFO, "Le joueur d`id {} et de classe {} a gagne: {} parties ({}%)", new Object[]{playerid, fullThomasPlayerList.get(playerid).getSimpleName(), wonGames, wonPercentage});
                    LOGGER.log(Level.INFO, "Et il a perdu: {} parties ({} %)", new Object[]{lostGames, lostGamesPercentage});
                    LOGGER.log(Level.INFO, "Il a eu : {} ({} %) egalites.", new Object[]{tieGames, tieGamesPercentage});
                    LOGGER.log(Level.INFO, "Son score moyen est: {}\n", meanScore);
                }
            }
            //TODO: if csv, write in gamestat the summary
        }
        else {// having no args, or only "--demo" arg, do the same thing
            GameOutputManager outputManager = new GameOutputManager(main.csv);

            outputManager.startMainOutputLoop();
            //TODO: if csv, write in gamestat the summary
        }
    }
}