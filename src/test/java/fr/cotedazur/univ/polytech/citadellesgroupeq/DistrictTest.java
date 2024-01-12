package fr.cotedazur.univ.polytech.citadellesgroupeq;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DistrictTest {
    @Test
    void testColorConstructor() {
        District firstDistrict =new District("temple", 8, "gray", "null");
        District secondDistrict =new District("temple", 8, Color.GRAY, "null");
        assertEquals(firstDistrict, secondDistrict);

        assertThrows(IllegalArgumentException.class, () -> new District("temple", 8, "hehe", "null"));//incorrect value for color
    }


}