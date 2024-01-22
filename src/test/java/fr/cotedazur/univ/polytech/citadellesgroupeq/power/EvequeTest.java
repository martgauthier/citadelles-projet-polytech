package fr.cotedazur.univ.polytech.citadellesgroupeq.power;

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
class EvequeTest {
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
    void testTryToDestroyEvequeDistrictThrowsIllegalArg () {
        initSpyCondottiere();
        condottierePlayer.setCash(1000);
        evequePlayer.addDistrictToCity(basicDistrict);
        doReturn(createOptionalEntry(evequePlayer.getId(), basicDistrict)).when(condottierePlayer).selectDistrictToDestroyAsCondottiere(anyList());

        assertThrows(IllegalArgumentException.class, () -> condottierePlayer.playTurn(summary, game));
    }

    @Test
    @SuppressWarnings("java:S2699")//add assertion to this case
    void testCanDestroyDeadEvequeDistrict() {
        initSpyCondottiere();
        evequePlayer.addDistrictToCity(basicDistrict);
        evequePlayer.dieForThisTurn();
        condottierePlayer.setCash(1000);
        doReturn(createOptionalEntry(evequePlayer.getId(), basicDistrict)).when(condottierePlayer).selectDistrictToDestroyAsCondottiere(anyList());

        condottierePlayer.playTurn(summary, game);//assert it doesn't throw
    }
}
