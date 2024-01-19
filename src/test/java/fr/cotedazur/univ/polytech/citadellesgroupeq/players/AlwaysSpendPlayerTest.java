package fr.cotedazur.univ.polytech.citadellesgroupeq.players;

import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
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
    void testPlayerTurnWithCardsInHand(){
        assertEquals(2, botWithCards.getCardsInHand().size());
        botWithCards.setRole(Role.ASSASSIN);
        botWithCards.playTurn(summary, new GameLogicManager());

        assertTrue(summary.hasPickedCash());
    }

    @Test
    void testPlayerTurnWithEmptyHand(){
        assertEquals(0,botWithoutCards.getCardsInHand().size());
        botWithoutCards.setRole(Role.ASSASSIN);
        botWithoutCards.playTurn(summary, new GameLogicManager());

        // il n'a plus de carte en main donc il pioche
        assertTrue(summary.hasPickedCards());

        botWithoutCards.playTurn(summary, new GameLogicManager());

        // il a maintenant une carte en main, son objectif avoir plus d'argent
        assertTrue(summary.hasPickedCash());
    }


}