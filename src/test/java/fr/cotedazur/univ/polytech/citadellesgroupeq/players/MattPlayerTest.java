package fr.cotedazur.univ.polytech.citadellesgroupeq.players;

import fr.cotedazur.univ.polytech.citadellesgroupeq.Color;
import fr.cotedazur.univ.polytech.citadellesgroupeq.District;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.strategies.DefaultStrategy;
import fr.cotedazur.univ.polytech.citadellesgroupeq.strategies.MattMoreThan5CitiesStrategy;
import fr.cotedazur.univ.polytech.citadellesgroupeq.strategies.SecurePointsForEndGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

class MattPlayerTest {
    Player mattPlayer;
    Player otherPlayer;
    GameLogicManager game;
    RoundSummary summary;

    @BeforeEach
    void setup() {
        game=new GameLogicManager();
        mattPlayer= Mockito.spy(new MattPlayer(0, game.getCardDeck()));
        mattPlayer.setStrategy(new DefaultStrategy(mattPlayer));//necessary because of mock
        otherPlayer= Mockito.spy(new ThomasPlayer(1, game.getCardDeck()));
        otherPlayer.setStrategy(new DefaultStrategy(otherPlayer));//necessary because of mock

        game.getPlayersList().set(0, mattPlayer);
        game.getPlayersList().set(1, otherPlayer);
        summary=new RoundSummary();
    }

    @Test
    void testUsesSecurePointStrategy() {
        game.finishGame();
        game.makeAllPlayersSelectRole();
        game.playPlayerTurn(mattPlayer);
        assertInstanceOf(SecurePointsForEndGame.class, mattPlayer.getStrategy());
    }

    @Test
    void testMagicienWhenPlayerCloseToWin() {
        doReturn(true).when(otherPlayer).isCloseToWin();
        mattPlayer.setStrategy(new DefaultStrategy(mattPlayer));
        mattPlayer.clearHand();
        game.makeAllPlayersSelectRole();
        mattPlayer.getStrategy().selectAndSetRole(List.of(Role.ROI, Role.ARCHITECTE, Role.MAGICIEN, Role.MARCHAND), game.getPlayersList());

        assertInstanceOf(DefaultStrategy.class, mattPlayer.getStrategy());
        assertEquals(Role.MAGICIEN, mattPlayer.getRole());
        summary=game.playPlayerTurn(mattPlayer);
        assertTrue(summary.hasUsedRolePower());
    }

    @Test
    void testGetChoosenDistrictToBuy_WhenNoCardsToBuy_ReturnsEmpty() {
        mattPlayer.clearHand();
        assertEquals(Optional.empty(), mattPlayer.getChoosenDistrictToBuy());
        assertEquals(0, mattPlayer.getCardsInHand().size());
        District villa = new District("Villa", 4, Color.GREEN,"null");
        District bateau = new District("Bateau",5,Color.GREEN, "null");
        District jetSki = new District("Jet Ski", 3,Color.YELLOW, "null");
        mattPlayer.setCash(20);
        mattPlayer.setRole(Role.MARCHAND);

        mattPlayer.addCardToHand(bateau);
        mattPlayer.addCardToHand(villa);
        mattPlayer.addCardToHand(new District("Jacuzzi", 4, Color.BLUE, "null"));

        // il devrait choisir la ville, car elle est le plus proche de 3 et Green
        assertEquals(Optional.of(villa), mattPlayer.getChoosenDistrictToBuy());
        mattPlayer.removeCardFromHand(villa);

        // il devrait choisir le bateau, car il est Green meme si plus éloigné de la valeur 3
        assertEquals( Optional.of(bateau), mattPlayer.getChoosenDistrictToBuy());
        mattPlayer.removeCardFromHand(bateau);
        mattPlayer.addCardToHand(jetSki);

        // il devrait choisir le jet ski, car comme il n'y a aucunes cartes de la couleur verte dans sa main, il prend le plus proche de trois
        assertEquals(Optional.of(jetSki), mattPlayer.getChoosenDistrictToBuy());
    }

    @Test
    void MattMoreThan5CitiesStrategyTestWhenAlreadyGetColor(){
        List<District> city=new ArrayList<>();
        city.add(new District("Temple",1,"blue", "null"));
        city.add(new District("Eglise",2, "green", "null"));
        city.add(new District("Monastere",1, "red", "null"));
        city.add(new District("Prison",2,"yellow", "null"));
        city.add(new District("Ecole",2,"purple", "null"));

        District villa = new District("Villa", 4, Color.RED,"null");
        District bateau = new District("Bateau",3,Color.PURPLE, "null");
        District jetSki = new District("Jet Ski", 1,Color.YELLOW, "null");

        doReturn(bateau).when(mattPlayer).pickCard(summary);
        mattPlayer.setStrategy(new MattMoreThan5CitiesStrategy(mattPlayer));

        mattPlayer.clearHand();
        mattPlayer.setCash(10);
        mattPlayer.addAllCardsToHand(villa,bateau,jetSki);
        mattPlayer.addAllDistrictsToCity(city);
        mattPlayer.setRole(Role.MARCHAND);

        //comme il a déjà la couleur, il achète le bateau qui est la carte la plus proche de la valeur 3
        summary=game.playPlayerTurn(mattPlayer);

        assertTrue(summary.hasBoughtDistricts());
        assertTrue(summary.getBoughtDistricts().contains(bateau));
    }

}
