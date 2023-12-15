package fr.cotedazur.univ.polytech.citadellesgroupeq;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AlwaysSpendPlayerTest {
    Player botWithCards;
    Player botWithoutCards;
    RoundSummary summary;

    @BeforeEach
    public void setUp(){
        botWithCards = new AlwaysSpendPlayer(0);
        botWithoutCards = new AlwaysSpendPlayer(1,0,new ArrayList<>());
        summary = new RoundSummary();
    }

    @Test
    public void testPlayerTurnWithCardsInHand(){
        assertEquals(2, botWithCards.getCardsInHand().size());
        botWithCards.playPlayerTurn(summary);

        assertTrue(summary.hasPickedCash());
    }

    @Test
    public void testPlayerTurnWithEmptyHand(){
        assertEquals(0,botWithoutCards.getCardsInHand().size());
        botWithoutCards.playPlayerTurn(summary);

        // il n'a plus de carte en main donc il pioche
        assertTrue(summary.hasPickedCards());

        botWithoutCards.playPlayerTurn(summary);

        // il a maintenant une carte en main, son objectif avoir plus d'argent
        assertTrue(summary.hasPickedCash());
    }


}