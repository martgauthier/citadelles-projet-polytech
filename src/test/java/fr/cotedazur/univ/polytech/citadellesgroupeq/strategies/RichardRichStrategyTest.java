package fr.cotedazur.univ.polytech.citadellesgroupeq.strategies;

import com.beust.ah.A;
import fr.cotedazur.univ.polytech.citadellesgroupeq.CardDeck;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.RichardPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RichardRichStrategyTest {
    RichardPlayer player;
    CardDeck deck;
    GameLogicManager game;
    RoundSummary summary;


    @BeforeEach
    void setup() {
        deck=new CardDeck();
        game=new GameLogicManager();
        game.setCardDeck(deck);

        summary=new RoundSummary();

        player= Mockito.spy(new RichardPlayer(0, deck));
        player.setStrategy(new DefaultStrategy(player));//because player has been mocked
        game.getPlayersList().set(0, player);
        game.makeAllPlayersSelectRole();
    }

    @Test
    void testEntersAndLeavesRichStrategy() {
        assertFalse(player.getStrategy() instanceof RichardRichStrategy);
        player.setCash(1000);

        game.playPlayerTurn(player);
        assertTrue(player.getStrategy() instanceof RichardRichStrategy);

        player.setCash(2);
        game.playPlayerTurn(player);
        assertFalse(player.getStrategy() instanceof RichardRichStrategy);
    }

    @Test
    void testDoNotEnterIfGameFinished() {
        game.finishGame();
        player.setCash(1000);
        game.playPlayerTurn(player);
        assertFalse(player.getStrategy() instanceof RichardRichStrategy);
        assertTrue(player.getStrategy() instanceof SecurePointsForEndGame);
    }

    @Test
    void testWillNeverPickCondottiere() {
        List<Role> shortListOfRoles=new ArrayList<>(List.of(Role.ROI, Role.CONDOTTIERE));
        player.setStrategy(new RichardRichStrategy(player));

        player.getStrategy().selectAndSetRole(shortListOfRoles, game.getPlayersList());
        assertEquals(Role.ROI, player.getRole());
    }

    @Test
    void testPrefersArchitect() {
        List<Role> rolesList=new ArrayList<>(List.of(Role.ROI, Role.VOLEUR, Role.ARCHITECTE));
        player.setStrategy(new RichardRichStrategy(player));

        player.getStrategy().selectAndSetRole(rolesList, game.getPlayersList());
        assertEquals(Role.ARCHITECTE, player.getRole());

        rolesList.add(Role.MAGICIEN);//it should still prefer architecte

        player.getStrategy().selectAndSetRole(rolesList, game.getPlayersList());
        assertEquals(Role.ARCHITECTE, player.getRole());
    }

    @Test
    void testPicksMagicienIfNoArchitect() {
        List<Role> rolesList=new ArrayList<>(List.of(Role.ROI, Role.ASSASSIN, Role.MAGICIEN));
        player.setStrategy(new RichardRichStrategy(player));

        player.getStrategy().selectAndSetRole(rolesList, game.getPlayersList());
        assertEquals(Role.MAGICIEN, player.getRole());
    }
}
