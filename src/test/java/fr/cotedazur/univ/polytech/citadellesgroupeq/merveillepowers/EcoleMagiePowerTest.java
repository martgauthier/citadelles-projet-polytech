package fr.cotedazur.univ.polytech.citadellesgroupeq.merveillepowers;

import fr.cotedazur.univ.polytech.citadellesgroupeq.District;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.AlwaysSpendPlayer;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;
import fr.cotedazur.univ.polytech.citadellesgroupeq.strategies.DefaultStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

class EcoleMagiePowerTest {
    Player player;
    RoundSummary summary;
    GameLogicManager game;

    @BeforeEach
    void setup() {
        summary=new RoundSummary();
        game=new GameLogicManager();
        player= Mockito.spy(new AlwaysSpendPlayer(0, game.getCardDeck()));
        player.setStrategy(new DefaultStrategy(player));
        game.getPlayersList().set(0, player);
    }


    @Test
    void applyPowerEcoleDeMagie() {
        player.setRole(Role.MARCHAND);
        doNothing().when(player).draw2Coins(any());
        doReturn(Optional.empty()).when(player).getChoosenDistrictToBuy();
        District ecoleDeMagieDistrict = new District("Ecole de Magie", 3,
                "Purple", "Ecole de magie power");

        player.addDistrictToCity(ecoleDeMagieDistrict);
        game.playPlayerTurn(player);//will call PowerManager in player turn


        assertEquals(4,player.getCash());// Une pièce grace au pouvoir du marchand et une grace au pouvoir de l'ecole de magie
    }
}
