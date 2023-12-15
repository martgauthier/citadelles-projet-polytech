package fr.cotedazur.univ.polytech.citadellesgroupeq;

import static org.junit.jupiter.api.Assertions.*;

import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ColorTest {
    GameManager game;
    Player firstP, secondP;
    @BeforeEach
    void setup() {
        game=new GameManager();
        firstP=game.getPlayersList().get(0);
        secondP=game.getPlayersList().get(1);
    }

    @Test
    void testGrayDoesntWinCoins() {
        firstP.setRole(Role.ASSASSIN);//arbitrary role with GRAY color
        firstP.addCitadelToCity(new Citadel("temple", 8, Color.GRAY));
        firstP.addCitadelToCity(new Citadel("autre", 9, Color.GRAY));
        firstP.addCitadelToCity(new Citadel("autreencore", 9, Color.RED));//added another color to check

        RoundSummary summary=game.playPlayerTurn(firstP);
        assertEquals(0, summary.getCoinsWonByColorCards());
    }

    @Test
    void testRedColorWinCoins() {//or any other color
        firstP.setRole(Role.CONDOTTIERE);//condottiere is RED
        firstP.addCitadelToCity(new Citadel("temple", 8, Color.GRAY));
        firstP.addCitadelToCity(new Citadel("temple", 8, Color.RED));
        firstP.addCitadelToCity(new Citadel("temple", 8, Color.BLUE));
        firstP.addCitadelToCity(new Citadel("temple", 8, Color.RED));
        firstP.addCitadelToCity(new Citadel("temple", 8, Color.PURPLE));//added some other colors to check it doesn't count other colors

        RoundSummary summary=game.playPlayerTurn(firstP);
        assertEquals(2, summary.getCoinsWonByColorCards());
    }
}
