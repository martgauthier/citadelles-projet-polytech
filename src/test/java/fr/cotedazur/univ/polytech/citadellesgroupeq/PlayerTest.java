package fr.cotedazur.univ.polytech.citadellesgroupeq;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.Arrays;
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
        bot.draw2Coins();
        assertEquals(42, bot.getCash());
        bot.setCash(-1);
        assertEquals(42, bot.getCash());

        bot.add(4);
        assertEquals(46, bot.getCash());
        bot.add(-2);
        assertEquals(46, bot.getCash());
    }

    @Test
    void testAddCards(){
        assertEquals(2, bot.getCards().size());
        List<Citadel> cardsToAdd = List.of(new Citadel("Temple", 9), new Citadel("Eglise", 8));
        bot.addCards(cardsToAdd);
        assertEquals(4, bot.getCards().size());
    }

    @RepeatedTest(100)
    void testDealCardsOrCash() {
        assertEquals(0, bot.getCash());
        assertEquals(2, bot.getCards().size());

        bot.dealCardsOrCash(new RoundSummary());

        // Vérifie que le joueur a soit 2 pièces de plus, soit 1 cartes de plus
        assertTrue(bot.getCash() == 2 || bot.getCards().size() == 3);
    }
}