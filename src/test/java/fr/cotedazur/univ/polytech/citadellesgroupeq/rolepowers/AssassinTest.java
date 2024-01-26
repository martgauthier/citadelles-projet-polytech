package fr.cotedazur.univ.polytech.citadellesgroupeq.rolepowers;

import fr.cotedazur.univ.polytech.citadellesgroupeq.Color;
import fr.cotedazur.univ.polytech.citadellesgroupeq.District;
import fr.cotedazur.univ.polytech.citadellesgroupeq.CardDeck;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.*;
import fr.cotedazur.univ.polytech.citadellesgroupeq.strategies.DefaultStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class AssassinTest {
    GameLogicManager game;
    RoundSummary summary;
    Player assassinPlayer;
    Player otherRolePlayer;
    Player voleurPlayer;

    Player magicienPlayer;

    Player condottierePlayer;

    Player evequePlayer;

    Player architectePlayer;

    District basicDistrict;

    CardDeck pioche;

    @BeforeEach
    void setup() {
        pioche=new CardDeck();
        assassinPlayer = new AlwaysSpendPlayer(0, pioche);
        voleurPlayer = new AlwaysSpendPlayer(1, pioche);
        otherRolePlayer = new RealEstatePlayer(2, pioche);
        magicienPlayer=new ColorPlayer(3, pioche);
        condottierePlayer=new RandomPlayer(4, pioche);
        evequePlayer=new ColorPlayer(5, pioche);
        architectePlayer=new RealEstatePlayer(6, pioche);

        assassinPlayer.setRole(Role.ASSASSIN);
        voleurPlayer.setRole(Role.VOLEUR);
        magicienPlayer.setRole(Role.MAGICIEN);
        condottierePlayer.setRole(Role.CONDOTTIERE);
        evequePlayer.setRole(Role.EVEQUE);
        architectePlayer.setRole(Role.ARCHITECTE);

        basicDistrict=new District("temple", 5, Color.PURPLE, "null");
        game=new GameLogicManager(List.of(assassinPlayer, voleurPlayer,otherRolePlayer, magicienPlayer, condottierePlayer, evequePlayer, architectePlayer));
        game.setCardDeck(pioche);
        summary=new RoundSummary();
    }

    @Test
    void testPlayerIsDead() {
        assassinPlayer= Mockito.spy(new AlwaysSpendPlayer(0, pioche));
        assassinPlayer.setRole(Mockito.spy(Role.ASSASSIN));
        assassinPlayer.setStrategy(new DefaultStrategy(assassinPlayer));

        game.getPlayersList().set(assassinPlayer.getId(), assassinPlayer);

        doReturn(Role.VOLEUR).when(assassinPlayer).selectRoleToKillAsAssassin(anyList());
        otherRolePlayer.setRole(Role.VOLEUR);

        assassinPlayer.playTurn(summary, game);
        verify(assassinPlayer, times(1)).selectRoleToKillAsAssassin(anyList());
        verify(assassinPlayer.getRole(), times(1)).power(game, assassinPlayer, summary);
        assertTrue(summary.hasUsedPower());
        assertTrue(otherRolePlayer.isDeadForThisTurn());
    }

    @Test
    void testPlayerIsNotKilled() {
        assassinPlayer= Mockito.spy(new AlwaysSpendPlayer(0, pioche));
        assassinPlayer.setRole(Mockito.spy(Role.ASSASSIN));
        assassinPlayer.setStrategy(new DefaultStrategy(assassinPlayer));

        game.getPlayersList().set(assassinPlayer.getId(), assassinPlayer);

        doReturn(Role.VOLEUR).when(assassinPlayer).selectRoleToKillAsAssassin(anyList());
        otherRolePlayer.setRole(Role.CONDOTTIERE);//other role than the one killed

        assassinPlayer.playTurn(summary, game);
        verify(assassinPlayer, times(1)).selectRoleToKillAsAssassin(anyList());
        verify(assassinPlayer.getRole(), times(1)).power(game, assassinPlayer, summary);
        assertTrue(summary.hasUsedPower());
        assertFalse(otherRolePlayer.isDeadForThisTurn());
    }
}
