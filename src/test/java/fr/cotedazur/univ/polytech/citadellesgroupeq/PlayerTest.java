package fr.cotedazur.univ.polytech.citadellesgroupeq;

import org.junit.jupiter.api.BeforeEach;
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

    @Test
    void testDealCardsOrCash() throws IOException {
        assertEquals(0, bot.getCash());
        assertEquals(2, bot.getCards().size());
        bot.dealCardsOrCash();
        // Soit le bot a pioché deux cartes soit il a récupéré deux pièces
        assertTrue(bot.getCash() == 2 || bot.getCards().size() == 4);
    }
}