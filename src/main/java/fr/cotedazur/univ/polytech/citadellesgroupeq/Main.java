package fr.cotedazur.univ.polytech.citadellesgroupeq;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

public class Main {
    @Parameter(names = "--demo", description = "Demo of one game")
    private boolean demo = false;
    @Parameter(names = "--2thousands", description = "Demo of 2x1000 games")
    private boolean twoThousands = false;
    @Parameter(names = "--csv", description = "Simulation for statistics")
    private boolean csv = false;
    public static void main(String... args) {
        Main main=new Main();
        JCommander jCommander = JCommander.newBuilder().addObject(main).build();
        jCommander.parse(args);
        if(main.csv){
            //faire le truc pour csv
            System.out.println("Argument CSV");
        } else if (main.demo) {
            GameOutputManager outputManager = new GameOutputManager();

            outputManager.startMainOutputLoop();
        } else if (main.twoThousands) {
            //faire le truc qui fait 2x1000 parties
            System.out.println("Argument 2000 parties");
        }else{
            //faire un truc par défault
            System.out.println("Pas d'arguments.");
        }
    }
}