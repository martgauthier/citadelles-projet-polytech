package fr.cotedazur.univ.polytech.citadellesgroupeq.players;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import fr.cotedazur.univ.polytech.citadellesgroupeq.DistrictsJSONReader;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.AlwaysSpendPlayer;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.RandomPlayer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import java.util.List;
import java.util.Random;

class RandomPlayerTest {

    Player randomPlayer;
    Player otherPlayer;
    RoundSummary summary;
    Random trickedRandom;
    GameLogicManager game;
    DistrictsJSONReader pioche;

    @BeforeEach
    void setUp() {
        pioche=new DistrictsJSONReader();
        randomPlayer=new RandomPlayer(0, pioche);
        otherPlayer=new RealEstatePlayer(1, pioche);
        game=new GameLogicManager(List.of(randomPlayer, otherPlayer));
        game.setDistrictsJSONReader(pioche);
        summary = new RoundSummary();
        otherPlayer.setRole(Role.CONDOTTIERE);

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
        randomPlayer.playTurn(summary, game);
        assertTrue(summary.hasPickedCash());
    }


    @Test
    void testPlayPlayerTurnRandomChoice0() {
        randomPlayer.setRole(Role.ROI);
        when(trickedRandom.nextInt(anyInt())).thenReturn(0);
        randomPlayer.setRandomGenerator(trickedRandom);
        randomPlayer.playTurn(summary, game);
        assertTrue(summary.hasPickedCards());
    }
}