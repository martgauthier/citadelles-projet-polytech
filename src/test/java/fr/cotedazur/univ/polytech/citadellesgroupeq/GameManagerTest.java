package fr.cotedazur.univ.polytech.citadellesgroupeq;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameManagerTest {
    GameManager game;
    public static final ArrayList<Role> TOO_SHORT_ROLES_LIST = new ArrayList<>(List.of(Role.ROI));
    public static final ArrayList<Role> CORRECT_ROLES_LIST = new ArrayList<>(List.of(Role.ROI, Role.ASSASSIN, Role.ARCHITECTE));
    public static final ArrayList<Role> FULL_EMPTY_ROLES_LIST = new ArrayList<>(List.of(Role.EMPTY_ROLE, Role.EMPTY_ROLE, Role.EMPTY_ROLE));

    @BeforeEach
    void setup() {
        game = new GameManager();
    }

    @Test
    void testThrowMakeAllPlayersSelectRole() {
        assertThrows(IllegalArgumentException.class, () -> game.makeAllPlayersSelectRole(TOO_SHORT_ROLES_LIST));
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
        assertThrows(IllegalArgumentException.class, () -> game.setAvailableRoles(8));
        //On test 10 fois pour vérifier que la méthode est stable malgré l'aléatoire
        for(int i = 0; i < 10; i++){
            List<Role> availableRoles = game.setAvailableRoles(2);
            //On vérifie que les roles existent et qu'il y en a 3
            assertEquals(3, availableRoles.size());
            for (Role role : availableRoles) {
                Role.valueOf(role.toString()); // Si le nom du rôle n'existe pas une IllegalArgumentException sera levée
            }
        }
    }
}