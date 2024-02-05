package fr.cotedazur.univ.polytech.citadellesgroupeq.strategies;

import fr.cotedazur.univ.polytech.citadellesgroupeq.Color;
import fr.cotedazur.univ.polytech.citadellesgroupeq.District;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.MattPlayer;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.RealEstatePlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MattStartGameStrategyTest {
    Player mainPlayer;
    Player otherPlayer;
    GameLogicManager game;
    RoundSummary summary;

    @BeforeEach
    void setup() {
        game=new GameLogicManager();
        mainPlayer=Mockito.spy(new MattPlayer(0, game.getCardDeck()));
        mainPlayer.setStrategy(new MattStartGameStrategy(mainPlayer));
        otherPlayer=Mockito.spy(new RealEstatePlayer(1, game.getCardDeck()));
        game.getPlayersList().set(0, mainPlayer);
        game.getPlayersList().set(1, otherPlayer);
        summary=new RoundSummary();
    }

    @Test
    void testMattStartGameKillsArchitecte() {
        game.makeAllPlayersSelectRole();
        mainPlayer.getStrategy().selectAndSetRole(List.of(Role.ASSASSIN, Role.ROI, Role.EVEQUE), List.of());//to force it

        assertEquals(Role.ASSASSIN, mainPlayer.getRole());

        otherPlayer.setRole(Role.ARCHITECTE);
        assertEquals(Role.ASSASSIN, mainPlayer.getRole());
        mainPlayer.getStrategy().playTurn(summary, game);
        assertTrue(summary.hasUsedPower());
    }

    @Test
    void testChoosesRole() {
        mainPlayer.getStrategy().selectAndSetRole(List.of(Role.ROI, Role.EVEQUE, Role.ASSASSIN, Role.MARCHAND), List.of());
        assertEquals(Role.MARCHAND, mainPlayer.getRole());

        mainPlayer.getStrategy().selectAndSetRole(List.of(Role.ROI, Role.EVEQUE, Role.ARCHITECTE), List.of());
        assertEquals(Role.ARCHITECTE, mainPlayer.getRole());

        mainPlayer.getStrategy().selectAndSetRole(List.of(Role.ROI, Role.EVEQUE, Role.ASSASSIN), List.of());
        assertEquals(Role.ASSASSIN, mainPlayer.getRole());

        mainPlayer.getStrategy().selectAndSetRole(List.of(Role.ROI, Role.EVEQUE), List.of());
        assertEquals(Role.ROI, mainPlayer.getRole());
    }

    @Test
    void testPicksCardOrDrawCoins() {
        mainPlayer.setCash(1);
        mainPlayer.setRole(Role.MARCHAND);
        mainPlayer.setCardsInHand(new ArrayList<>(List.of(new District("temple", 8, Color.BLUE))));

        summary=game.playPlayerTurn(mainPlayer);
        assertTrue(summary.hasPickedCards());

        mainPlayer.setRole(Role.ARCHITECTE);
        mainPlayer.setCash(1000);

        summary=game.playPlayerTurn(mainPlayer);
        assertTrue(summary.hasPickedCash());

        mainPlayer.setRole(Role.ASSASSIN);//ne doit avoir aucune influence
        mainPlayer.setCash(3);
        summary=game.playPlayerTurn(mainPlayer);
        assertTrue(summary.hasPickedCash());

        mainPlayer.setCash(5);

        summary=game.playPlayerTurn(mainPlayer);
        assertTrue(summary.hasPickedCards());
    }

    @Test
    void testStopUsesThisStrategy() {
        assertInstanceOf(MattStartGameStrategy.class, mainPlayer.getStrategy());
        mainPlayer.setCash(5);
        mainPlayer.setCardsInHand(new ArrayList<>(List.of(
                new District("temple", 8, Color.PURPLE),
                new District("temple1", 8, Color.PURPLE),
                new District("temple2", 8, Color.PURPLE),
                new District("temple3", 8, Color.PURPLE)
        )));

        game.makeAllPlayersSelectRole();

        game.playPlayerTurn(mainPlayer);
        assertInstanceOf(DefaultStrategy.class, mainPlayer.getStrategy());
    }
}
