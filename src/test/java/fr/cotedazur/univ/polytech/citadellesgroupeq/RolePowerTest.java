package fr.cotedazur.univ.polytech.citadellesgroupeq;

import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.AlwaysSpendPlayer;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.ColorPlayer;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.RealEstatePlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RolePowerTest {
    GameLogicManager game;
    RoundSummary summary;
    Player assassinPlayer;
    Player otherRolePlayer;
    Player voleurPlayer;

    Player magicienPlayer;

    @BeforeEach
    void setup() {
        assassinPlayer = new AlwaysSpendPlayer(0);
        voleurPlayer = new AlwaysSpendPlayer(1);
        otherRolePlayer = new RealEstatePlayer(2);
        magicienPlayer=new ColorPlayer(3);
        game=new GameLogicManager(List.of(assassinPlayer, voleurPlayer,otherRolePlayer, magicienPlayer));
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

        game.getPlayersList().set(assassinPlayer.getId(), assassinPlayer);

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

        game.getPlayersList().set(assassinPlayer.getId(), assassinPlayer);

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
        doReturn(new District("temple", 8, Color.GRAY)).when(magicienPlayer).pickCard(summary);//prevents card from being picked

        game.getPlayersList().set(magicienPlayer.getId(), magicienPlayer);
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

    @Test
    void testVoleurStealsGold() throws Exception {
        voleurPlayer = Mockito.spy(new AlwaysSpendPlayer(1));
        voleurPlayer.setRole(Mockito.spy(Role.VOLEUR));

        game.getPlayersList().set(voleurPlayer.getId(), voleurPlayer);

        otherRolePlayer.setCash(5); // Le joueur à voler est à 5 pièces
        voleurPlayer.setCash(0);
        assertEquals(5, otherRolePlayer.getCash());
        otherRolePlayer.setRole(Role.MARCHAND);
        assassinPlayer.setRole(Role.ASSASSIN);

        doReturn(otherRolePlayer.getRole()).when(voleurPlayer).selectRoleToSteal(anyList(),anyList());

        voleurPlayer.playPlayerTurn(summary, game);

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

        assertEquals(5, otherRolePlayer.getCash());
        assertEquals(6, assassinPlayer.getCash());

        voleurPlayer.playPlayerTurn(summary, game);

        assertEquals(voleurPlayer.getCash(), 2 - summary.getBoughtDistricts().stream().mapToInt(District::getCost).sum()); // Le voleur ne devrait pas avoir volé de pièces, il en a seulement pioché 2
        assertEquals(5, otherRolePlayer.getCash()); // Les autres devraient avoir le même nombre de pièces
        assertEquals(6, assassinPlayer.getCash());
        assertFalse(summary.hasUsedPower());
    }

    /**
     * Vérifie que les cartes échangées avec la pile sont différentes, et que les autres sont égales.
     * ATTENTION, une carte pourrait être "égale" (equals), car les cartes sont présentes en plusieurs exemplaires,
     * mais là on vérifie que la REFERENCE de l'objet a changé ou non
     */
    @Test
    void testExchangedCardsFromPileAreDifferent() {
        initSpyMagicien(false);
        List<District> cardsBeforeTurn;

        List<List<Integer>> testedCombinaisons=List.of(
                List.of(),//vide
                List.of(1),//un seul élement, qui n'est pas le premier
                List.of(0),
                List.of(0,1)
        );

        for(List<Integer> combinaison: testedCombinaisons) {
            cardsBeforeTurn=new ArrayList<>(magicienPlayer.getCardsInHand());//shallow copy of list

            int[] combinaisonAsIntArray=combinaison.stream().mapToInt(i -> i).toArray();//convert from list to primitive array
            doReturn(combinaisonAsIntArray).when(magicienPlayer).selectCardsToExchangeWithPileAsMagicien();

            magicienPlayer.playPlayerTurn(summary, game);

            assertTrue(summary.hasExchangedCardsWithPileAsMagician());//car on a appelé initSpy(false)

            List<Integer> cardsChanged=Arrays.stream(summary.getExchangedCardsWithPileIndex()).boxed().toList();//convert from primitive array to list

            assertTrue(cardsChanged.size() <= cardsBeforeTurn.size());
            assertEquals(cardsChanged.size(), combinaison.size());


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
    @Test
    void testCoinsBonusMarchand(){
        otherRolePlayer = Mockito.spy(otherRolePlayer);
        otherRolePlayer.setRole(Role.MARCHAND);
        int coins= otherRolePlayer.getCash();
        otherRolePlayer.playPlayerTurn(summary,game);
        assertEquals(coins,otherRolePlayer.getCash()-1);


    }
}