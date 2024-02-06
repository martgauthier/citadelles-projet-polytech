package fr.cotedazur.univ.polytech.citadellesgroupeq.strategies;

import fr.cotedazur.univ.polytech.citadellesgroupeq.CardDeck;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Color;
import fr.cotedazur.univ.polytech.citadellesgroupeq.District;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.RichardPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RichardMaliceStrategyTest {
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
    void testEntersAndLeavesMaliceStrategy() {//should enter it only if a player currently has 6 cities
        game.playPlayerTurn(player);
        assertFalse(player.getStrategy() instanceof RichardMaliceStrategy);
        assertTrue(player.getStrategy() instanceof DefaultStrategy);

        Player otherPlayer=game.getPlayersList().get(3);
        otherPlayer.setCity(new ArrayList<>(List.of(
                new District("temple", 8, Color.RED),
                new District("temple", 8, Color.YELLOW),
                new District("temple", 8, Color.BLUE),
                new District("temple", 8, Color.GRAY),
                new District("temple", 8, Color.PURPLE),
                new District("temple", 8, Color.GREEN)
        )));//6 districts in city, should trigger RichardMalice

        game.playPlayerTurn(player);
        assertTrue(player.getStrategy() instanceof RichardMaliceStrategy);

        otherPlayer.removeDistrictFromCity(otherPlayer.getCity().get(0));//removes one

        game.playPlayerTurn(player);//Richard should go back to default
        assertFalse(player.getStrategy() instanceof RichardMaliceStrategy);
    }
}
