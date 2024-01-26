package fr.cotedazur.univ.polytech.citadellesgroupeq.merveillepowers;

import fr.cotedazur.univ.polytech.citadellesgroupeq.District;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.RealEstatePlayer;
import fr.cotedazur.univ.polytech.citadellesgroupeq.strategies.DefaultStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BibliothequePowerTest {
    Player player;
    RoundSummary summary;
    GameLogicManager game;

    @BeforeEach
    void setup() {
        summary=new RoundSummary();
        game=new GameLogicManager();
        player= Mockito.spy(new RealEstatePlayer(0, game.getCardDeck()));
        player.setStrategy(new DefaultStrategy(player));
        game.getPlayersList().set(0, player);
    }

    @Test
    void applyPowerBibliotheque(){
        player.setRole(Role.MARCHAND);

        District biblio =new District("Bibliotheque",6,"Purple","Bibliotheque power");
        player.addDistrictToCity(biblio);
        game.playPlayerTurn(player);//will pick cards because he is RealEstate
        assertEquals(4,player.getCardsInHand().toArray().length);
    }
}
