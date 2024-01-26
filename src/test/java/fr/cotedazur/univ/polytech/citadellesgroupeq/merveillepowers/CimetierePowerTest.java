package fr.cotedazur.univ.polytech.citadellesgroupeq.merveillepowers;

import fr.cotedazur.univ.polytech.citadellesgroupeq.District;
import fr.cotedazur.univ.polytech.citadellesgroupeq.PowerManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.RealEstatePlayer;
import fr.cotedazur.univ.polytech.citadellesgroupeq.strategies.DefaultStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

class CimetierePowerTest {
    Player player;
    RoundSummary summary;
    GameLogicManager game;
    List<District> city;
    PowerManager pm;

    @BeforeEach
    void setup() {
        summary = new RoundSummary();
        game = new GameLogicManager();

        game.getPlayersList().set(0, player);
        city=new ArrayList<>();
        city.add(new District("Temple",1,"blue", "null"));
        city.add(new District("Eglise",2, "green", "null"));
        city.add(new District("Monastere",1, "red", "null"));
        city.add(new District("Prison",2,"yellow", "null"));
        pm = new PowerManager(game);
    }

    @Test
    void dontApplyPowerWhenColorized() {
        player = Mockito.spy(new RealEstatePlayer(0,20,new ArrayList<>(), game.getDistrictsJSONReader()));
        player.setStrategy(new DefaultStrategy(player));
        player.setCity(city);
        player.setRole(Role.MARCHAND);
        doNothing().when(player).draw2Coins(any());
        doReturn(Optional.empty()).when(player).getChoosenDistrictToBuy();
        District cimetiereDistrict = new District("Cimetiere", 5,
                "Purple", "Cimetiere power");
        District districtToDestroy = new District("Caserne",3,"red", "null");
        player.addDistrictToCity(districtToDestroy);
        player.addDistrictToCity(cimetiereDistrict);

        // On détruit la cité et on la retire de celle ci
        summary.setDestroyedDistrict(new AbstractMap.SimpleEntry<>(player.getId(), districtToDestroy));
        player.removeDistrictFromCity(districtToDestroy);

        game.playPlayerTurn(player);//will call PowerManager in player turn

        // on applique pas le pouvoir car le joueur possède déjà la couleur
        assertFalse(player.getCardsInHand().contains(districtToDestroy));
    }
    @Test
    void ApplyPowerWhenNotColorized() {
        player = Mockito.spy(new RealEstatePlayer(0,20,new ArrayList<>(), game.getDistrictsJSONReader()));
        player.setStrategy(new DefaultStrategy(player));
        player.setCity(city);
        player.setRole(Role.MARCHAND);
        doNothing().when(player).draw2Coins(any());
        doReturn(Optional.empty()).when(player).getChoosenDistrictToBuy();
        District cimetiereDistrict = new District("Cimetiere", 5,
                "Purple", "Cimetiere power");
        // On recupere le monastere
        District districtToDestroy = player.getCity().get(2);
        player.addDistrictToCity(cimetiereDistrict);

        // On détruit la cité et on la retire de celle ci
        summary.setDestroyedDistrict(new AbstractMap.SimpleEntry<>(player.getId(), districtToDestroy));
        player.removeDistrictFromCity(districtToDestroy);
        pm.applyCityPowers(player, summary);

        // on applique pas le pouvoir car le joueur possède déjà la couleur
        assertTrue(player.getCardsInHand().contains(districtToDestroy));
    }
}