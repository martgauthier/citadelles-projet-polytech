package fr.cotedazur.univ.polytech.citadellesgroupeq.players;

import fr.cotedazur.univ.polytech.citadellesgroupeq.Citadel;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RealEstatePlayerTest {
    Player botWithEightCards;
    Player botWithoutCards;
    RoundSummary summary;

    @BeforeEach
    public void setUp(){
        List<Citadel> citadelList = new ArrayList<>();

        citadelList.add(new Citadel("Temple", 9, "blue"));
        citadelList.add(new Citadel("Eglise", 8, "blue"));
        citadelList.add(new Citadel("Monastere", 7, "blue"));
        citadelList.add(new Citadel("Cathedrale", 6, "blue"));
        citadelList.add(new Citadel("Chateau", 4, "yellow"));
        citadelList.add(new Citadel("Palais", 5, "yellow"));
        citadelList.add(new Citadel("Port", 4, "green"));
        citadelList.add(new Citadel("Hotel de ville", 5, "green"));

        botWithEightCards = new RealEstatePlayer(0,0,citadelList);
        botWithoutCards = new RealEstatePlayer(1,0,new ArrayList<>());
        summary = new RoundSummary();
    }

    @Test
    void testPlayerTurnWithEightCardsInHand(){
        assertEquals(8, botWithEightCards.getCardsInHand().size());
        botWithEightCards.setRole(Role.ROI);
        botWithEightCards.playPlayerTurn(summary, new GameManager());

        assertTrue(summary.hasPickedCash());
    }

    @Test
    void testPlayerTurnWithEmptyHand(){
        assertEquals(0,botWithoutCards.getCardsInHand().size());
        for (int i = 0; i < 8; i++){
            botWithoutCards.setRole(Role.ROI);
            botWithoutCards.playPlayerTurn(summary, new GameManager());
            assertTrue(summary.hasPickedCards());
        }
        assertEquals(8,botWithoutCards.getCardsInHand().size());

        // il a maintenant 8 cartes en main, son objectif faire de l'argent
        botWithoutCards.playPlayerTurn(summary, new GameManager());
        assertTrue(summary.hasPickedCash());
    }
}