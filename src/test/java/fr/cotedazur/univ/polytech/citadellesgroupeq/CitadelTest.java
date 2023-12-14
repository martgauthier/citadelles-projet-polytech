package fr.cotedazur.univ.polytech.citadellesgroupeq;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CitadelTest {
    @Test
    void testColorConstructor() {
        Citadel firstCitadel=new Citadel("temple", 8, "gray");
        Citadel secondCitadel=new Citadel("temple", 8, Color.GRAY);
        assertEquals(firstCitadel, secondCitadel);

        assertThrows(IllegalArgumentException.class, () -> new Citadel("temple", 8, "hehe"));//incorrect value for color
    }


}