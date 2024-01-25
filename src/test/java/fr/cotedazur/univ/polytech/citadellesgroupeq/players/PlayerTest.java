package fr.cotedazur.univ.polytech.citadellesgroupeq.players;

import fr.cotedazur.univ.polytech.citadellesgroupeq.Color;
import fr.cotedazur.univ.polytech.citadellesgroupeq.District;
import fr.cotedazur.univ.polytech.citadellesgroupeq.CardDeck;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    Player bot;
    CardDeck reader;
    @BeforeEach
    void setup() {
        reader = new CardDeck();
        bot = new AlwaysSpendPlayer(0, reader);
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
        List<District> cardsToAdd = List.of(reader.getFromIndex(0), reader.getFromIndex(1));
        bot.addAllCardsToHand(cardsToAdd);
        assertEquals(4, bot.getCardsInHand().size());
    }

    @Test
    void testDealCardsOrCash() {
        assertEquals(0, bot.getCash());
        assertEquals(2, bot.getCardsInHand().size());

        bot.dealCardsOrCash(new RoundSummary());

        // Vérifie que le joueur a soit 2 pièces de plus, soit 1 cartes de plus
        assertTrue(bot.getCash() == 2 || bot.getCardsInHand().size() == 3);
    }
    @Test
    void testGetTotalCityPrice(){
        List<District> districts =new ArrayList<>();
        districts.add(new District("Temple",9,"gray", "null"));
        districts.add(new District("Eglise",8, "gray", "null"));
        districts.add(new District("Monastere",7, "gray", "null"));//arbitrary colors just for price
        bot.addAllDistrictsToCity(districts);
        assertEquals(24,bot.getTotalCityPrice());
    }
    @Test
    void testAssassinate(){
        bot.dieForThisTurn();
        assertTrue(bot.isDeadForThisTurn());
        bot.rescucitate();
        assertFalse(bot.isDeadForThisTurn());
    }
    @Test
    void testGetColorCombo(){
        List<District> districts =new ArrayList<>();
        districts.add(new District("Temple",9,"blue", "null"));
        districts.add(new District("Eglise",8, "green", "null"));
        districts.add(new District("Monastere",7, "red", "null"));
        districts.add(new District("Prison",9,"yellow", "null"));
        districts.add(new District("Donjon",8, "purple", "null"));//arbitrary colors just for the test
        bot.addAllDistrictsToCity(districts);
        assertTrue(bot.hasAllColorsInCity());
    }

    @Test
    void testIsDistrictInCity(){
        List<District> districts =new ArrayList<>();
        districts.add(new District("Temple",9,"gray", "null"));
        districts.add(new District("Eglise",8, "gray", "null"));
        districts.add(new District("Ecole de Magie",7, "gray", "null"));
        bot.addAllDistrictsToCity(districts);
        assertTrue(bot.isDistrictInCity("Ecole de Magie"));
        assertFalse(bot.isDistrictInCity("Dojon"));
    }

    @Test
    void testCloseToWin() {
        assertFalse(bot.isCloseToWin());//n'est pas proche de la victoire dans les conditions initiales
        bot.setCash(10);
        bot.addAllCardsToHand(new District("temple", 8, Color.RED), new District("temple2", 8, Color.PURPLE));
        bot.setCity(List.of(
                new District("temple", 8, Color.GRAY),
                new District("temple", 8, Color.GRAY),
                new District("temple", 8, Color.GRAY),
                new District("temple", 8, Color.GRAY),
                new District("temple", 8, Color.GRAY),
                new District("temple", 8, Color.GRAY)//adds 6 cards to his city
        ));

        assertTrue(bot.isCloseToWin());
    }

    @SuppressWarnings("java:S5778")
    @Test
    void testBuyingSameDistrictThrowsIllegalArg() {
        District basicDistrict=new District("temple", 8, Color.YELLOW);

        bot.addDistrictToCity(basicDistrict);
        assertThrows(IllegalArgumentException.class, () -> bot.addDistrictToCity(new District(basicDistrict.getName(), basicDistrict.getCost(), basicDistrict.getColor())));
    }
    @Test
    void testHaveObservatoryInCity(){
        District obs= new District("Observatoire",8,Color.PURPLE);
        bot.addDistrictToCity(obs);
        assertTrue(bot.haveObservatoryInCity());
    }
}