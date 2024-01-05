package fr.cotedazur.univ.polytech.citadellesgroupeq;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.AlwaysSpendPlayer;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.RandomPlayer;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Random;

class RandomPlayerTest {

    Player randomPlayer;
    Player otherPlayer;
    RoundSummary summary;
    Random trickedRandom;
    GameLogicManager game;

    @BeforeEach
    void setUp() {
        summary = new RoundSummary();
        randomPlayer = new RandomPlayer(0);
        otherPlayer=new AlwaysSpendPlayer(1);
        otherPlayer.setRole(Role.CONDOTTIERE);
        game=new GameLogicManager(List.of(randomPlayer, otherPlayer));
        if(trickedRandom != null) {//limits creation of new mocks
            reset(trickedRandom);
        }
        else {
            trickedRandom = Mockito.mock(Random.class);
        }
    }

    @Test
    void testPlayPlayerTurnRandomChoice1() {
        randomPlayer.setRole(Role.ROI);
        doReturn(1).when(trickedRandom).nextInt(anyInt());
        randomPlayer.setRandomGenerator(trickedRandom);
        randomPlayer.playPlayerTurn(summary, game);
        assertTrue(summary.hasPickedCash());
    }


    @Test
    void testPlayPlayerTurnRandomChoice0() {
        randomPlayer.setRole(Role.ROI);
        when(trickedRandom.nextInt(anyInt())).thenReturn(0);
        randomPlayer.setRandomGenerator(trickedRandom);
        randomPlayer.playPlayerTurn(summary, game);
        assertTrue(summary.hasPickedCards());
    }
}