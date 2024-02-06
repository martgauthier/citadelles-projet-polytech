package fr.cotedazur.univ.polytech.citadellesgroupeq.players;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Color;
import fr.cotedazur.univ.polytech.citadellesgroupeq.District;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.strategies.DefaultStrategy;
import fr.cotedazur.univ.polytech.citadellesgroupeq.strategies.MattMoreThan5CitiesStrategy;
import fr.cotedazur.univ.polytech.citadellesgroupeq.strategies.SecurePointsForEndGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
public class RichardPlayerTest {
    Player richardPlayer;
    Player otherPlayer;
    Player otherPlayer2;
    GameLogicManager game;
    RoundSummary summary;
    @BeforeEach
    void setup() {
        game = new GameLogicManager();
        richardPlayer = Mockito.spy(new RichardPlayer(0, game.getCardDeck()));
        richardPlayer.setStrategy(new DefaultStrategy(richardPlayer));//necessary because of mock
        otherPlayer = Mockito.spy(new ThomasPlayer(1, game.getCardDeck()));
        otherPlayer.setStrategy(new DefaultStrategy(otherPlayer));//necessary because of mock
        otherPlayer2=Mockito.spy(new ThomasPlayer(2,game.getCardDeck()));
        otherPlayer2.setStrategy(new DefaultStrategy(otherPlayer2));

        game.getPlayersList().set(0, richardPlayer);
        game.getPlayersList().set(1, otherPlayer);
        game.getPlayersList().set(2,otherPlayer2);
        summary = new RoundSummary();
    }
    @Test
    void testUsesSecurePointStrategy() {
        game.finishGame();
        game.makeAllPlayersSelectRole();
        game.playPlayerTurn(richardPlayer);
        assertInstanceOf(SecurePointsForEndGame.class, richardPlayer.getStrategy());
    }
    @Test
    void testSelectPlayerToExchangeCardsWithAsMagicien(){
        otherPlayer.addCardToHand(new District("Maison",5,Color.BLUE));
        assertEquals(otherPlayer,richardPlayer.selectPlayerToExchangeCardsWithAsMagicien(game.getPlayersList()));
        otherPlayer2.addCardToHand(new District("Maison",5,Color.BLUE));
        otherPlayer2.addCardToHand(new District("Maison",5,Color.BLUE));
        assertEquals(otherPlayer2,richardPlayer.selectPlayerToExchangeCardsWithAsMagicien(game.getPlayersList()));
    }
    @Test
    void testGetChoosenDistrictToBuy_WhenNoCardsToBuy_ReturnsEmpty() {
        richardPlayer.clearHand();
        assertEquals(Optional.empty(), richardPlayer.getChoosenDistrictToBuy());
        assertEquals(0, richardPlayer.getCardsInHand().size());
        District villa = new District("Villa", 4, Color.RED,"null");
        District bateau = new District("Bateau",5,Color.BLUE, "null");
        District jetSki = new District("Jet Ski", 3,Color.YELLOW, "null");
        richardPlayer.setCash(20);
        richardPlayer.setRole(Role.MARCHAND);

        richardPlayer.addCardToHand(bateau);
        richardPlayer.addCardToHand(villa);
        richardPlayer.addCardToHand(new District("Jacuzzi", 4, Color.GREEN, "null"));

        // il devrait choisir la ville, car elle est le plus proche de 3 et rouge
        assertEquals(Optional.of(villa), richardPlayer.getChoosenDistrictToBuy());
        richardPlayer.removeCardFromHand(villa);

        // il devrait choisir le bateau, car il est bleu meme si plus éloigné de la valeur 3
        assertEquals( Optional.of(bateau), richardPlayer.getChoosenDistrictToBuy());
        richardPlayer.removeCardFromHand(bateau);
        richardPlayer.addCardToHand(jetSki);

        // il devrait choisir le jet ski, car comme il n'y a aucunes cartes de la couleur verte dans sa main, il prend le plus proche de trois
        assertEquals(Optional.of(jetSki), richardPlayer.getChoosenDistrictToBuy());
    }
    @Test
    void testSelectRoleToKill(){
        List<Role> availableRole=new ArrayList<>();
        availableRole.add(Role.ROI);
        availableRole.add(Role.MARCHAND);
        availableRole.add(Role.ARCHITECTE);
        assertEquals(Role.ROI,richardPlayer.selectRoleToKillAsAssassin(availableRole));
        List<Role> availableRole2=new ArrayList<>();
        availableRole2.add(Role.CONDOTTIERE);
        availableRole2.add(Role.VOLEUR);
        availableRole2.add(Role.ASSASSIN);
        assertEquals(Role.CONDOTTIERE,richardPlayer.selectRoleToKillAsAssassin(availableRole2));
    }
    @Test
    void testSelectRoleDefault(){
        richardPlayer.selectAndSetRole(List.of(Role.ROI, Role.EVEQUE, Role.ASSASSIN, Role.MARCHAND, Role.MAGICIEN), List.of());
        assertEquals(Role.EVEQUE,richardPlayer.getRole());
        richardPlayer.clearHand();
        richardPlayer.selectAndSetRole(List.of(Role.ROI, Role.EVEQUE, Role.ASSASSIN, Role.MARCHAND, Role.MAGICIEN), List.of());
        assertEquals(Role.MAGICIEN,richardPlayer.getRole());

    }
}
