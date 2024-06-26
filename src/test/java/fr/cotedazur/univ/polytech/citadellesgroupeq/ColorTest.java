package fr.cotedazur.univ.polytech.citadellesgroupeq;

import static org.junit.jupiter.api.Assertions.*;

import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ColorTest {
    GameLogicManager game;
    Player firstP, secondP;
    @BeforeEach
    void setup() {
        game=new GameLogicManager();
        firstP=game.getPlayersList().get(0);
        secondP=game.getPlayersList().get(1);
    }

    @Test
    void testGrayDoesntWinCoins() {
        firstP.setRole(Role.ASSASSIN);//an arbitrary role with GRAY color
        firstP.addDistrictToCity(new District("temple", 8, Color.GRAY, "null"));
        firstP.addDistrictToCity(new District("autre", 9, Color.GRAY, "null"));
        firstP.addDistrictToCity(new District("autreencore", 9, Color.RED, "null"));//added another color to check

        RoundSummary summary=game.playPlayerTurn(firstP);
        assertEquals(0, summary.getCoinsWonByColorCards());
    }

    @Test
    void testRedColorWinCoins() {//or any other color
        firstP.setRole(Role.CONDOTTIERE);//condottiere is RED
        firstP.addDistrictToCity(new District("temple", 8, Color.GRAY, "null"));
        firstP.addDistrictToCity(new District("temple1", 8, Color.RED, "null"));
        firstP.addDistrictToCity(new District("temple2", 8, Color.BLUE, "null"));
        firstP.addDistrictToCity(new District("anotherTemple", 8, Color.RED, "null"));
        firstP.addDistrictToCity(new District("temple3", 8, Color.PURPLE, "null"));//added some other colors to check it doesn't count other colors.

        RoundSummary summary=game.playPlayerTurn(firstP);
        assertEquals(2, summary.getCoinsWonByColorCards());
    }
}
