package fr.cotedazur.univ.polytech.citadellesgroupeq;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleTest {
    @Test
    void testNames() {
        assertEquals("assassin", Role.ASSASSIN.name().toLowerCase());
        assertEquals("condottiere", Role.CONDOTTIERE.name().toLowerCase());
    }

    @Test
    void testOrdinal() {
        assertEquals(0, Role.EMPTY_ROLE.ordinal());
        assertEquals(1, Role.ASSASSIN.ordinal());
        assertEquals(2, Role.VOLEUR.ordinal());
        assertEquals(3, Role.MAGICIEN.ordinal());
        assertEquals(4, Role.ROI.ordinal());
        assertEquals(5, Role.EVEQUE.ordinal());
        assertEquals(6, Role.MARCHAND.ordinal());
        assertEquals(7, Role.ARCHITECTE.ordinal());
        assertEquals(8, Role.CONDOTTIERE.ordinal());
    }
}