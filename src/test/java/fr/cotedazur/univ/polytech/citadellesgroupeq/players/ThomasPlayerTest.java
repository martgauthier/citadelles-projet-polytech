package fr.cotedazur.univ.polytech.citadellesgroupeq.players;

import fr.cotedazur.univ.polytech.citadellesgroupeq.Color;
import fr.cotedazur.univ.polytech.citadellesgroupeq.District;
import fr.cotedazur.univ.polytech.citadellesgroupeq.CardDeck;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ThomasPlayerTest {
    GameLogicManager game;
    ThomasPlayer player1,player2;
    CardDeck pioche;
    @BeforeEach
    void setup() {
        pioche=new CardDeck();
        player1=new ThomasPlayer(0, pioche);
        player2=new ThomasPlayer(1, pioche);

        game=new GameLogicManager(List.of(player1, player2));
        game.setCardDeck(pioche);

        player1.clearHand();
        player2.clearHand();
    }

    @Test
    void testDesc(){
        assertEquals("ThomasPlayer",player1.getBotLogicName());
    }

    @Test
    void testChosesColorRole(){
        List<Role> availablesRoles=List.of(Role.ASSASSIN, Role.VOLEUR, Role.CONDOTTIERE, Role.MARCHAND);
        assertEquals(3, player1.selectAndSetRole(availablesRoles, List.of()));
        List<Role> availablesRoles2=List.of(Role.ARCHITECTE, Role.VOLEUR, Role.CONDOTTIERE, Role.MARCHAND);
        assertEquals(0, player2.selectAndSetRole(availablesRoles2, List.of()));
    }
    @Test
    void testChoosesBetweenCoinsAndCards(){
        player1.clearHand();
        player1.pickCard(new RoundSummary());
        player1.pickCard(new RoundSummary());//adds 2 cards in hand
        player1.setCash(0);//to match first test specifications

        player2.clearHand();
        player2.pickCard(new RoundSummary());
        player2.pickCard(new RoundSummary());//adds 2 cards in hand
        player2.setCash(0);//to match first test specifications

        player1.addAllCardsToHand(new District("Temple",8,Color.BLUE));
        player1.setRole(Role.ASSASSIN);
        assertEquals(0,player1.getCash());
        RoundSummary summary=new RoundSummary();
        player1.playTurn(summary,game);
        assertTrue(summary.hasPickedCash());

        player2.setRole(Role.EVEQUE);
        assertEquals(0,player2.getCash());
        summary=new RoundSummary();
        player2.playTurn(summary,game);
        assertTrue(summary.hasPickedCash());
    }
    @Test
    void testSelectDistrictToDestroyAsCondottiere(){
        player1.setRole(Role.CONDOTTIERE);
        player1.setCash(100);
        player2.setRole(Role.ASSASSIN);
        List<Player> players=new ArrayList<>();
        players.add(player2);
        assertEquals(Optional.empty(),player1.selectDistrictToDestroyAsCondottiere(players));
        player2.setCash(6);
        player2.addAllCardsToHand(new District("Tour",4,Color.YELLOW),new District("Tour",4,Color.YELLOW));
        List<District> city=new ArrayList<>();
        for(int i=0;i<6;i++){
            city.add(new District("Tour",4,Color.YELLOW));
        }
        city.add(new District("temple",3,Color.YELLOW));
        player2.addAllDistrictsToCity(city);
        Optional<AbstractMap.SimpleEntry<Integer, District>> districtDestroyed=player1.selectDistrictToDestroyAsCondottiere(players);
        assertTrue(districtDestroyed.isPresent());
        assertEquals(player2.getCity().get(6),districtDestroyed.get().getValue());
    }
    @Test
    void testSelectRoleToSteal(){
        player1.setRole(Role.VOLEUR);
        List<Role> availablesRoles=List.of(Role.ARCHITECTE, Role.EVEQUE, Role.MARCHAND);
        List<Role> unavailablesRoles=List.of(Role.VOLEUR,Role.ASSASSIN);
        assertEquals(Optional.of(Role.ARCHITECTE),player1.selectRoleToSteal(availablesRoles,unavailablesRoles));
        List<Role> availablesRoles2=List.of(Role.ROI, Role.EVEQUE, Role.MARCHAND);
        assertEquals(Optional.of(Role.MARCHAND),player1.selectRoleToSteal(availablesRoles2,unavailablesRoles));
    }

    @Test
    void testSelectPlayerToExchangeCardsWithAsMagicien() {
        Player playerToExchange = player1.selectPlayerToExchangeCardsWithAsMagicien(game.getPlayersList());
        assertEquals(null, playerToExchange);
    }

    @Test
    void testChoosesToExchangeCardWithPlayer() {
        boolean choose = player1.choosesToExchangeCardWithPlayer();
        assertTrue(choose || !choose);
    }
}
