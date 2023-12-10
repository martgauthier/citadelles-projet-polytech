package fr.cotedazur.univ.polytech.citadellesgroupeq;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    Player bot;
    @BeforeEach
    void setup() {
        bot = new Player(0);
    }

    @Test
    void testCash() {
        assertFalse(bot.stillHasCash());
        bot.setCash(40);
        assertTrue(bot.stillHasCash());
        assertEquals(40, bot.getCash());
        bot.draw2Coins(new RoundSummary());
        assertEquals(42, bot.getCash());
        bot.setCash(-1);
        assertEquals(42, bot.getCash());

        bot.addCoins(4);
        assertEquals(46, bot.getCash());
        bot.addCoins(-2);
        assertEquals(46, bot.getCash());
    }

    @Test
    void testAddCards(){
        assertEquals(2, bot.getCardsInHand().size());
        List<Citadel> cardsToAdd = List.of(new Citadel("Temple", 9), new Citadel("Eglise", 8));
        bot.addAllCardsToHand(cardsToAdd);
        assertEquals(4, bot.getCardsInHand().size());
    }

    @RepeatedTest(100)
    void testDealCardsOrCash() {
        assertEquals(0, bot.getCash());
        assertEquals(2, bot.getCardsInHand().size());

        bot.dealCardsOrCash(new RoundSummary());

        // Vérifie que le joueur a soit 2 pièces de plus, soit 1 cartes de plus
        assertTrue(bot.getCash() == 2 || bot.getCardsInHand().size() == 3);
    }
}