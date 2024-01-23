package fr.cotedazur.univ.polytech.citadellesgroupeq.rolepowers;

import fr.cotedazur.univ.polytech.citadellesgroupeq.Color;
import fr.cotedazur.univ.polytech.citadellesgroupeq.District;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.*;
import fr.cotedazur.univ.polytech.citadellesgroupeq.strategies.DefaultStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class ArchitecteTest {
    GameLogicManager game;
    RoundSummary summary;
    Player assassinPlayer;
    Player otherRolePlayer;
    Player voleurPlayer;

    Player magicienPlayer;

    Player condottierePlayer;

    Player evequePlayer;

    Player architectePlayer;

    District basicDistrict;

    @BeforeEach
    void setup() {
        assassinPlayer = new AlwaysSpendPlayer(0);
        voleurPlayer = new AlwaysSpendPlayer(1);
        otherRolePlayer = new RealEstatePlayer(2);
        magicienPlayer=new ColorPlayer(3);
        condottierePlayer=new RandomPlayer(4);
        evequePlayer=new ColorPlayer(5);
        architectePlayer=new RealEstatePlayer(6);

        assassinPlayer.setRole(Role.ASSASSIN);
        voleurPlayer.setRole(Role.VOLEUR);
        magicienPlayer.setRole(Role.MAGICIEN);
        condottierePlayer.setRole(Role.CONDOTTIERE);
        evequePlayer.setRole(Role.EVEQUE);
        architectePlayer.setRole(Role.ARCHITECTE);

        basicDistrict=new District("temple", 5, Color.PURPLE, "null");
        game=new GameLogicManager(List.of(assassinPlayer, voleurPlayer,otherRolePlayer, magicienPlayer, condottierePlayer, evequePlayer, architectePlayer));
        summary=new RoundSummary();
    }
    void initSpyArchitecte() {
        architectePlayer=Mockito.spy(architectePlayer);
        architectePlayer.setStrategy(new DefaultStrategy(architectePlayer));
    }

    @Test
    void testArchitecteAlwaysPicks3Cards() {
        initSpyArchitecte();
        architectePlayer.playTurn(summary, game);
        verify(architectePlayer, times(3)).pickCard(any());//il a pioché 2 fois grâce au rôle + 1 FOIS CAR IL EST REALESTATEPLAYER
    }

    @Test
    void testArchitecteCanBuy3Districts() {
        initSpyArchitecte();
        architectePlayer.setCash(11000);//make him rich

        List<District> architecteHand = new ArrayList<>(List.of(
                new District("temple0", 8, Color.GRAY, "null"),
                new District("temple1", 8, Color.GRAY, "null"),
                new District("temple2", 8, Color.GRAY, "null"),
                new District("temple3", 8, Color.GRAY, "null"),
                new District("temple4", 8, Color.GRAY, "null"),
                new District("temple5", 8, Color.GRAY, "null"),
                new District("temple6", 8, Color.GRAY, "null"),
                new District("temple7", 8, Color.GRAY, "null")));

        architectePlayer.setCardsInHand(architecteHand); // il a 8 cartes en main, et le real estate player veut toujours acheter à 8 cartes

        architectePlayer.playTurn(summary, game);

        verify(architectePlayer, times(3)).buyDistrictsDuringTurn(summary);
        assertEquals(3, summary.getBoughtDistricts().size());
    }
}
