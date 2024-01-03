package fr.cotedazur.univ.polytech.citadellesgroupeq;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.RandomPlayer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.Random;

class RandomPlayerTest {

    Player randomPlayer;
    RoundSummary summary;
    Random trickedRandom;

    @BeforeEach
    void setUp() {
        summary = new RoundSummary();
        randomPlayer = new RandomPlayer(0);
        trickedRandom = mock(Random.class);
    }

    @Test
    void testPlayPlayerTurnRandomChoice1() {
        randomPlayer.setRole(Role.ASSASSIN);
        when(trickedRandom.nextInt(anyInt())).thenReturn(1);
        randomPlayer.setRandomGenerator(trickedRandom);
        randomPlayer.playPlayerTurn(summary, new GameLogicManager());
        assertTrue(summary.hasPickedCash());
    }


    @Test
    void testPlayPlayerTurnRandomChoice0() {
        randomPlayer.setRole(Role.ASSASSIN);
        when(trickedRandom.nextInt(anyInt())).thenReturn(0);
        randomPlayer.setRandomGenerator(trickedRandom);
        randomPlayer.playPlayerTurn(summary, new GameLogicManager());
        assertTrue(summary.hasPickedCards());
    }
}