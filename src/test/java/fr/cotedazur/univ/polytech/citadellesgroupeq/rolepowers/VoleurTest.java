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
class VoleurTest {
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
    void testVoleurStealsGold() throws Exception {
        voleurPlayer = Mockito.spy(new AlwaysSpendPlayer(1, pioche));
        voleurPlayer.setStrategy(new DefaultStrategy(voleurPlayer));
        voleurPlayer.setRole(Mockito.spy(Role.VOLEUR));

        game.getPlayersList().set(voleurPlayer.getId(), voleurPlayer);

        otherRolePlayer.setCash(5); // Le joueur à voler est à 5 pièces
        voleurPlayer.setCash(0);
        assertEquals(5, otherRolePlayer.getCash());
        otherRolePlayer.setRole(Role.MARCHAND);
        assassinPlayer.setRole(Role.ASSASSIN);

        doReturn(Optional.of(otherRolePlayer.getRole())).when(voleurPlayer).selectRoleToSteal(anyList(),anyList());

        voleurPlayer.playTurn(summary, game);

        assertEquals(7, summary.getDrawnCoins()); // Le voleur doit avoir volé 5 pièces et tiré 2 pièces
        assertEquals(0, otherRolePlayer.getCash()); // Le joueur volé doit être à sec
        assertTrue(summary.hasUsedPower());
    }
    @Test
    void testVoleurCannotStealFromAssassinOrAssassinated() {
        voleurPlayer.setRole(Role.VOLEUR);

        game.getPlayersList().set(voleurPlayer.getId(), voleurPlayer);

        otherRolePlayer.setRole(Role.MAGICIEN);
        otherRolePlayer.dieForThisTurn();
        voleurPlayer.setCash(0);
        otherRolePlayer.setCash(5);
        assassinPlayer.setCash(6);

        evequePlayer.dieForThisTurn();
        condottierePlayer.dieForThisTurn();
        architectePlayer.dieForThisTurn();

        assertEquals(5, otherRolePlayer.getCash());
        assertEquals(6, assassinPlayer.getCash());

        voleurPlayer.playTurn(summary, game);

        assertEquals(voleurPlayer.getCash(), 2 - summary.getBoughtDistricts().stream().mapToInt(District::getCost).sum()); // Le voleur ne devrait pas avoir volé de pièces, il en a seulement pioché 2
        assertEquals(5, otherRolePlayer.getCash()); // Les autres devraient avoir le même nombre de pièces
        assertEquals(6, assassinPlayer.getCash());
        assertFalse(summary.hasUsedPower());
    }
}
