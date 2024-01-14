package fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic;


import fr.cotedazur.univ.polytech.citadellesgroupeq.District;
import fr.cotedazur.univ.polytech.citadellesgroupeq.DistrictsJSONReader;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;
import fr.cotedazur.univ.polytech.citadellesgroupeq.strategies.DefaultStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class GameLogicManagerTest {
    GameLogicManager game;
    DistrictsJSONReader reader;
    public static final List<Role> TOO_SHORT_ROLES_LIST = new ArrayList<>(List.of(Role.ROI));
    public static final List<Role> CORRECT_ROLES_LIST = new ArrayList<>(List.of(Role.ROI, Role.ASSASSIN, Role.ARCHITECTE,Role.MARCHAND,Role.VOLEUR));
    public static final List<Role> FULL_EMPTY_ROLES_LIST = new ArrayList<>(List.of(Role.EMPTY_ROLE, Role.EMPTY_ROLE, Role.EMPTY_ROLE, Role.EMPTY_ROLE));

    @BeforeEach
    void setup() {
        game = new GameLogicManager();
        reader=new DistrictsJSONReader();
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
        for(int i=0; i < game.getPlayersList().size(); i++) {
            game.getPlayersList().set(i, Mockito.spy(game.getPlayersList().get(i)));
            game.getPlayersList().get(i).setStrategy(new DefaultStrategy(game.getPlayersList().get(i)));
        }

        game.makeAllPlayersSelectRole();
        List<Role> rolesSelected = new ArrayList<>();
        for (Player player : game.getPlayersList()) {
            verify(player, times(1)).selectRole(anyList());
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

    @Test
    void testCreatingGameWithPlayers() {
        for(Player player: game.getPlayersList()) {
            assertEquals(2, player.getCardsInHand().size());
        }
    }

    @Test
    void testGameCreatesDifferentInstancesFromDefaultPlayerList() {
        GameLogicManager firstGame = new GameLogicManager();
        GameLogicManager secondGame = new GameLogicManager();
        for(int i=0; i < firstGame.getPlayersList().size(); i++) {
            assertNotEquals(firstGame.getPlayersList().get(i), secondGame.getPlayersList().get(i));
        }

        assertEquals(firstGame.getPlayersList().get(0).getCash(), secondGame.getPlayersList().get(0).getCash());//par défaut, ils ont un cash à 0
        firstGame.getPlayersList().get(0).setCash(50);
        assertNotEquals(firstGame.getPlayersList().get(0).getCash(), secondGame.getPlayersList().get(0).getCash());//changer le cash d'un ne change pas le cash de l'autre
    }

    @Test
    void testPlayerTurn() {
        game = new GameLogicManager();
        game.getPlayersList().get(0).setCash(1000);//rend un joueur capable d'acheter toutes ses cartes
        game.getPlayersList().get(0).setRole(Role.MARCHAND);
        assertEquals(2,game.getPlayersList().get(0).getCardsInHand().size());

        RoundSummary summary=game.playPlayerTurn(game.getPlayersList().get(0));

        assertTrue(summary.hasBoughtDistricts());
        assertEquals(1, summary.getBoughtDistricts().size());

        int boughtDistrictPrice=summary.getBoughtDistricts().get(0).getCost();

        assertTrue(summary.hasPickedCards() ^ summary.hasPickedCash()); //opérateur XOR, pour vérifier que le joueur n'a fait qu'un des deux
    }
    @Test
    void testFinishCondition(){
        List<District> districts =new ArrayList<>();
        districts.add(reader.getFromIndex(0));
        districts.add(reader.getFromIndex(1));
        districts.add(reader.getFromIndex(2));
        districts.add(reader.getFromIndex(3));
        districts.add(reader.getFromIndex(4));
        districts.add(reader.getFromIndex(5));
        districts.add(reader.getFromIndex(6));
        districts.add(reader.getFromIndex(7));
        game.getPlayersList().get(0).addAllDistrictsToCity(districts);
        game.getPlayersList().get(0).setRole(Role.ASSASSIN);
        game.playPlayerTurn(game.getPlayersList().get(0));
        assertTrue(game.isFinished());
    }
}