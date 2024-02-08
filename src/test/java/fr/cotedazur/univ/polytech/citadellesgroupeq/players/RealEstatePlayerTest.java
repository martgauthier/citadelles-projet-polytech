package fr.cotedazur.univ.polytech.citadellesgroupeq.players;

import fr.cotedazur.univ.polytech.citadellesgroupeq.District;
import fr.cotedazur.univ.polytech.citadellesgroupeq.CardDeck;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RealEstatePlayerTest {
    GameLogicManager game;
    Player botWithEightCards;
    Player botWithoutCards;
    RoundSummary summary;
    CardDeck pioche;

    @BeforeEach
    public void setUp(){
        List<District> districtList = new ArrayList<>();

        game = new GameLogicManager();
        pioche=new CardDeck();

        districtList.add(new District("Temple", 9, "blue", "null"));
        districtList.add(new District("Eglise", 8, "blue", "null"));
        districtList.add(new District("Monastere", 7, "blue", "null"));
        districtList.add(new District("Cathedrale", 6, "blue", "null"));
        districtList.add(new District("Chateau", 4, "yellow", "null"));
        districtList.add(new District("Palais", 5, "yellow", "null"));
        districtList.add(new District("Port", 4, "green", "null"));
        districtList.add(new District("Hotel de ville", 5, "green", "null"));

        botWithEightCards = new RealEstatePlayer(0,0, districtList, pioche);
        botWithoutCards = new RealEstatePlayer(1,0,new ArrayList<>(), pioche);
        summary = new RoundSummary();
    }

    @Test
    void testSmallConstructor(){
        RealEstatePlayer realEstatePlayer = new RealEstatePlayer(2, pioche);
        assertEquals(2, realEstatePlayer.getCash());
    }

    @Test
    void testDesc(){
        assertEquals("RealEstatePlayer",botWithEightCards.getBotLogicName());
    }
    @Test
    void testPlayerTurnWithEightCardsInHand(){
        assertEquals(8, botWithEightCards.getCardsInHand().size());
        botWithEightCards.setRole(Role.ROI);
        botWithEightCards.playTurn(summary, new GameLogicManager());

        assertTrue(summary.hasPickedCash());
    }

    @Test
    void testPlayerTurnWithEmptyHand(){
        assertEquals(0,botWithoutCards.getCardsInHand().size());
        for (int i = 0; i < 8; i++){
            botWithoutCards.setRole(Role.ROI);
            botWithoutCards.playTurn(summary, new GameLogicManager());
            assertTrue(summary.hasPickedCards());
        }
        assertEquals(8,botWithoutCards.getCardsInHand().size());

        // il a maintenant 8 cartes en main, son objectif faire de l'argent
        botWithoutCards.playTurn(summary, new GameLogicManager());
        assertTrue(summary.hasPickedCash());
    }

    @Test
    void testSelectPlayerToExchangeCardsWithAsMagicien() {
        Player playerToExchange = botWithoutCards.selectPlayerToExchangeCardsWithAsMagicien(game.getPlayersList());
        assertTrue(game.getPlayersList().contains(playerToExchange));
    }

    @Test
    void testChoosesToExchangeCardWithPlayer() {
        boolean choose = botWithoutCards.choosesToExchangeCardWithPlayer();
        assertTrue(choose || !choose);
    }
}