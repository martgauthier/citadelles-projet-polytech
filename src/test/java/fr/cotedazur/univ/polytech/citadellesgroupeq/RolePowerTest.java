package fr.cotedazur.univ.polytech.citadellesgroupeq;

import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.AlwaysSpendPlayer;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.ColorPlayer;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.RealEstatePlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RolePowerTest {
    GameManager game;
    RoundSummary summary;
    Player assassinPlayer;
    Player otherRolePlayer;

    Player magicienPlayer;

    @BeforeEach
    void setup() {
        assassinPlayer = new AlwaysSpendPlayer(0);
        otherRolePlayer = new RealEstatePlayer(1);
        magicienPlayer=new ColorPlayer(2);
        game=new GameManager(List.of(assassinPlayer, otherRolePlayer, magicienPlayer));
        summary=new RoundSummary();
    }

    @Test
    void testPowerIsCalledOnce() {
        Role mockedRole = mock(Role.class);
        assassinPlayer.setRole(mockedRole);
        assassinPlayer.playPlayerTurn(summary, game);
        verify(mockedRole, times(1)).power(game, assassinPlayer, summary);
    }

    @Test
    void testPlayerIsDead() {
        assassinPlayer= Mockito.spy(new AlwaysSpendPlayer(0));
        assassinPlayer.setRole(Mockito.spy(Role.ASSASSIN));

        game.getPlayersList().set(0, assassinPlayer);

        doReturn(Role.VOLEUR).when(assassinPlayer).selectRoleToKillAsAssassin(anyList());
        otherRolePlayer.setRole(Role.VOLEUR);

        assassinPlayer.playPlayerTurn(summary, game);
        verify(assassinPlayer, times(1)).selectRoleToKillAsAssassin(anyList());
        verify(assassinPlayer.getRole(), times(1)).power(game, assassinPlayer, summary);
        assertTrue(summary.hasUsedPower());
        assertTrue(otherRolePlayer.isDeadForThisTurn());
    }

    @Test
    void testPlayerIsNotKilled() {
        assassinPlayer= Mockito.spy(new AlwaysSpendPlayer(0));
        assassinPlayer.setRole(Mockito.spy(Role.ASSASSIN));

        game.getPlayersList().set(0, assassinPlayer);

        doReturn(Role.VOLEUR).when(assassinPlayer).selectRoleToKillAsAssassin(anyList());
        otherRolePlayer.setRole(Role.CONDOTTIERE);//other role than the one killed

        assassinPlayer.playPlayerTurn(summary, game);
        verify(assassinPlayer, times(1)).selectRoleToKillAsAssassin(anyList());
        verify(assassinPlayer.getRole(), times(1)).power(game, assassinPlayer, summary);
        assertTrue(summary.hasUsedPower());
        assertFalse(otherRolePlayer.isDeadForThisTurn());
    }

    void initSpyMagicien(boolean choosesToExchangeWithPlayer) {
        magicienPlayer = Mockito.spy(magicienPlayer);
        magicienPlayer.setRole(Role.MAGICIEN);
        doReturn(choosesToExchangeWithPlayer).when(magicienPlayer).choosesToExchangeCardWithPlayer();

        game.getPlayersList().set(2, magicienPlayer);
    }

    @Test
    void testHasExchangedCardWithPlayer() {
        initSpyMagicien(true);
        List<District> cardsBeforeExchange=magicienPlayer.getCardsInHand();

        assertEquals(-1, summary.getExchangedCardsPlayerId());//la personne avec qui il a échangé n'est pas encore défini

        magicienPlayer.playPlayerTurn(summary, game);

        assertFalse(summary.hasExchangedCardsWithPileAsMagician());

        int exchangedPlayerId=summary.getExchangedCardsPlayerId();
        assertNotEquals(magicienPlayer.getId(), exchangedPlayerId);//il n'échange pas avec lui-même
        assertNotEquals(-1, exchangedPlayerId);//la personne avec qui il a échangé est bien défini

        assertEquals(game.getPlayersList().get(exchangedPlayerId).getCardsInHand(), cardsBeforeExchange);//il a bien donné ses cartes à l'autre joueur
    }

    @Test
    void testExchangePlayerIdNotDefinedIfNoExchange() {
        initSpyMagicien(false);

        assertEquals(-1, summary.getExchangedCardsPlayerId());
        magicienPlayer.playPlayerTurn(summary, game);

        assertEquals(-1, summary.getExchangedCardsPlayerId());//il n'échange pas avec le joueur, donc pas d'échange défini
        assertTrue(summary.hasExchangedCardsWithPileAsMagician());
    }

    /**
     * Vérifie que les cartes échangées avec la pile sont différentes, et que les autres sont égales.
     * ATTENTION, une carte pourrait être "égale" (equals), car les cartes sont présentes en plusieurs exemplaires,
     * mais là on vérifie que la REFERENCE de l'objet a changé ou non
     */
    @Test
    void testExchangedCardsFromPileAreDifferent() {
        initSpyMagicien(false);
        List<District> cardsBeforeTurn=new ArrayList<>(magicienPlayer.getCardsInHand());//shallow copy of list

        List<List<Integer>> testedCombinaisons=List.of(
                List.of(),//vide
                List.of(1),//un seul élement, qui n'est pas le premier
                List.of(0, 1)//tout
        );

        for(List<Integer> combinaison: testedCombinaisons) {
            doReturn(combinaison.stream().mapToInt(i -> i).toArray()).when(magicienPlayer).selectCardsToExchangeWithPileAsMagicien();
            magicienPlayer.playPlayerTurn(summary, game);

            assertTrue(summary.hasExchangedCardsWithPileAsMagician());

            List<Integer> cardsChanged=Arrays.stream(summary.getExchangedCardsWithPileIndex()).boxed().toList();

            assertTrue(cardsChanged.size() <= cardsBeforeTurn.size());


            //pour chacune des combinaisons plus haut, vérifie que les cartes changées changent et les cartes non-changées restent
            for(int cardIndex=0; cardIndex<cardsBeforeTurn.size(); cardIndex++) {
                if(cardsChanged.contains(cardIndex)) {//assertSame vérifie les REFERENCES objets, contrairement à assertEquals qui vérifie equals()
                    assertNotSame(cardsBeforeTurn.get(cardIndex), magicienPlayer.getCardsInHand().get(cardIndex));//cartes échangées sont différentes
                }
                else {
                    assertSame(cardsBeforeTurn.get(cardIndex), magicienPlayer.getCardsInHand().get(cardIndex));//cartes non-échangées sont les mêmes
                }
            }
        }
    }
}