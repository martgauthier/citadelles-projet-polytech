package fr.cotedazur.univ.polytech.citadellesgroupeq;

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
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PowerMerveilleTest {
    RoundSummary summary;
    GameLogicManager game;

    @BeforeEach
    void setup() {
        summary = new RoundSummary();
        game=new GameLogicManager();
    }

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


    /**
     * Tests for manufacture merveille
     */

    @Test
    void testDefaultPlayerBehavior() {//should want to use manufacture power if cash > 5 and cards in hand < 3
        Player player = new AlwaysSpendPlayer(0);
        player.setCash(1000);
        player.setCardsInHand(new ArrayList<>());
        assertTrue(player.wantsToUseManufacturePower());

        player.setCash(3);
        assertFalse(player.wantsToUseManufacturePower());

        player.setCash(1000);
        player.setCardsInHand(new ArrayList<>(List.of(
                new District("temple", 8, Color.GRAY, "null"),
                new District("temple", 8, Color.GRAY, "null"),
                new District("temple", 8, Color.GRAY, "null")
        )));

        assertFalse(player.wantsToUseManufacturePower());
    }

    @Test
    void testManufactureThrowsIfNotEnoughCash() {
        Player player = Mockito.spy(new AlwaysSpendPlayer(0));

        doReturn(true).when(player).wantsToUseManufacturePower();
        player.setCash(0);
        player.setRole(Role.ARCHITECTE);//arbitrary role
        player.addDistrictToCity(new District("Manufacture", 5, Color.PURPLE, "Manufacture power"));
        assertThrows(IllegalArgumentException.class, () -> player.playTurn(summary, game));
    }

    @Test
    void testPlayerUsesManufacturePower() {
        Player player = Mockito.spy(new AlwaysSpendPlayer(0));
        player.setCash(1000);

        doNothing().when(player).draw2Coins(any());
        doNothing().when(player).buyDistrictsDuringTurn(any());

        player.setCardsInHand(new ArrayList<>(List.of(new District("temple", 8, Color.BLUE, "null"))));
        player.setRole(Role.EVEQUE);//arbitrary role that does nothing
        player.addDistrictToCity(new District("Manufacture", 5, Color.PURPLE, "Manufacture power"));

        player.playTurn(summary, game);//AlwaysSpendPlayer will try to pick coins, and buy districts, in these conditions
        verify(player, times(3)).pickCard(any());

        assertEquals(1000 - 3, player.getCash());
        assertEquals(4, player.getCardsInHand().size());
        assertTrue(summary.hasUsedMerveillePower());
        assertTrue(summary.getUsedMerveilles().contains("Manufacture"));
    }

}