package fr.cotedazur.univ.polytech.citadellesgroupeq;

import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.AlwaysSpendPlayer;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.RealEstatePlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RolePowerTest {
    GameLogicManager game;
    RoundSummary summary;
    Player assassinPlayer;
    Player otherRolePlayer;
    Player voleurPlayer;

    @BeforeEach
    void setup() {
        assassinPlayer = new AlwaysSpendPlayer(0);
        voleurPlayer = new AlwaysSpendPlayer(1);
        otherRolePlayer = new RealEstatePlayer(2);
        game=new GameLogicManager(List.of(assassinPlayer, voleurPlayer,otherRolePlayer));
        summary=new RoundSummary();
    }

    @Test
    void testPowerIsCalledOnce() {
        Role mockedRole = mock(Role.class);
        assassinPlayer.setRole(mockedRole);
        assassinPlayer.playPlayerTurn(summary, game);
        verify(mockedRole, times(1)).power(game, assassinPlayer, summary);
    }

    @Test
    void testPlayerIsDead() {
        assassinPlayer= Mockito.spy(new AlwaysSpendPlayer(0));
        assassinPlayer.setRole(Mockito.spy(Role.ASSASSIN));

        game.getPlayersList().set(0, assassinPlayer);

        doReturn(Role.VOLEUR).when(assassinPlayer).selectRoleToKillAsAssassin(anyList());
        otherRolePlayer.setRole(Role.VOLEUR);

        assassinPlayer.playPlayerTurn(summary, game);
        verify(assassinPlayer, times(1)).selectRoleToKillAsAssassin(anyList());
        verify(assassinPlayer.getRole(), times(1)).power(game, assassinPlayer, summary);
        assertTrue(summary.hasUsedPower());
        assertTrue(otherRolePlayer.isDeadForThisTurn());
    }

    @Test
    void testPlayerIsNotKilled() {
        assassinPlayer= Mockito.spy(new AlwaysSpendPlayer(0));
        assassinPlayer.setRole(Mockito.spy(Role.ASSASSIN));

        game.getPlayersList().set(0, assassinPlayer);

        doReturn(Role.VOLEUR).when(assassinPlayer).selectRoleToKillAsAssassin(anyList());
        otherRolePlayer.setRole(Role.CONDOTTIERE);//other role than the one killed

        assassinPlayer.playPlayerTurn(summary, game);
        verify(assassinPlayer, times(1)).selectRoleToKillAsAssassin(anyList());
        verify(assassinPlayer.getRole(), times(1)).power(game, assassinPlayer, summary);
        assertTrue(summary.hasUsedPower());
        assertFalse(otherRolePlayer.isDeadForThisTurn());
    }

    @Test
    void testVoleurStealsGold() throws Exception {
        voleurPlayer = Mockito.spy(new AlwaysSpendPlayer(1));
        voleurPlayer.setRole(Mockito.spy(Role.VOLEUR));

        game.getPlayersList().set(1, voleurPlayer);

        otherRolePlayer.setCash(5); // Le joueur à voler est à 5 pièces
        voleurPlayer.setCash(0);
        assertEquals(5, otherRolePlayer.getCash());
        otherRolePlayer.setRole(Role.MARCHAND);
        assassinPlayer.setRole(Role.ASSASSIN);

        doReturn(otherRolePlayer.getRole()).when(voleurPlayer).selectRoleToSteal(anyList(),anyList());

        voleurPlayer.playPlayerTurn(summary, game);

        assertEquals(7, summary.getDrawnCoins()); // Le voleur doit avoir volé 5 pièces et tiré 2 pièces
        assertEquals(0, otherRolePlayer.getCash()); // Le joueur volé doit être à sec
        assertTrue(summary.hasUsedPower());
    }
    @Test
    void testVoleurCannotStealFromAssassinOrAssassinated() {
        voleurPlayer = Mockito.spy(new AlwaysSpendPlayer(1));
        voleurPlayer.setRole(Mockito.spy(Role.VOLEUR));

        game.getPlayersList().set(1, voleurPlayer);

        otherRolePlayer.setRole(Role.MAGICIEN);
        otherRolePlayer.dieForThisTurn();
        voleurPlayer.setCash(0);
        otherRolePlayer.setCash(5);
        assassinPlayer.setCash(6);

        assertEquals(5, otherRolePlayer.getCash());
        assertEquals(6, assassinPlayer.getCash());

        voleurPlayer.playPlayerTurn(summary, game);

        assertEquals(2, voleurPlayer.getCash()); // Le voleur ne devrait pas avoir volé de pièces, il en a seulement pioché 2
        assertEquals(5, otherRolePlayer.getCash()); // Les autres devraient avoir le même nombre de pièces
        assertEquals(6, assassinPlayer.getCash());
        assertFalse(summary.hasUsedPower());
    }

}
