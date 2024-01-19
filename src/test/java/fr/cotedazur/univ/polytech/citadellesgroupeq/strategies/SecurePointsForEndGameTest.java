package fr.cotedazur.univ.polytech.citadellesgroupeq.strategies;

import fr.cotedazur.univ.polytech.citadellesgroupeq.Color;
import fr.cotedazur.univ.polytech.citadellesgroupeq.District;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.AlwaysSpendPlayer;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SecurePointsForEndGameTest {
    Player secureLastPlayer;
    IStrategy secureLastStrat;
    GameLogicManager game;

    @BeforeEach
    void setUp() {
        secureLastPlayer = new AlwaysSpendPlayer(0);
        secureLastStrat = new SecurePointsForEndGame(secureLastPlayer);
        secureLastPlayer.setStrategy(secureLastStrat);
        game = new GameLogicManager(List.of(secureLastPlayer));
        secureLastPlayer.setRole(Role.CONDOTTIERE);
    }

    @Test
    void testGetChoosenDistrictToBuy() {
        secureLastPlayer.setCash(10);
        District district1 = new District("temple", 5, Color.BLUE, "null");
        District district2 = new District("avion", 10, Color.YELLOW, "null");
        District district3 = new District("Donjon", 5, Color.RED, "null");
        District district4 = new District("avion", 10, Color.PURPLE, "null");
        District district5 = new District("temple", 5, Color.GREEN, "null");
        District district6 = new District("temple", 7, Color.BLUE, "null");

        secureLastPlayer.addCardToHand(district5);
        secureLastPlayer.addCardToHand(district6);

        secureLastPlayer.addDistrictToCity(district1);
        secureLastPlayer.addDistrictToCity(district2);
        secureLastPlayer.addDistrictToCity(district3);
        secureLastPlayer.addDistrictToCity(district4);

        assertEquals(secureLastStrat.getChoosenDistrictToBuy(), Optional.of(district5));
    }

}