package fr.cotedazur.univ.polytech.citadellesgroupeq;

import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.*;
import fr.cotedazur.univ.polytech.citadellesgroupeq.strategies.DefaultStrategy;
import fr.cotedazur.univ.polytech.citadellesgroupeq.strategies.IStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RolePowerTest {
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

    @BeforeEach
    void setup() {
        assassinPlayer = new AlwaysSpendPlayer(0);
        voleurPlayer = new AlwaysSpendPlayer(1);
        otherRolePlayer = new RealEstatePlayer(2);
        magicienPlayer=new ColorPlayer(3);
        condottierePlayer=new RandomPlayer(4);
        evequePlayer=new ColorPlayer(5);
        architectePlayer=new RealEstatePlayer(6);

        assassinPlayer.setRole(Role.ASSASSIN);
        voleurPlayer.setRole(Role.VOLEUR);
        magicienPlayer.setRole(Role.MAGICIEN);
        condottierePlayer.setRole(Role.CONDOTTIERE);
        evequePlayer.setRole(Role.EVEQUE);
        architectePlayer.setRole(Role.ARCHITECTE);

        basicDistrict=new District("temple", 5, Color.PURPLE);
        game=new GameLogicManager(List.of(assassinPlayer, voleurPlayer,otherRolePlayer, magicienPlayer, condottierePlayer, evequePlayer, architectePlayer));
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
        assassinPlayer.setStrategy(new DefaultStrategy(assassinPlayer));

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
        assassinPlayer.setStrategy(new DefaultStrategy(assassinPlayer));

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
        magicienPlayer.setStrategy(new DefaultStrategy(magicienPlayer));
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
        voleurPlayer.setStrategy(new DefaultStrategy(voleurPlayer));
        voleurPlayer.setRole(Mockito.spy(Role.VOLEUR));

        game.getPlayersList().set(voleurPlayer.getId(), voleurPlayer);

        otherRolePlayer.setCash(5); // Le joueur à voler est à 5 pièces
        voleurPlayer.setCash(0);
        assertEquals(5, otherRolePlayer.getCash());
        otherRolePlayer.setRole(Role.MARCHAND);
        assassinPlayer.setRole(Role.ASSASSIN);

        doReturn(Optional.of(otherRolePlayer.getRole())).when(voleurPlayer).selectRoleToSteal(anyList(),anyList());

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

        evequePlayer.dieForThisTurn();
        condottierePlayer.dieForThisTurn();
        architectePlayer.dieForThisTurn();

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


    /**
     * CONDOTTIERE POWER TESTS
     */
    void initSpyCondottiere() {
        condottierePlayer.setRole(Role.CONDOTTIERE);
        condottierePlayer=Mockito.spy(condottierePlayer);
        condottierePlayer.setStrategy(new DefaultStrategy(condottierePlayer));

        doReturn(Optional.empty()).when(condottierePlayer).getChoosenDistrictToBuy();
    }

    Optional<AbstractMap.SimpleEntry<Integer, District>> createOptionalEntry(int id, District district) {
        return Optional.of(new AbstractMap.SimpleEntry<>(id, district));
    }

    @Test
    void testCondottierePowerThrows() {
        initSpyCondottiere();
        doReturn(createOptionalEntry(-1, basicDistrict)).when(condottierePlayer).selectDistrictToDestroyAsCondottiere(anyList());
        assertThrows(IllegalArgumentException.class, () -> Role.CONDOTTIERE.power(game, condottierePlayer, summary));//negative index

        doReturn(createOptionalEntry(game.getPlayersList().size(), basicDistrict)).when(condottierePlayer).selectDistrictToDestroyAsCondottiere(anyList());
        assertThrows(IllegalArgumentException.class, () -> Role.CONDOTTIERE.power(game, condottierePlayer, summary));//too big index

        basicDistrict.setCost(condottierePlayer.getCash()+2);//make it just too much expensive
        game.getPlayersList().get(1).addDistrictToCity(basicDistrict);
        doReturn(createOptionalEntry(1, basicDistrict)).when(condottierePlayer).selectDistrictToDestroyAsCondottiere(anyList());
        assertThrows(IllegalArgumentException.class, () -> Role.CONDOTTIERE.power(game, condottierePlayer, summary));//too expensive

        basicDistrict.setCost(1);//make it buyable
        doReturn(createOptionalEntry(0, basicDistrict)).when(condottierePlayer).selectDistrictToDestroyAsCondottiere(anyList());
        assertThrows(IllegalArgumentException.class, () -> Role.CONDOTTIERE.power(game, condottierePlayer, summary));//not present in player city

        doReturn(createOptionalEntry(1, basicDistrict)).when(condottierePlayer).selectDistrictToDestroyAsCondottiere(anyList());
        Role.CONDOTTIERE.power(game, condottierePlayer, summary);//asserts it doesn't throw anything, as it has good price and is present in player city

        doReturn(Optional.empty()).when(condottierePlayer).selectDistrictToDestroyAsCondottiere(anyList());
        Role.CONDOTTIERE.power(game, condottierePlayer, summary);//asserts it can receive empty optional
    }

    @Test
    void testDistrictRemovedFromCityAndCoinsRemovedFromCondottiere() {
        initSpyCondottiere();

        basicDistrict.setCost(2);
        condottierePlayer.setCash(8);//make him able to destroy it
        game.getPlayersList().get(0).addDistrictToCity(basicDistrict);

        doReturn(createOptionalEntry(0, basicDistrict)).when(condottierePlayer).selectDistrictToDestroyAsCondottiere(anyList());

        assertTrue(game.getPlayersList().get(0).getCity().contains(basicDistrict));

        condottierePlayer.playPlayerTurn(summary, game);

        verify(condottierePlayer).removeCoins(1);

        assertFalse(game.getPlayersList().get(0).getCity().contains(basicDistrict));
    }

    @Test
    void testNoDestroyWhenEmptyChoice() {
        initSpyCondottiere();
        doReturn(Optional.empty()).when(condottierePlayer).selectDistrictToDestroyAsCondottiere(anyList());
        condottierePlayer.playPlayerTurn(summary, game);
        verify(condottierePlayer, never()).removeCoins(anyInt());
    }

    @Test
    void testCondottiereSummaryIsWritten() {
        initSpyCondottiere();

        basicDistrict.setCost(2);
        condottierePlayer.setCash(8);//make him able to destroy it
        game.getPlayersList().get(0).addDistrictToCity(basicDistrict);

        doReturn(createOptionalEntry(0, basicDistrict)).when(condottierePlayer).selectDistrictToDestroyAsCondottiere(anyList());

        condottierePlayer.playPlayerTurn(summary, game);

        assertTrue(summary.getOptionalDestroyedDistrict().isPresent());
        assertEquals(0, summary.getOptionalDestroyedDistrict().get().getKey());
        assertEquals(basicDistrict, summary.getOptionalDestroyedDistrict().get().getValue());
        assertTrue(summary.hasUsedPower());
    }

    @Test
    void testCondottiereSummaryIsNotWritten() {
        initSpyCondottiere();
        doReturn(Optional.empty()).when(condottierePlayer).selectDistrictToDestroyAsCondottiere(anyList());
        condottierePlayer.playPlayerTurn(summary, game);

        assertFalse(summary.hasUsedPower());
        assertTrue(summary.getOptionalDestroyedDistrict().isEmpty());
    }

    @Test
    void testMasterOfTheGameWithPowerKing(){
        otherRolePlayer= Mockito.spy(otherRolePlayer);
        otherRolePlayer.setRole(Mockito.spy(Role.ROI));
        otherRolePlayer.playPlayerTurn(summary,game);
        assertEquals(otherRolePlayer.getId(),game.getMasterOfTheGameIndex());
    }


    /**
     * EVEQUE POWER TESTS
     */

    @Test
    void testTryToDestroyEvequeDistrictThrowsIllegalArg () {
        initSpyCondottiere();
        condottierePlayer.setCash(1000);
        evequePlayer.addDistrictToCity(basicDistrict);
        doReturn(createOptionalEntry(evequePlayer.getId(), basicDistrict)).when(condottierePlayer).selectDistrictToDestroyAsCondottiere(anyList());

        assertThrows(IllegalArgumentException.class, () -> condottierePlayer.playPlayerTurn(summary, game));
    }

    @Test
    void testCanDestroyDeadEvequeDistrict() {
        initSpyCondottiere();
        evequePlayer.addDistrictToCity(basicDistrict);
        evequePlayer.dieForThisTurn();
        condottierePlayer.setCash(1000);
        doReturn(createOptionalEntry(evequePlayer.getId(), basicDistrict)).when(condottierePlayer).selectDistrictToDestroyAsCondottiere(anyList());

        condottierePlayer.playPlayerTurn(summary, game);//assert it doesn't throw
    }

    /**
     * ARCHITECTE Test
     */
    void initSpyArchitecte() {
        architectePlayer=Mockito.spy(architectePlayer);
        architectePlayer.setStrategy(new DefaultStrategy(architectePlayer));
    }

    @Test
    void testArchitecteAlwaysPicks3Cards() {
        initSpyArchitecte();
        architectePlayer.playPlayerTurn(summary, game);
        verify(architectePlayer, times(3)).pickCard(any());//il a pioché 2 fois grâce au rôle + 1 FOIS CAR IL EST REALESTATEPLAYER
    }

    @Test
    void testArchitecteCanBuy3Districts() {
        initSpyArchitecte();
        architectePlayer.setCash(11000);//make him rich

        List<District> architecteHand = new ArrayList<>(List.of(
                new District("temple", 8, Color.GRAY),
                new District("temple", 8, Color.GRAY),
                new District("temple", 8, Color.GRAY),
                new District("temple", 8, Color.GRAY),
                new District("temple", 8, Color.GRAY),
                new District("temple", 8, Color.GRAY),
                new District("temple", 8, Color.GRAY),
                new District("temple", 8, Color.GRAY)));

        architectePlayer.setCardsInHand(architecteHand); // il a 8 cartes en main, et le real estate player veut toujours acheter à 8 cartes

        architectePlayer.playPlayerTurn(summary, game);

        verify(architectePlayer, times(3)).buyDistrictsDuringTurn(summary);
        assertEquals(3, summary.getBoughtDistricts().size());
    }

}