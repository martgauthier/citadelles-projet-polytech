package fr.cotedazur.univ.polytech.citadellesgroupeq.evaluator;

import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.playerevaluator.GameStatsCsv;
import fr.cotedazur.univ.polytech.citadellesgroupeq.playerevaluator.StatsManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class StatsManagerTest {
    GameLogicManager game;

    StatsManager stats;

    RoundSummary summary;

    Player firstPlayer;

    String[] stat, desiredStat;

    GameStatsCsv csv;

    @BeforeEach
    void setup() {
        game=new GameLogicManager();
        firstPlayer=game.getPlayersList().get(0);

        summary=new RoundSummary();

        game.makeAllPlayersSelectRole();

        stats=new StatsManager(game.getPlayersList());
        csv=new GameStatsCsv();
    }

    @Test
    void testAddSummary() {
        firstPlayer.playTurn(summary, game);

        assertEquals(0, stats.getPlayerSummaries().get(firstPlayer).size());
        stats.addSummary(firstPlayer, summary);
        assertEquals(1, stats.getPlayerSummaries().get(firstPlayer).size());
    }

    @Test
    void testGetPlayerWinning() {
        assertTrue(stats.getPlayerWinning().isEmpty());
        stats.setWinForPlayer(firstPlayer);

        assertTrue(stats.getPlayerWinning().isPresent());
        assertEquals(firstPlayer, stats.getPlayerWinning().get());
    }

    @Test
    void testSetTie() {
        for(boolean playerHasTieGame: stats.getPlayerHasTieGameArray()) {
            assertFalse(playerHasTieGame);
        }

        stats.setTieForPlayer(firstPlayer);

        assertTrue(stats.getPlayerHasTieGameArray()[0]);
    }

    public void setEmptyStatLineForPlayer(int playerId) {
        summary.setRole(Role.EVEQUE);

        Player joueur=game.getPlayersList().get(playerId);

        game.makeScoreOfPlayer(joueur, summary);

        stats.addSummary(joueur, summary);

        stat=stats.getStatForAPlayer(joueur, game, 0);

        desiredStat=new String[] {"1",
                firstPlayer.getBotLogicName(),
                "0",
                "EVEQUE",
                "0",
                "0",
                "0",
                "Non",
                "Non"
        };
    }

    @Test
    void testStatsForSingleRoundSummaryIsSame() {
        setEmptyStatLineForPlayer(0);

        assertArrayEquals(stat, desiredStat);
    }

    @Test
    void testSetTieForPlayer() {
        setEmptyStatLineForPlayer(0);

        stats.setTieForPlayer(firstPlayer);
        stat=stats.getStatForAPlayer(firstPlayer, game, 0);
        desiredStat[8]="Oui";

        assertArrayEquals(stat, desiredStat);
    }

    @Test
    void testSetWinForPlayer() {
        setEmptyStatLineForPlayer(0);

        stats.setWinForPlayer(firstPlayer);
        stat=stats.getStatForAPlayer(firstPlayer, game, 0);
        desiredStat[7]="Oui";
        assertArrayEquals(stat, desiredStat);
    }

    void deleteFileIfPresent() {
        File file = new File(GameStatsCsv.CSV_PATH.toString());
        if (file.exists()) {
            file.delete();
        }
    }

    void deleteDetailsFileIfPresent() {
        File file = new File(GameStatsCsv.DETAILS_CSV_PATH.toString());
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void testPreferredRoleForPlayer() {
        setEmptyStatLineForPlayer(0);

        RoundSummary firstSummaryAssassin=new RoundSummary();
        firstSummaryAssassin.setRole(Role.ASSASSIN);
        stats.addSummary(firstPlayer, firstSummaryAssassin);

        //same thing, for secondSummaryAssassin
        RoundSummary secondSummaryAssassin=new RoundSummary();
        secondSummaryAssassin.setRole(Role.ASSASSIN);
        stats.addSummary(firstPlayer, secondSummaryAssassin);

        RoundSummary summaryArchitecte=new RoundSummary();
        summaryArchitecte.setRole(Role.ARCHITECTE);
        stats.addSummary(firstPlayer, summaryArchitecte);

        stats.setStatOfFavoriteRole(firstPlayer);

        stat=stats.getStatForAPlayer(firstPlayer, game, 0);
        desiredStat[3]="ASSASSIN";

        assertArrayEquals(stat, desiredStat);
    }

    @Test
    void testWriteInCsv() {
        setEmptyStatLineForPlayer(0);
        setEmptyStatLineForPlayer(1);
        setEmptyStatLineForPlayer(2);
        setEmptyStatLineForPlayer(3);

        deleteDetailsFileIfPresent();
        assertFalse(Files.exists(GameStatsCsv.DETAILS_CSV_PATH));//vérifie qu'il n'y a rien pour l'instant

        stats.writePlayersDetailsStatInCsv(csv, game, 0);
        assertTrue(Files.exists(GameStatsCsv.DETAILS_CSV_PATH));//vérifie que ca a bien écrit
    }

    @Test
    void testUpdateInCsvWrites() {
        testWriteInCsv();

        stats.updatePlayerStatInCsv(csv, game, 0);
        assertTrue(Files.exists(GameStatsCsv.DETAILS_CSV_PATH));//vérifie que ca a bien écrit
        deleteDetailsFileIfPresent();
        deleteFileIfPresent();
    }

    @AfterAll
    static void deleteAfterEverything() {
        StatsManagerTest test=new StatsManagerTest();
        test.deleteDetailsFileIfPresent();
        test.deleteFileIfPresent();
    }
}
