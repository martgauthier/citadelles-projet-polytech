package fr.cotedazur.univ.polytech.citadellesgroupeq;

import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.RealEstatePlayer;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

class PowerMerveilleTest {

    @Test
    void applyPowerEcoleDeMagie() {
        GameLogicManager game = new GameLogicManager();
        GameOutputManager gameOutputManager = new GameOutputManager();
        Player player = new RealEstatePlayer(0);
        player.setRole(Role.MARCHAND);

        District ecoleDeMagieDistrict = new District("Ecole de Magie", 3,
                                "Purple", "Ecole de magie power");

        player.addDistrictToCity(ecoleDeMagieDistrict);


        PowerManager powerManager = new PowerManager(game);
        game.playPlayerTurn(player);//will call PowerManager in player turn


        assertEquals(2,player.getCash());// Une pièce grace au pouvoir du marchand et une grace au pouvoir de l'ecole de magie

        gameOutputManager.describePlayerRound(player, game);
    }

    /** TEST OF DONJON IN CondottiereTest **/
    @Test
    void applyPowerBibliotheque(){
        GameLogicManager game = new GameLogicManager();
        GameOutputManager gameOutputManager = new GameOutputManager();
        Player player = new RealEstatePlayer(0);
        player.setRole(Role.MARCHAND);

        District biblio =new District("Bibliotheque",6,"Purple","Bibliotheque power");
        player.addDistrictToCity(biblio);
        game.playPlayerTurn(player);
        assertEquals(4,player.getCardsInHand().toArray().length);

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
        Player player = new RealEstatePlayer(0,0,districts2);
        player.setRole(Role.ASSASSIN);
        District labo =new District("Laboratoire",6,"Purple","Laboratoire power");
        player.addDistrictToCity(labo);
        game.playPlayerTurn(player);
        assertEquals(1,player.getCash());

        GameLogicManager game2 = new GameLogicManager();
        List<District> districts=new ArrayList<>();
        districts.add(new District("Temple",1,"blue", "null"));
        districts.add(new District("Eglise",2, "green", "null"));
        Player player2 = new RealEstatePlayer(0,0,districts);
        player2.setRole(Role.ASSASSIN);
        District labo2 =new District("Laboratoire",6,"Purple","Laboratoire power");
        player2.addDistrictToCity(labo2);
        game2.playPlayerTurn(player);
        assertEquals(0,player2.getCash());

    }

}