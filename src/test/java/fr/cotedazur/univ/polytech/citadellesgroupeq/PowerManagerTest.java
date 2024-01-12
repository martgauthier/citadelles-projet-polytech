package fr.cotedazur.univ.polytech.citadellesgroupeq;

import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.RandomPlayer;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.RealEstatePlayer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PowerManagerTest {

    @Test
    void applyPowerEcoleDeMagie() {

        GameLogicManager game = new GameLogicManager();
        Player player = new RealEstatePlayer(0);
        player.setRole(Role.MARCHAND);

        District ecoleDeMagieDistrict = new District("Ecole de Magie", 3,
                                "Purple", "Ecole de magie power");

        player.addDistrictToCity(ecoleDeMagieDistrict);


        PowerManager powerManager = new PowerManager(game);

        powerManager.applyPower(ecoleDeMagieDistrict, player);

        assertEquals(player.getRole().getColor(), ecoleDeMagieDistrict.getColor());
        game.playPlayerTurn(player);
        assertEquals(2,player.getCash());// Une pièce r=grace au pouvoir du marchand et une grace au pouvoir de l'ecole de magie
    }
}