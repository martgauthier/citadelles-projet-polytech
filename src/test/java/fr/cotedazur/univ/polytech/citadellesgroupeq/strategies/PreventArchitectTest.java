package fr.cotedazur.univ.polytech.citadellesgroupeq.strategies;

import fr.cotedazur.univ.polytech.citadellesgroupeq.Color;
import fr.cotedazur.univ.polytech.citadellesgroupeq.District;
import fr.cotedazur.univ.polytech.citadellesgroupeq.CardDeck;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.AlwaysSpendPlayer;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.RealEstatePlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

class PreventArchitectTest {
    Player player;
    Player otherPlayer;
    GameLogicManager game;
    CardDeck pioche;
    RoundSummary summary;
    @BeforeEach
    void setup() {
        pioche=new CardDeck();
        player= Mockito.spy(new RealEstatePlayer(0, pioche));
        player.setStrategy(new PreventArchitectStrategy(player));
        otherPlayer=Mockito.spy(new AlwaysSpendPlayer(1, pioche));//arbitrary choice of players
        otherPlayer.setStrategy(new DefaultStrategy(otherPlayer));
        game=new GameLogicManager(List.of(player, otherPlayer));
        game.setCardDeck(pioche);
        summary=new RoundSummary();
    }

    @Test
    void testIsPlayerCloseToWin() {
        List<Role> availableRoles=List.of(Role.ARCHITECTE, Role.CONDOTTIERE, Role.EVEQUE);
        doReturn(2).when(player).selectAndSetRole(availableRoles, game.getPlayersList());

        assertEquals(2, player.getStrategy().selectAndSetRole(availableRoles, game.getPlayersList()));//no players are close to win, so player uses default selectRole.


        otherPlayer.setCash(10);
        otherPlayer.addAllCardsToHand(new District("temple", 8, Color.RED), new District("temple2", 8, Color.PURPLE));
        otherPlayer.setCity(List.of(
                new District("temple", 8, Color.GRAY),
                new District("temple", 8, Color.GRAY),
                new District("temple", 8, Color.GRAY),
                new District("temple", 8, Color.GRAY),
                new District("temple", 8, Color.GRAY),
                new District("temple", 8, Color.GRAY)//adds 6 cards to his city
        ));

        int roleSelectedIndex=player.getStrategy().selectAndSetRole(availableRoles, game.getPlayersList());
        assertEquals(1, roleSelectedIndex);//a player is close to win, player uses PreventArchitect strategy and chooses the condottiere.
    }

    @Test
    void testRoleToKillIsArchitect() {
        player.setRole(Role.ASSASSIN);
        assertEquals(Role.ARCHITECTE, player.getStrategy().selectRoleToKillAsAssassin(List.of(Role.ASSASSIN, Role.VOLEUR, Role.ARCHITECTE)));
    }

    @Test
    void testName() {
        assertEquals("[PreventArchitect Strategy]", player.getStrategy().getStrategyName());
    }
}
