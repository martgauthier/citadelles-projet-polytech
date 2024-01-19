package fr.cotedazur.univ.polytech.citadellesgroupeq.players;

import fr.cotedazur.univ.polytech.citadellesgroupeq.Color;
import fr.cotedazur.univ.polytech.citadellesgroupeq.District;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class KingPlayerTest {
    Player kingPlayer;
    @BeforeEach
    void setup() {
        kingPlayer=new KingPlayer(0);
    }

    @Test
    void testChoosenCitadelToBuy() {
        kingPlayer.setCash(1000);//no matter his money
        List<District> districtList=new ArrayList<>(List.of(
                new District("temple", 2, Color.YELLOW),
                new District("temple", 4, Color.YELLOW),
                new District("temple", 2, Color.GRAY),
                new District("temple", 8, Color.RED),
                new District("temple", 5, Color.PURPLE),
                new District("temple", 3, Color.BLUE)
        ));

        kingPlayer.setCardsInHand(districtList);

        assertEquals(districtList.get(0), kingPlayer.getChoosenDistrictToBuy().get());

        districtList.remove(0);
        assertEquals(districtList.get(0), kingPlayer.getChoosenDistrictToBuy().get());

        districtList.remove(0);
        assertEquals(districtList.get(2), kingPlayer.getChoosenDistrictToBuy().get());

        districtList.remove(2);

        assertEquals(districtList.get(1), kingPlayer.getChoosenDistrictToBuy().get());

        districtList.remove(1);
        assertEquals(districtList.get(1), kingPlayer.getChoosenDistrictToBuy().get());
    }

    @Test
    void testDefaultRoleToKillAsAssassin() {
        kingPlayer.setRole(Role.ASSASSIN);
        List<Role> rolesAvailable = List.of(Role.ROI, Role.ARCHITECTE, Role.CONDOTTIERE);
        assertEquals(Role.ROI, kingPlayer.selectRoleToKillAsAssassin(rolesAvailable));

        assertEquals(Role.ARCHITECTE, kingPlayer.selectRoleToKillAsAssassin(List.of(Role.ASSASSIN, Role.ARCHITECTE)));
    }
}
