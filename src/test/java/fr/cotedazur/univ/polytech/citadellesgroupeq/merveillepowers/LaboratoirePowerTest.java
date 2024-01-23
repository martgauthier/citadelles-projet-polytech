package fr.cotedazur.univ.polytech.citadellesgroupeq.merveillepowers;

import fr.cotedazur.univ.polytech.citadellesgroupeq.District;
import fr.cotedazur.univ.polytech.citadellesgroupeq.GameOutputManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.AlwaysSpendPlayer;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.RealEstatePlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LaboratoirePowerTest {
    Player player;
    RoundSummary summary;
    GameLogicManager game;

    @BeforeEach
    void setup() {
        summary=new RoundSummary();
        game=new GameLogicManager();
        player= Mockito.spy(new AlwaysSpendPlayer(0, game.getDistrictsJSONReader()));
        game.getPlayersList().set(0, player);
    }

    @Test
    void applyPowerLaboratoire(){
        GameLogicManager game = new GameLogicManager();
        GameOutputManager gameOutputManager = new GameOutputManager();
        List<District> districts2=new ArrayList<>();
        districts2.add(new District("Temple",1,"blue", "null"));
        districts2.add(new District("Eglise",2, "green", "null"));
        districts2.add(new District("Monastere",1, "red", "null"));
        districts2.add(new District("Prison",2,"yellow", "null"));
        Player player = new RealEstatePlayer(0,0,districts2, game.getDistrictsJSONReader());
        player.setRole(Role.ASSASSIN);
        District labo =new District("Laboratoire",6,"Purple","Laboratoire power");
        player.addDistrictToCity(labo);
        game.playPlayerTurn(player);
        assertEquals(1,player.getCash());

        GameLogicManager game2 = new GameLogicManager();
        List<District> districts=new ArrayList<>();
        districts.add(new District("Temple",1,"blue", "null"));
        districts.add(new District("Eglise",2, "green", "null"));
        Player player2 = new RealEstatePlayer(0,0,districts, game.getDistrictsJSONReader());
        player2.setRole(Role.ASSASSIN);
        District labo2 =new District("Laboratoire",6,"Purple","Laboratoire power");
        player2.addDistrictToCity(labo2);
        game2.playPlayerTurn(player);
        assertEquals(0,player2.getCash());
    }
}
