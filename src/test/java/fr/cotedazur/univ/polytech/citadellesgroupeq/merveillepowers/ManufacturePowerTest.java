package fr.cotedazur.univ.polytech.citadellesgroupeq.merveillepowers;

import fr.cotedazur.univ.polytech.citadellesgroupeq.Color;
import fr.cotedazur.univ.polytech.citadellesgroupeq.District;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.AlwaysSpendPlayer;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class ManufacturePowerTest {
    RoundSummary summary;
    GameLogicManager game;

    Player player;

    @BeforeEach
    void setup() {
        summary=new RoundSummary();
        game=new GameLogicManager();
        player= Mockito.spy(new AlwaysSpendPlayer(0, game.getCardDeck()));
        game.getPlayersList().set(0, player);
    }

    @Test
    void testDefaultPlayerBehavior() {//should want to use manufacture power if cash > 5 and cards in hand < 3.
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
        doReturn(true).when(player).wantsToUseManufacturePower();
        player.setCash(0);
        player.setRole(Role.ARCHITECTE);//arbitrary role
        player.addDistrictToCity(new District("Manufacture", 5, Color.PURPLE, "Manufacture power"));
        assertThrows(IllegalArgumentException.class, () -> player.playTurn(summary, game));
    }

    @Test
    void testPlayerUsesManufacturePower() {
        player.setCash(1000);

        doNothing().when(player).draw2Coins(any());
        doNothing().when(player).buyDistrictsDuringTurn(any());

        player.setCardsInHand(new ArrayList<>(List.of(new District("temple", 8, Color.BLUE, "null"))));
        player.setRole(Role.EVEQUE);//arbitrary role that does nothing
        player.addDistrictToCity(new District("Manufacture", 5, Color.PURPLE, "Manufacture power"));

        player.playTurn(summary, game);//AlwaysSpendPlayer will try to pick coins, and buy districts, in these conditions.
        verify(player, times(3)).pickCard(any());

        assertEquals(1000 - 3, player.getCash());
        assertEquals(4, player.getCardsInHand().size());
        assertTrue(summary.hasUsedMerveillePower());
        assertTrue(summary.getUsedMerveilles().contains("Manufacture"));
    }
}
