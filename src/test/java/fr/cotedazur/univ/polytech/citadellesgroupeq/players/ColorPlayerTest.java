package fr.cotedazur.univ.polytech.citadellesgroupeq.players;

import fr.cotedazur.univ.polytech.citadellesgroupeq.Citadel;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Color;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ColorPlayerTest {
    GameManager game;
    ColorPlayer firstPlayer, secondPlayer;
    Citadel highPriceColorCitadel, lowPriceColorCitadel;
    @BeforeEach
    void setup() {
        firstPlayer=new ColorPlayer(0);
        secondPlayer=new ColorPlayer(0);
        game=new GameManager(List.of(firstPlayer, secondPlayer));

        highPriceColorCitadel=new Citadel("temple", 80, Color.RED);
        lowPriceColorCitadel=new Citadel("temple", 1, Color.YELLOW);
    }

    @Test
    void testChoosesColorCitadel() {
        firstPlayer.addAllCardsToHand(new Citadel("temple", 8, Color.GRAY), new Citadel("temple", 7, Color.GRAY), highPriceColorCitadel);
        firstPlayer.addCoins(1000);//to make him rich
        assertTrue(firstPlayer.getChoosenCitadelToBuy().isPresent());
        assertEquals(highPriceColorCitadel, firstPlayer.getChoosenCitadelToBuy().get());
    }


    @Test
    void testBuysLowestColorCitadel() {
        firstPlayer.addAllCardsToHand(highPriceColorCitadel, lowPriceColorCitadel, new Citadel("temple", 1, Color.GRAY));
        firstPlayer.addCoins(1000);//to make him rich
        assertTrue(firstPlayer.getChoosenCitadelToBuy().isPresent());
        assertEquals(lowPriceColorCitadel, firstPlayer.getChoosenCitadelToBuy().get());
    }

    @Test
    void testBuysOtherLowestCard() {
        Citadel lowPriceGrayCitadel=new Citadel("temple", 2, Color.GRAY);
        firstPlayer.addAllCardsToHand(highPriceColorCitadel, lowPriceGrayCitadel);
        firstPlayer.addCoins(3);
        assertTrue(firstPlayer.getChoosenCitadelToBuy().isPresent());
        assertEquals(lowPriceGrayCitadel, firstPlayer.getChoosenCitadelToBuy().get());
    }

    @Test
    void testBuysNoCardIfTooExpensive() {
        Citadel highPriceGrayCitadel=new Citadel("temple", 50, Color.GRAY);
        firstPlayer.addAllCardsToHand(highPriceGrayCitadel, highPriceColorCitadel);
        assertFalse(firstPlayer.getChoosenCitadelToBuy().isPresent());
    }
}