package fr.cotedazur.univ.polytech.citadellesgroupeq;

import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    Player bot;
    CitadelsJSONReader reader;
    @BeforeEach
    void setup() throws ParseException {
        bot = new AlwaysSpendPlayer(0);
        reader = new CitadelsJSONReader();
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
        List<Citadel> cardsToAdd = List.of(reader.getFromIndex(0), reader.getFromIndex(1));
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
    @Test
    void testGetTotalCityPrice(){
        List<Citadel> citadels=new ArrayList<>();
        citadels.add(new Citadel("Temple",9,"gray"));
        citadels.add(new Citadel("Eglise",8, "gray"));
        citadels.add(new Citadel("Monastere",7, "gray"));//arbitrary colors just for price
        bot.addAllCitadelsToCity(citadels);
        assertEquals(24,bot.getTotalCityPrice());
    }
}