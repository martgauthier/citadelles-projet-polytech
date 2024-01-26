package fr.cotedazur.univ.polytech.citadellesgroupeq.players;

import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.strategies.DefaultStrategy;
import fr.cotedazur.univ.polytech.citadellesgroupeq.strategies.SecurePointsForEndGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

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
        assertTrue(summary.hasUsedPower());
    }
}
