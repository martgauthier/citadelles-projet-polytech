package fr.cotedazur.univ.polytech.citadellesgroupeq.strategies;

import fr.cotedazur.univ.polytech.citadellesgroupeq.CardDeck;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Color;
import fr.cotedazur.univ.polytech.citadellesgroupeq.District;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.AlwaysSpendPlayer;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.MattPlayer;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MattMoreThan5CitiesTest {
    @Test
    void testReturnsPlayerChoiceToBuy() {
        CardDeck pioche = new CardDeck();
        Player mainPlayer=new MattPlayer(0, pioche);

        MattMoreThan5CitiesStrategy strat = new MattMoreThan5CitiesStrategy(mainPlayer);

        mainPlayer.setStrategy(strat);

        assertTrue(mainPlayer.getCity().size() < 5);

        assertEquals(strat, mainPlayer.getStrategy());

        strat.getChoosenDistrictToBuy();

        assertNotEquals(strat, mainPlayer.getStrategy());
    }

    @Test
    void testBuysLowestCardNotInCity() {

        CardDeck pioche = new CardDeck();
        Player mainPlayer=new MattPlayer(0, pioche);

        MattMoreThan5CitiesStrategy strat = new MattMoreThan5CitiesStrategy(mainPlayer);

        mainPlayer.setStrategy(strat);


        mainPlayer.setCity(new ArrayList<>(List.of(
                new District("temple", 8, Color.RED),
                new District("temple1", 2, Color.RED),
                new District("temple2", 2, Color.RED),
                new District("temple3", 2, Color.RED),
                new District("temple4", 2, Color.RED)
        )));

        List<District> cardsInHand=new ArrayList<>(List.of(
                new District("temple", 2, Color.BLUE),
                new District("testons", 3, Color.GREEN)
        ));

        mainPlayer.setCardsInHand(cardsInHand);

        Optional<District> choosen=strat.getChoosenDistrictToBuy();

        assertTrue(choosen.isPresent());
        assertEquals(cardsInHand.get(0), choosen.get());
    }
}
