package fr.cotedazur.univ.polytech.citadellesgroupeq.players;

import fr.cotedazur.univ.polytech.citadellesgroupeq.District;
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
        List<District> districtList = new ArrayList<>();

        districtList.add(new District("Temple", 9, "blue"));
        districtList.add(new District("Eglise", 8, "blue"));
        districtList.add(new District("Monastere", 7, "blue"));
        districtList.add(new District("Cathedrale", 6, "blue"));
        districtList.add(new District("Chateau", 4, "yellow"));
        districtList.add(new District("Palais", 5, "yellow"));
        districtList.add(new District("Port", 4, "green"));
        districtList.add(new District("Hotel de ville", 5, "green"));

        botWithEightCards = new RealEstatePlayer(0,0, districtList);
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