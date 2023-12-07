package fr.cotedazur.univ.polytech.citadellesgroupeq;

import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class RoundSummaryTest {

    CitadelsJSONReader reader = new CitadelsJSONReader();

    RoundSummary firstBasicRound;
    RoundSummary secondBasicRound;

    RoundSummaryTest() throws ParseException { }

    @BeforeEach
    void setup() {
        firstBasicRound=new RoundSummary(2, RoundSummary.EMPTY_CITADEL_LIST, RoundSummary.EMPTY_CITADEL_LIST);
        secondBasicRound=new RoundSummary(2, RoundSummary.EMPTY_CITADEL_LIST, RoundSummary.EMPTY_CITADEL_LIST);
    }

    @Test
    void testEquals() {
        assertEquals(firstBasicRound, secondBasicRound);
    }

    @Test
    void testHasBought() {
        assertTrue(new RoundSummary(2, RoundSummary.EMPTY_CITADEL_LIST, reader.getCitadelsList()).hasBoughtCitadels());

        assertFalse(new RoundSummary(2, RoundSummary.EMPTY_CITADEL_LIST, RoundSummary.EMPTY_CITADEL_LIST).hasBoughtCitadels());
        assertFalse(new RoundSummary(0, reader.getCitadelsList(), RoundSummary.EMPTY_CITADEL_LIST).hasBoughtCitadels());
    }

    @Test
    void testHasPickedCashOrCards() {
        assertTrue(firstBasicRound.hasPickedCash());
        assertFalse(firstBasicRound.hasPickedCards());

        RoundSummary bothChoices = new RoundSummary(2, reader.getCitadelsList(), RoundSummary.EMPTY_CITADEL_LIST);
        assertTrue(bothChoices.hasPickedCash());
        assertTrue(bothChoices.hasPickedCards());
    }

}