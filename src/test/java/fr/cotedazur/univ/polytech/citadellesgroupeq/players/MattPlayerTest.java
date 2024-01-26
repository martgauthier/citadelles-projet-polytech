package fr.cotedazur.univ.polytech.citadellesgroupeq.players;

import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.strategies.DefaultStrategy;
import fr.cotedazur.univ.polytech.citadellesgroupeq.strategies.SecurePointsForEndGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

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
}
