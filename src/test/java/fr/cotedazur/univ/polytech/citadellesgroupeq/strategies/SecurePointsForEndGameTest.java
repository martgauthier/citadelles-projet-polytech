package fr.cotedazur.univ.polytech.citadellesgroupeq.strategies;

import fr.cotedazur.univ.polytech.citadellesgroupeq.Color;
import fr.cotedazur.univ.polytech.citadellesgroupeq.District;
import fr.cotedazur.univ.polytech.citadellesgroupeq.DistrictsJSONReader;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.AlwaysSpendPlayer;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SecurePointsForEndGameTest {
    Player secureLastPlayer;
    IStrategy secureLastStrat;
    GameLogicManager game;
    DistrictsJSONReader pioche;
    @BeforeEach
    void setUp() {
        pioche=new DistrictsJSONReader();
        secureLastPlayer = new AlwaysSpendPlayer(0, pioche);
        secureLastStrat = new SecurePointsForEndGame(secureLastPlayer);
        secureLastPlayer.setStrategy(secureLastStrat);
        game = new GameLogicManager(List.of(secureLastPlayer));
        secureLastPlayer.setRole(Role.CONDOTTIERE);
    }

    @Test
    void testGetChoosenDistrictToBuy() {
        secureLastPlayer.setCash(200);
        District district1 = new District("avion", 5, Color.BLUE, "null");
        District district2 = new District("Université", 10, Color.YELLOW, "null");
        District district3 = new District("Donjon", 5, Color.YELLOW, "null");
        // il manque Vert et Rouge
        District district4 = new District("Bibliotheque", 10, Color.RED, "null");
        District district5 = new District("Temple", 5, Color.GREEN, "null");
        District district6 = new District("Cimetiere", 6, Color.BLUE, "null");
        District district7 = new District("Dracoport", 9, Color.PURPLE, "null" );

        // Cité
        secureLastPlayer.addDistrictToCity(district1);
        secureLastPlayer.addDistrictToCity(district2);
        secureLastPlayer.addDistrictToCity(district3);

        // Main
        secureLastPlayer.addCardToHand(district4);
        secureLastPlayer.addCardToHand(district5);
        secureLastPlayer.addCardToHand(district6);
        secureLastPlayer.addCardToHand(district7);

        // On peut pas compléter la couleur donc on prend la plus chère
        assertEquals(secureLastStrat.getChoosenDistrictToBuy(), Optional.of(district4));
        secureLastPlayer.addDistrictToCity(district4);
        secureLastPlayer.removeCardFromHand(district4);

        // On peut compléter la couleur mais il est toujours préférable de prendre la carte la plus haute
        assertEquals(secureLastStrat.getChoosenDistrictToBuy(), Optional.of(district7));
        secureLastPlayer.addDistrictToCity(district7);
        secureLastPlayer.removeCardFromHand(district7);

        // On peut compléter la couleur et c'est le meilleur choix
        assertEquals(secureLastStrat.getChoosenDistrictToBuy(), Optional.of(district5));
    }

}