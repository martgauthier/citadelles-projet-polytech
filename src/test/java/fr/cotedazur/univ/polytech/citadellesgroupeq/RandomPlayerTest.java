package fr.cotedazur.univ.polytech.citadellesgroupeq;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;

import java.util.Random;

public class RandomPlayerTest {

    Player randomPlayer;
    RoundSummary summary;
    @Mock
    Random trickedRandom = mock(Random.class);

    @BeforeEach
    public void setUp() {
        summary = new RoundSummary();
        randomPlayer = new RandomPlayer(0);
    }

    @Test
    public void testPlayPlayerTurnRandomChoice1() {
        when(trickedRandom.nextInt(anyInt())).thenReturn(1);
        randomPlayer.playPlayerTurn(summary);
        assertTrue(summary.hasPickedCash());
    }

    @Test
    public void testPlayPlayerTurnRandomChoice0() {
        when(trickedRandom.nextInt(anyInt())).thenReturn(0);
        randomPlayer.playPlayerTurn(summary);
        assertTrue(summary.hasPickedCards());
    }
}