package fr.cotedazur.univ.polytech.citadellesgroupeq;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DistrictTest {
    @Test
    void testColorConstructor() {
        District firstDistrict =new District("temple", 8, "gray");
        District secondDistrict =new District("temple", 8, Color.GRAY);
        assertEquals(firstDistrict, secondDistrict);

        assertThrows(IllegalArgumentException.class, () -> new District("temple", 8, "hehe"));//incorrect value for color
    }


}