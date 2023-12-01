package fr.cotedazur.univ.polytech.citadellesgroupeq;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
}