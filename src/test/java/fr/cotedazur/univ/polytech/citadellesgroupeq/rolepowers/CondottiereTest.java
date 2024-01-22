package fr.cotedazur.univ.polytech.citadellesgroupeq.rolepowers;

import fr.cotedazur.univ.polytech.citadellesgroupeq.Color;
import fr.cotedazur.univ.polytech.citadellesgroupeq.District;
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
class CondottiereTest {
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

        basicDistrict=new District("temple", 5, Color.PURPLE, "null");
        game=new GameLogicManager(List.of(assassinPlayer, voleurPlayer,otherRolePlayer, magicienPlayer, condottierePlayer, evequePlayer, architectePlayer));
        summary=new RoundSummary();
    }
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

        condottierePlayer.playTurn(summary, game);

        verify(condottierePlayer).removeCoins(1);

        assertFalse(game.getPlayersList().get(0).getCity().contains(basicDistrict));
    }

    @Test
    void testNoDestroyWhenEmptyChoice() {
        initSpyCondottiere();
        doReturn(Optional.empty()).when(condottierePlayer).selectDistrictToDestroyAsCondottiere(anyList());
        condottierePlayer.playTurn(summary, game);
        verify(condottierePlayer, never()).removeCoins(anyInt());
    }

    @Test
    void testCondottiereSummaryIsWritten() {
        initSpyCondottiere();

        basicDistrict.setCost(2);
        condottierePlayer.setCash(8);//make him able to destroy it
        game.getPlayersList().get(0).addDistrictToCity(basicDistrict);

        doReturn(createOptionalEntry(0, basicDistrict)).when(condottierePlayer).selectDistrictToDestroyAsCondottiere(anyList());

        condottierePlayer.playTurn(summary, game);

        assertTrue(summary.getOptionalDestroyedDistrict().isPresent());
        assertEquals(0, summary.getOptionalDestroyedDistrict().get().getKey());
        assertEquals(basicDistrict, summary.getOptionalDestroyedDistrict().get().getValue());
        assertTrue(summary.hasUsedPower());
    }

    @Test
    void testCondottiereSummaryIsNotWritten() {
        initSpyCondottiere();
        doReturn(Optional.empty()).when(condottierePlayer).selectDistrictToDestroyAsCondottiere(anyList());
        condottierePlayer.playTurn(summary, game);

        assertFalse(summary.hasUsedPower());
        assertTrue(summary.getOptionalDestroyedDistrict().isEmpty());
    }
    @Test
    void testCantDestroyDonjonMerveille() {
        initSpyCondottiere();

        District donjon = new District("donjon", 1, Color.PURPLE, "null");
        condottierePlayer.setCash(8);//make him able to destroy it
        game.getPlayersList().get(0).addDistrictToCity(donjon);

        doReturn(createOptionalEntry(0, donjon)).when(condottierePlayer).selectDistrictToDestroyAsCondottiere(anyList());
        assertThrows(IllegalArgumentException.class, () -> condottierePlayer.playTurn(summary, game));
    }

    @Test
    void testCondottiereCantKillAtLastRound() {
        initSpyCondottiere();

        basicDistrict.setCost(1);
        condottierePlayer.setCash(1000);

        game.getPlayersList().get(0).addDistrictToCity(basicDistrict);

        doReturn(createOptionalEntry(0, basicDistrict)).when(condottierePlayer).selectDistrictToDestroyAsCondottiere(anyList());

        game.finishGame();
        game.playPlayerTurn(condottierePlayer);

        assertTrue(game.getPlayersList().get(0).getCity().contains(basicDistrict));//would be false if game wasn't finished
    }
}
