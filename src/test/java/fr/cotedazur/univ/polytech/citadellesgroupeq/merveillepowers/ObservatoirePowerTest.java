package fr.cotedazur.univ.polytech.citadellesgroupeq.merveillepowers;

import fr.cotedazur.univ.polytech.citadellesgroupeq.Color;
import fr.cotedazur.univ.polytech.citadellesgroupeq.District;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.AlwaysSpendPlayer;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;


class ObservatoirePowerTest {
    RoundSummary summary;
    GameLogicManager game;

    Player player;

    @BeforeEach
    void setup() {
        summary=new RoundSummary();
        game=new GameLogicManager();
        player= Mockito.spy(new AlwaysSpendPlayer(0, game.getDistrictsJSONReader()));
        game.getPlayersList().set(0, player);
    }

    @Test
    void testCardInHandWithObservatory(){
        player.setCardsInHand(new ArrayList<>());
        player.setRole(Role.EVEQUE);//arbitrary role that does nothing
        District obs=new District("Observatoire",8, Color.PURPLE,"Observatoire power");
        player.addDistrictToCity(obs);
        assertEquals(0,player.getCardsInHand().size());
        player.playTurn(summary,game);
        verify(player, times(1)).pickCardForObservatory(any());
        assertEquals(1,player.getCardsInHand().size());
        assertTrue(summary.hasUsedMerveillePower());
        assertTrue(summary.getUsedMerveilles().contains("Observatoire"));

    }
    @Test
    void testCardInHandWithoutObservatory(){
        player.setCardsInHand(new ArrayList<>());
        player.setRole(Role.EVEQUE);//arbitrary role that does nothing
        assertEquals(0,player.getCardsInHand().size());
        player.playTurn(summary,game);
        verify(player, times(0)).pickCardForObservatory(any());
        assertEquals(1,player.getCardsInHand().size());
    }
}
