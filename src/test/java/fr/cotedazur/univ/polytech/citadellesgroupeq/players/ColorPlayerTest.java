package fr.cotedazur.univ.polytech.citadellesgroupeq.players;

import fr.cotedazur.univ.polytech.citadellesgroupeq.Citadel;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Color;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ColorPlayerTest {
    GameLogicManager game;
    ColorPlayer firstPlayer, secondPlayer;
    Citadel highPriceRedCitadel, lowPriceRedCitadel;
    @BeforeEach
    void setup() {
        firstPlayer=new ColorPlayer(0);
        secondPlayer=new ColorPlayer(0);
        game=new GameLogicManager(List.of(firstPlayer, secondPlayer));

        highPriceRedCitadel=new Citadel("temple", 80, Color.RED);
        lowPriceRedCitadel=new Citadel("temple", 1, Color.RED);

        firstPlayer.clearHand();
        secondPlayer.clearHand();
    }

    @Test
    void testChoosesColorRole() {
        List<Role> availablesRoles=List.of(Role.ASSASSIN, Role.VOLEUR, Role.CONDOTTIERE, Role.MARCHAND);
        assertEquals(2, firstPlayer.selectRole(availablesRoles));
    }

    @Test
    void testChoosesAssassinIfNoColorRole() {
        List<Role> availablesRoles=List.of(Role.ARCHITECTE, Role.VOLEUR, Role.MAGICIEN);
        assertEquals(0, firstPlayer.selectRole(availablesRoles));
    }

    @RepeatedTest(50)
    void testChoosesColorCitadel() {
        firstPlayer.setRole(Role.CONDOTTIERE);
        firstPlayer.addAllCardsToHand(new Citadel("temple", 8, Color.PURPLE), new Citadel("temple", 7, Color.PURPLE), highPriceRedCitadel);
        firstPlayer.addCoins(1000);//to make him rich
        assertTrue(firstPlayer.getChoosenCitadelToBuy().isPresent());
        assertEquals(highPriceRedCitadel.getName(), firstPlayer.getChoosenCitadelToBuy().get().getName());
    }


    @Test
    void testBuysLowestColorCitadel() {
        firstPlayer.setRole(Role.CONDOTTIERE);
        firstPlayer.addAllCardsToHand(highPriceRedCitadel, lowPriceRedCitadel, new Citadel("temple", 1, Color.GRAY));
        firstPlayer.addCoins(1000);//to make him rich
        assertTrue(firstPlayer.getChoosenCitadelToBuy().isPresent());
        assertEquals(lowPriceRedCitadel, firstPlayer.getChoosenCitadelToBuy().get());
    }

    @Test
    void testBuysOtherLowestCard() {
        Citadel lowPriceGrayCitadel=new Citadel("temple", 2, Color.PURPLE);
        firstPlayer.addAllCardsToHand(highPriceRedCitadel, lowPriceGrayCitadel);
        firstPlayer.addCoins(3);
        assertTrue(firstPlayer.getChoosenCitadelToBuy().isPresent());
        assertEquals(lowPriceGrayCitadel, firstPlayer.getChoosenCitadelToBuy().get());
    }

    @Test
    void testBuysNoCardIfTooExpensive() {
        Citadel highPriceGrayCitadel=new Citadel("temple", 50, Color.GRAY);
        firstPlayer.addAllCardsToHand(highPriceGrayCitadel, highPriceRedCitadel);
        assertFalse(firstPlayer.getChoosenCitadelToBuy().isPresent());
    }
}