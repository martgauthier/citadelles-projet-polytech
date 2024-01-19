package fr.cotedazur.univ.polytech.citadellesgroupeq.players;

import fr.cotedazur.univ.polytech.citadellesgroupeq.District;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Color;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ColorPlayerTest {
    GameLogicManager game;
    ColorPlayer firstPlayer, secondPlayer;
    District highPriceRedDistrict, lowPriceRedDistrict;
    @BeforeEach
    void setup() {
        firstPlayer=new ColorPlayer(0);
        secondPlayer=new ColorPlayer(0);
        game=new GameLogicManager(List.of(firstPlayer, secondPlayer));

        highPriceRedDistrict =new District("temple", 80, Color.RED, "null");
        lowPriceRedDistrict =new District("temple", 1, Color.RED, "null");

        firstPlayer.clearHand();
        secondPlayer.clearHand();
    }

    @Test
    void testChoosesColorRole() {
        List<Role> availablesRoles=List.of(Role.ASSASSIN, Role.VOLEUR, Role.CONDOTTIERE, Role.MARCHAND);
        assertEquals(2, firstPlayer.selectAndSetRole(availablesRoles, List.of()));//any list
    }

    @Test
    void testChoosesAssassinIfNoColorRole() {
        List<Role> availablesRoles=List.of(Role.ARCHITECTE, Role.VOLEUR, Role.MAGICIEN);
        assertEquals(0, firstPlayer.selectAndSetRole(availablesRoles, List.of()));
    }

    @Test
    void testChoosesColorDistrict() {
        firstPlayer.setRole(Role.CONDOTTIERE);
        firstPlayer.addAllCardsToHand(new District("temple", 8, Color.PURPLE, "null"), new District("temple", 7, Color.PURPLE, "null"), highPriceRedDistrict);
        firstPlayer.addCoins(1000);//to make him rich
        assertTrue(firstPlayer.getChoosenDistrictToBuy().isPresent());
        assertEquals(highPriceRedDistrict.getName(), firstPlayer.getChoosenDistrictToBuy().get().getName());
    }


    @Test
    void testBuysLowestColorDistrict() {
        firstPlayer.setRole(Role.CONDOTTIERE);
        firstPlayer.addAllCardsToHand(highPriceRedDistrict, lowPriceRedDistrict, new District("temple", 1, Color.GRAY, "null"));
        firstPlayer.addCoins(1000);//to make him rich
        assertTrue(firstPlayer.getChoosenDistrictToBuy().isPresent());
        assertEquals(lowPriceRedDistrict, firstPlayer.getChoosenDistrictToBuy().get());
    }

    @Test
    void testBuysOtherLowestCard() {
        District lowPriceGrayDistrict =new District("temple", 2, Color.PURPLE, "null");
        firstPlayer.addAllCardsToHand(highPriceRedDistrict, lowPriceGrayDistrict);
        firstPlayer.addCoins(3);
        assertTrue(firstPlayer.getChoosenDistrictToBuy().isPresent());
        assertEquals(lowPriceGrayDistrict, firstPlayer.getChoosenDistrictToBuy().get());
    }

    @Test
    void testBuysNoCardIfTooExpensive() {
        District highPriceGrayDistrict =new District("temple", 50, Color.GRAY, "null");
        firstPlayer.addAllCardsToHand(highPriceGrayDistrict, highPriceRedDistrict);
        assertFalse(firstPlayer.getChoosenDistrictToBuy().isPresent());
    }
}