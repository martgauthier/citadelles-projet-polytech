package fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic;


import fr.cotedazur.univ.polytech.citadellesgroupeq.Citadel;
import fr.cotedazur.univ.polytech.citadellesgroupeq.CitadelsJSONReader;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameManagerTest {
    GameManager game;
    CitadelsJSONReader reader;
    public static final List<Role> TOO_SHORT_ROLES_LIST = new ArrayList<>(List.of(Role.ROI));
    public static final List<Role> CORRECT_ROLES_LIST = new ArrayList<>(List.of(Role.ROI, Role.ASSASSIN, Role.ARCHITECTE,Role.MARCHAND,Role.VOLEUR));
    public static final List<Role> FULL_EMPTY_ROLES_LIST = new ArrayList<>(List.of(Role.EMPTY_ROLE, Role.EMPTY_ROLE, Role.EMPTY_ROLE, Role.EMPTY_ROLE));

    @BeforeEach
    void setup() throws ParseException {
        game = new GameManager();
        reader=new CitadelsJSONReader();
    }

    @Test
    void testThrowMakeAllPlayersSelectRole() {
        assertThrows(IllegalArgumentException.class, () -> game.makeAllPlayersSelectRole(TOO_SHORT_ROLES_LIST));
        assertEquals(5, CORRECT_ROLES_LIST.size());
        game.makeAllPlayersSelectRole(CORRECT_ROLES_LIST);//asserts that it throws nothing
        for (Player player : game.getPlayersList()) {
            assertNotEquals(Role.EMPTY_ROLE, player.getRole());//vérifie que le rôle a bien été changé
        }
        assertThrows(IllegalArgumentException.class, () -> game.makeAllPlayersSelectRole(FULL_EMPTY_ROLES_LIST));
    }


    @Test
    void testMasterOfTheGameIndex() {
        game.setMasterOfTheGameIndex(-1);
        assertEquals(0, game.getMasterOfTheGameIndex());//default value
        game.setMasterOfTheGameIndex(5);//value is too high
        assertEquals(0, game.getMasterOfTheGameIndex());
        game.setMasterOfTheGameIndex(1);
        assertEquals(1, game.getMasterOfTheGameIndex());
    }

    @Test
    void testRolesSelected() {
        game.makeAllPlayersSelectRole();
        List<Role> rolesSelected = new ArrayList<>();
        for (Player player : game.getPlayersList()) {
            assertFalse(rolesSelected.contains(player.getRole()));
            assertNotSame(Role.EMPTY_ROLE, player.getRole());//checks that role cannot be empty
            rolesSelected.add(player.getRole());
        }
    }

    @Test
    void testSetAvailableRoles() {
        assertThrows(IllegalArgumentException.class, () -> game.generateAvailableRoles(8));
        //On test 10 fois pour vérifier que la méthode est stable malgré l'aléatoire
        for(int i = 0; i < 10; i++){
            List<Role> availableRoles = game.generateAvailableRoles(2);
            //On vérifie que les roles existent et qu'il y en a 3
            assertEquals(3, availableRoles.size());
            for (Role role : availableRoles) {
                assertNotSame(Role.EMPTY_ROLE, role);
            }
        }
    }

    @RepeatedTest(50)
    void testCreatingGameWithPlayers() {
        for(Player player: game.getPlayersList()) {
            assertEquals(2, player.getCardsInHand().size());
        }
    }

    @Test
    void testGameUsesCopyOfDefaultPlayerList() {//verifie que le Game fait bien une deep copy du DEFAULT_PLAYER_LIST
        assertNotSame(GameManager.DEFAULT_PLAYER_LIST.get(0).hashCode(), game.getPlayersList().get(0).hashCode());
        game.getPlayersList().get(0).setRole(Role.ASSASSIN);
        assertNotSame(Role.ASSASSIN, GameManager.DEFAULT_PLAYER_LIST.get(0).getRole());
    }

    @RepeatedTest(50)
    void testPlayerTurn() {
        game = new GameManager();
        game.getPlayersList().get(0).setCash(1000);//rend un joueur capable d'acheter toutes ses cartes
        assertEquals(2,game.getPlayersList().get(0).getCardsInHand().size());

        RoundSummary summary=game.playPlayerTurn(game.getPlayersList().get(0));

        assertTrue(summary.hasBoughtCitadels());
        assertEquals(1, summary.getBoughtCitadels().size());

        int boughtCitadelPrice=summary.getBoughtCitadels().get(0).getCost();

        for(Citadel cardInHand: game.getPlayersList().get(0).getCardsInHand()) {
            assertTrue(boughtCitadelPrice <= cardInHand.getCost());
        }

        assertTrue(summary.hasPickedCards() ^ summary.hasPickedCash()); //opérateur XOR, pour vérifier que le joueur n'a fait qu'un des deux
    }
    @Test
    void testFinishCondition(){
        List<Citadel> citadels=new ArrayList<>();
        citadels.add(reader.getFromIndex(0));
        citadels.add(reader.getFromIndex(1));
        citadels.add(reader.getFromIndex(2));
        citadels.add(reader.getFromIndex(3));
        citadels.add(reader.getFromIndex(4));
        citadels.add(reader.getFromIndex(5));
        citadels.add(reader.getFromIndex(6));
        citadels.add(reader.getFromIndex(7));
        game.getPlayersList().get(0).addAllCitadelsToCity(citadels);
        game.playPlayerTurn(game.getPlayersList().get(0));
        assertTrue(game.isFinished());
    }
}