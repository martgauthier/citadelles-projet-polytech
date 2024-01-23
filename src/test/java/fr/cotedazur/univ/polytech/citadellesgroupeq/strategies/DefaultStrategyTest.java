package fr.cotedazur.univ.polytech.citadellesgroupeq.strategies;

import fr.cotedazur.univ.polytech.citadellesgroupeq.DistrictsJSONReader;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.AlwaysSpendPlayer;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class DefaultStrategyTest {
    Player basicPlayer;
    IStrategy defaultStrat;
    GameLogicManager game;
    DistrictsJSONReader pioche;
    @BeforeEach
    void initPlayer() {
        pioche=new DistrictsJSONReader();
        basicPlayer=new AlwaysSpendPlayer(0, pioche);
        defaultStrat=basicPlayer.getStrategy();
        game=new GameLogicManager(List.of(basicPlayer));
        game.setDistrictsJSONReader(pioche);
        basicPlayer.setRole(Role.EVEQUE);//set arbitrary role
    }

    @Test
    void testDefaultStrategyIsSameAsPlayerStrat() {
        basicPlayer= mock(AlwaysSpendPlayer.class);
        defaultStrat=new DefaultStrategy(basicPlayer);
        doReturn(defaultStrat).when(basicPlayer).getStrategy();


        assertEquals(basicPlayer.getChoosenDistrictToBuy(), basicPlayer.getStrategy().getChoosenDistrictToBuy());
        assertEquals(basicPlayer.selectDistrictToDestroyAsCondottiere(game.getPlayersList()), basicPlayer.getStrategy().selectDistrictToDestroyAsCondottiere(game.getPlayersList()));
        assertEquals(basicPlayer.selectCardsToExchangeWithPileAsMagicien(), basicPlayer.getStrategy().selectCardsToExchangeWithPileAsMagicien());
    }
}
