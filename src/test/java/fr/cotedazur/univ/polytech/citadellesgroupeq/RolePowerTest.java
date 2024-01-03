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

    @BeforeEach
    void setup() {
        assassinPlayer = new AlwaysSpendPlayer(0);
        otherRolePlayer = new RealEstatePlayer(1);
        game=new GameLogicManager(List.of(assassinPlayer, otherRolePlayer));
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
}
