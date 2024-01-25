package fr.cotedazur.univ.polytech.citadellesgroupeq.strategies;

import fr.cotedazur.univ.polytech.citadellesgroupeq.Color;
import fr.cotedazur.univ.polytech.citadellesgroupeq.District;
import fr.cotedazur.univ.polytech.citadellesgroupeq.CardDeck;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.AlwaysSpendPlayer;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.ColorPlayer;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doReturn;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class AimForMoneyTest {
    Player mainPlayer, otherPlayer;
    RoundSummary summary;
    GameLogicManager game;
    CardDeck pioche;

    @BeforeEach
    void setup() {
        pioche=new CardDeck();
        mainPlayer=new AlwaysSpendPlayer(0, pioche);
        mainPlayer.setStrategy(new AimForMoneyStrategy(mainPlayer));
        mainPlayer.setRole(Role.ASSASSIN);
        otherPlayer=new ColorPlayer(1, pioche);
        otherPlayer.setRole(Role.CONDOTTIERE);
        game=new GameLogicManager(List.of(mainPlayer, otherPlayer));
        game.setDistrictsJSONReader(pioche);
        summary=new RoundSummary();
    }

    @Test
    void testMainPlayerChoosesVoleur() {
        List<Role> rolesAvailable=List.of(Role.ASSASSIN, Role.VOLEUR, Role.CONDOTTIERE);
        game.setMasterOfTheGameIndex(0);//makes mainPlayer select first

        game.makeAllPlayersSelectRole(rolesAvailable);
        assertEquals(Role.VOLEUR, mainPlayer.getRole());

        rolesAvailable=List.of(Role.CONDOTTIERE, Role.EVEQUE, Role.MAGICIEN);//doesn't contain voleur
        game.setMasterOfTheGameIndex(0);
        game.makeAllPlayersSelectRole(rolesAvailable);
        assertEquals(rolesAvailable.get(0), mainPlayer.getRole());
    }

    @Test
    void testAimForMoneyChoosesCoins() {
        mainPlayer.setCardsInHand(new ArrayList<>());//default AlwaysSpendPlayer will make him pick card, if empty hand

        mainPlayer.getStrategy().playTurn(summary, game);
        assertTrue(summary.hasPickedCash());//because of AimForMoneyStrategy

        summary=new RoundSummary();//reset it

        mainPlayer.setStrategy(new DefaultStrategy(mainPlayer));
        mainPlayer.getStrategy().playTurn(summary, game);
        assertFalse(summary.hasPickedCash());//because of DefaultStrategy
    }

    @Test
    void testAimForMoneyNeverBuys() {
        mainPlayer.addCardToHand(new District("temple", 1, Color.RED, "null"));//really cheap card
        mainPlayer.setCash(3);
        summary=game.playPlayerTurn(mainPlayer);
        assertFalse(summary.hasBoughtDistricts());

        mainPlayer.setStrategy(new DefaultStrategy(mainPlayer));
        summary=game.playPlayerTurn(mainPlayer);
        assertTrue(summary.hasBoughtDistricts());
    }

    @Test
    void testTriesToStealArchitecte() {
        mainPlayer=Mockito.spy(mainPlayer);
        mainPlayer.setStrategy(new AimForMoneyStrategy(mainPlayer));
        mainPlayer.setRole(Role.VOLEUR);

        List<Role> availableRoles=List.of(Role.CONDOTTIERE, Role.EVEQUE, Role.ARCHITECTE);

        doReturn(Optional.of(availableRoles.get(0))).when(mainPlayer).selectRoleToSteal(anyList(), anyList());//si il n'y avait pas de stratégie, renvoyer par défaut le premier role

        assertEquals(Optional.of(Role.ARCHITECTE), mainPlayer.getStrategy().selectRoleToSteal(availableRoles, List.of()));//c'est bien le retour de AimForMoney

        mainPlayer.setStrategy(new DefaultStrategy(mainPlayer));
        assertEquals(Optional.of(availableRoles.get(0)), mainPlayer.getStrategy().selectRoleToSteal(availableRoles, List.of()));//c'est bien le retour par défaut
    }
}
