package fr.cotedazur.univ.polytech.citadellesgroupeq;

import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.RealEstatePlayer;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

class PowerMerveilleTest {

    @Test
    void applyPowerEcoleDeMagie() {
        GameLogicManager game = new GameLogicManager();
        GameOutputManager gameOutputManager = new GameOutputManager();
        Player player = new RealEstatePlayer(0);
        player.setRole(Role.MARCHAND);

        District ecoleDeMagieDistrict = new District("Ecole de Magie", 3,
                                "Purple", "Ecole de magie power");

        player.addDistrictToCity(ecoleDeMagieDistrict);


        PowerManager powerManager = new PowerManager(game);
        game.playPlayerTurn(player);//will call PowerManager in player turn


        assertEquals(2,player.getCash());// Une pièce grace au pouvoir du marchand et une grace au pouvoir de l'ecole de magie

        gameOutputManager.describePlayerRound(player, game);
    }

    /** TEST OF DONJON IN CondottiereTest **/
    @Test
    void applyPowerBibliotheque(){
        GameLogicManager game = new GameLogicManager();
        GameOutputManager gameOutputManager = new GameOutputManager();
        Player player = new RealEstatePlayer(0);
        player.setRole(Role.MARCHAND);

        District biblio =new District("Bibliotheque",6,"Purple","Bibliotheque power");
        player.addDistrictToCity(biblio);
        game.playPlayerTurn(player);
        assertEquals(4,player.getCardsInHand().toArray().length);

    }

}