package fr.cotedazur.univ.polytech.citadellesgroupeq.players;

import fr.cotedazur.univ.polytech.citadellesgroupeq.District;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Color;
import fr.cotedazur.univ.polytech.citadellesgroupeq.CardDeck;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.strategies.DefaultStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class ColorPlayerTest {
    GameLogicManager game;
    ColorPlayer firstPlayer, secondPlayer;
    District highPriceRedDistrict, lowPriceRedDistrict;
    CardDeck pioche;
    @BeforeEach
    void setup() {
        pioche=new CardDeck();
        firstPlayer= Mockito.spy(new ColorPlayer(0, pioche));
        firstPlayer.setStrategy(new DefaultStrategy(firstPlayer));
        firstPlayer.setRandomGenerator(Mockito.spy(firstPlayer.getRandomGenerator()));

        secondPlayer=Mockito.spy(new ColorPlayer(1, pioche));
        secondPlayer.setStrategy(new DefaultStrategy(secondPlayer));

        game=new GameLogicManager(List.of(firstPlayer, secondPlayer));
        game.setCardDeck(pioche);

        highPriceRedDistrict =new District("temple", 80, Color.RED, "null");
        lowPriceRedDistrict =new District("temple", 1, Color.RED, "null");

        firstPlayer.clearHand();
        secondPlayer.clearHand();

        List<District> cards = new ArrayList<>();
        cards.add(highPriceRedDistrict);

        ColorPlayer colorPlayer = new ColorPlayer(2, 0,cards, pioche);
        assertFalse(colorPlayer.isDeadForThisTurn());
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

    @Test
    void testUsesRandomGeneratorToChooseExchangeCards() {
        Random mockedRandom=firstPlayer.getRandomGenerator();
        doReturn(false).when(mockedRandom).nextBoolean();

        assertFalse(firstPlayer.choosesToExchangeCardWithPlayer());

        doReturn(true).when(mockedRandom).nextBoolean();
        assertTrue(firstPlayer.choosesToExchangeCardWithPlayer());
    }

    @Test
    void testBotLogicName() {
        assertEquals("ColorPlayer", firstPlayer.getBotLogicName());
    }

}