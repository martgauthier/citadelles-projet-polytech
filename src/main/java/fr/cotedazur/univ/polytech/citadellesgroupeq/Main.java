package fr.cotedazur.univ.polytech.citadellesgroupeq;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import fr.cotedazur.univ.polytech.citadellesgroupeq.playerevaluator.BestScoreCalculator;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    @Parameter(names = "--demo", description = "Demo of one game")
    private boolean demo = false;
    @Parameter(names = "--2thousands", description = "Demo of 2x1000 games")
    private boolean twoThousands = false;
    @Parameter(names = "--csv", description = "Simulation for statistics")
    private boolean csv = false;


    private static final List<Class<? extends Player>> playersAgainstThomas=List.of(MattPlayer.class, ThomasPlayer.class, AlwaysSpendPlayer.class, RandomPlayer.class);
    private static final List<Class<? extends Player>> fullThomasPlayerList=List.of(ThomasPlayer.class, ThomasPlayer.class, ThomasPlayer.class, ThomasPlayer.class);
    private static final Logger LOGGER=Logger.getLogger("main");

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
            //TODO: write data in CSV
            LOGGER.info("MATTPLAYER VS THOMASPLAYER ---");

            int[] winPercentages= BestScoreCalculator.getWinPercentagePerPlayer(playersAgainstThomas);

            for(int playerid=0; playerid < playersAgainstThomas.size(); playerid++) {
                LOGGER.log(Level.INFO, "Le joueur d`id {0} et de classe {1} a le pourcentage: {2}", new Object[] {playerid, playersAgainstThomas.get(playerid).getSimpleName(), winPercentages[playerid]});
            }


            LOGGER.info("THOMASPLAYER VS HIMSELF:");
            winPercentages=BestScoreCalculator.getWinPercentagePerPlayer(fullThomasPlayerList);

            for(int playerid=0; playerid < fullThomasPlayerList.size(); playerid++) {
                LOGGER.log(Level.INFO, "Le joueur d`id {0} et de classe {1} a le pourcentage: {2}", new Object[] {playerid, fullThomasPlayerList.get(playerid).getSimpleName(), winPercentages[playerid]});
            }
        }
        else{
            //faire un truc par défault
            System.out.println("Pas d'arguments.");
        }
    }
}