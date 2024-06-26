package fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic;

import fr.cotedazur.univ.polytech.citadellesgroupeq.CardDeck;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoundSummaryTest {

    CardDeck reader = new CardDeck();

    RoundSummary firstBasicRound;
    RoundSummary secondBasicRound;

    @BeforeEach
    void setup() {
        firstBasicRound=new RoundSummary(2, RoundSummary.EMPTY_DISTRICT_LIST, RoundSummary.EMPTY_DISTRICT_LIST, false, 0,false,false);
        secondBasicRound=new RoundSummary(2, RoundSummary.EMPTY_DISTRICT_LIST, RoundSummary.EMPTY_DISTRICT_LIST, false, 0,false,false);
    }

    @Test
    void testEquals() {
        assertEquals(firstBasicRound, secondBasicRound);
    }

    @Test
    void testHasBought() {
        assertTrue(new RoundSummary(2, RoundSummary.EMPTY_DISTRICT_LIST, reader.getDistrictsList(), false, 0,false,false).hasBoughtDistricts());

        assertFalse(new RoundSummary(2, RoundSummary.EMPTY_DISTRICT_LIST, RoundSummary.EMPTY_DISTRICT_LIST, false, 0,false,false).hasBoughtDistricts());
        assertFalse(new RoundSummary(0, reader.getDistrictsList(), RoundSummary.EMPTY_DISTRICT_LIST, false, 0,false,false).hasBoughtDistricts());
    }

    @Test
    void testHasPickedCashOrCards() {
        assertTrue(firstBasicRound.hasPickedCash());
        assertFalse(firstBasicRound.hasPickedCards());

        RoundSummary bothChoices = new RoundSummary(2, reader.getDistrictsList(), RoundSummary.EMPTY_DISTRICT_LIST, false, 0,false,false);
        assertTrue(bothChoices.hasPickedCash());
        assertTrue(bothChoices.hasPickedCards());
    }

    @Test
    void testDrawnCards() {
        assertEquals(RoundSummary.EMPTY_DISTRICT_LIST, firstBasicRound.getDrawnCards());
        firstBasicRound.addDrawnCard(reader.getFromIndex(0));//arbitrary card
        assertTrue(firstBasicRound.hasPickedCards());
        assertEquals(1, firstBasicRound.getDrawnCards().size());
    }

    @Test
    void testDrawnCoins() {
        assertEquals(2, firstBasicRound.getDrawnCoins());
        firstBasicRound.addCoins(2);
        assertEquals(4, firstBasicRound.getDrawnCoins());
        firstBasicRound.setDrawnCoins(0);
        assertEquals(0, firstBasicRound.getDrawnCoins());
        assertFalse(firstBasicRound.hasPickedCash());
    }
    @Test
    void testUsePower(){
        assertFalse(firstBasicRound.hasUsedRolePower());
        firstBasicRound.toggleUsedRolePower();
        assertTrue(firstBasicRound.hasUsedRolePower());
    }
    @Test
    void testAssassinateSummary(){
        assertFalse(firstBasicRound.hasBeenKilled());
        firstBasicRound.setHasBeenKilledDuringTurn();
        assertTrue(firstBasicRound.hasBeenKilled());
    }
}