package fr.cotedazur.univ.polytech.citadellesgroupeq.rolepowers;

import fr.cotedazur.univ.polytech.citadellesgroupeq.Color;
import fr.cotedazur.univ.polytech.citadellesgroupeq.District;
import fr.cotedazur.univ.polytech.citadellesgroupeq.DistrictsJSONReader;
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
class MagicienTest {
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

    DistrictsJSONReader pioche;

    @BeforeEach
    void setup() {
        pioche=new DistrictsJSONReader();
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
        game.setDistrictsJSONReader(pioche);
        summary=new RoundSummary();
    }

    void initSpyMagicien(boolean choosesToExchangeWithPlayer) {
        magicienPlayer = Mockito.spy(magicienPlayer);
        magicienPlayer.setRole(Role.MAGICIEN);
        magicienPlayer.setStrategy(new DefaultStrategy(magicienPlayer));
        doReturn(choosesToExchangeWithPlayer).when(magicienPlayer).choosesToExchangeCardWithPlayer();
        doReturn(new District("temple", 8, Color.GRAY, "null")).when(magicienPlayer).pickCard(summary);//prevents card from being picked

        game.getPlayersList().set(magicienPlayer.getId(), magicienPlayer);
    }

    @Test
    void testHasExchangedCardWithPlayer() {
        initSpyMagicien(true);
        List<District> cardsBeforeExchange=magicienPlayer.getCardsInHand();

        assertEquals(-1, summary.getExchangedCardsPlayerId());//la personne avec qui il a échangé n'est pas encore défini

        magicienPlayer.playTurn(summary, game);

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
        magicienPlayer.playTurn(summary, game);

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

            magicienPlayer.playTurn(summary, game);

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
}
